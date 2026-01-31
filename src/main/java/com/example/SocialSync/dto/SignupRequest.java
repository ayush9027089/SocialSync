package social_media.social_media_handler.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private String admin;    
    private String secret;
}
