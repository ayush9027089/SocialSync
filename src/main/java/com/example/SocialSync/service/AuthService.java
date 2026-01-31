package social_media.social_media_handler.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.dto.auth.LoginRequest;
import social_media.social_media_handler.dto.auth.SignupRequest;
import social_media.social_media_handler.dto.auth.LoginResponse;
import social_media.social_media_handler.dto.auth.SignupResponse;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.repository.UserRepository;
import social_media.social_media_handler.service.email.EmailService;
import social_media.social_media_handler.util.AuthEmailTemplateUtil;
import social_media.social_media_handler.util.JwtUtil;
import social_media.social_media_handler.util.PasswordEmailTemplateUtil;
import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class AuthService implements UserDetailsService {
@Autowired
private UserRepository userRepository;
@Autowired
private PasswordEncoder passwordEncoder;
@Autowired
private JwtUtil jwtUtil;
@Autowired
private EmailService emailService;
@Override
public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
return userRepository.findByEmail(email)
.orElseThrow(() -> new UsernameNotFoundException("User not found"));
}
public SignupResponse signupUser(SignupRequest request) {
if (userRepository.existsByEmail(request.getEmail())) {
return new SignupResponse("Email already exists", null, null, false);
}
    String assignedRole = "ROLE_USER";
    boolean isAdminFlag = false;

    if (request.getAdmin() != null && !request.getAdmin().isEmpty()) {
        String correctSecret = "123456789"; 
        if (correctSecret.equals(request.getSecret())) {
            assignedRole = "ROLE_ADMIN";
            isAdminFlag = true;
        } else {
            return new SignupResponse("Invalid Secret Key for Admin!", null, null, false);
        }
    }
User user = User.builder()
.id(UUID.randomUUID().toString())
.username(request.getUsername())
.email(request.getEmail())
.password(passwordEncoder.encode(request.getPassword()))
.role(assignedRole)       
.isAdmin(isAdminFlag)
.createdAt(LocalDateTime.now())
.build();
try{
userRepository.save(user);
String token = jwtUtil.generateToken(user.getEmail(), user.getId());
try{
    emailService.sendEmail(
        user.getEmail(),
    "Welcome to Social Media Handler",
            AuthEmailTemplateUtil.signupSuccess(user.getUsername())
);
} catch (Exception e) {
        System.out.println("Email error: " + e.getMessage());
    }
return new SignupResponse("Signup successful", user.getEmail(), token, true);
}catch (Exception e) {
        return new SignupResponse("Database Error: " + e.getMessage(), null, null, false);
    }
}
public LoginResponse loginUser(LoginRequest request) {
User user = userRepository.findByEmail(request.getEmail())
.orElseThrow(() -> new RuntimeException("Invalid email or password"));
if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
throw new RuntimeException("Invalid email or password");
}
String token = jwtUtil.generateToken(user.getEmail(), user.getId());
Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
SecurityContextHolder.getContext().setAuthentication(authentication);
emailService.sendEmail(
user.getEmail(),
"Login Alert",
AuthEmailTemplateUtil.loginAlert(user.getUsername())
);
return new LoginResponse(token, user.getEmail(), user.getUsername(), "Login successful");
}
public void forgotPassword(String email) {
User user = userRepository.findByEmail(email)
.orElseThrow(() -> new RuntimeException("User not found"));
String token = UUID.randomUUID().toString();
user.setResetToken(token);
user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));
userRepository.save(user);
String resetLink = "http://localhost:3000/reset-password?token=" + token;
emailService.sendEmail( user.getEmail(), "🔐 Reset Your Password",
PasswordEmailTemplateUtil.resetPassword(user.getUsername(), resetLink));
}
public void resetPassword(String token, String newPassword) {
User user = userRepository.findByResetToken(token)
.orElseThrow(() -> new RuntimeException("Invalid token"));
if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
throw new RuntimeException("Token expired");
}
user.setPassword(passwordEncoder.encode(newPassword));
user.setResetToken(null);
user.setResetTokenExpiry(null);
userRepository.save(user);
}
}