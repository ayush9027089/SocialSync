// ===== GLOBAL FUNCTIONS =====

// TAB SWITCHING
function switchTab(tabName, evt) {
  document.querySelectorAll('.tab-btn').forEach(btn => {
    btn.classList.remove('tab-active');
  });

  if (evt) {
    let el = evt.target;
    while (el && !el.classList.contains('tab-btn')) el = el.parentElement;
    if (el) el.classList.add('tab-active');
  }

  const contentArea = document.getElementById('contentArea');
  if (contentArea) {
    contentArea.innerHTML = `
      <h3 class="text-xl font-semibold">No ${tabName} posts</h3>
      <p class="text-gray-500 mt-2">This section is currently empty.</p>
    `;
  }
}

// LOGOUT FUNCTION
function logout() {
  if (confirm('Are you sure you want to logout?')) {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    window.location.href = 'home.html';
  }
}

// INITIALIZE PAGE PROTECTION
window.addEventListener('load', () => {
  const token = localStorage.getItem('authToken');
  const currentPath = window.location.pathname;
  
  // Redirect to login if not authenticated and trying to access protected pages
  if (!token && !currentPath.includes('login') && !currentPath.includes('signup')) {
    window.location.href = 'login.html';
  }
  
  // Display user email in profile dropdown
  const userEmail = localStorage.getItem('userEmail');
  const userEmailElement = document.getElementById('userEmail');
  if (userEmailElement) {
    userEmailElement.textContent = userEmail || 'No email found';
  }

  // Set active nav link
  const page = window.location.pathname.split('/').pop();
  const navLinks = document.querySelectorAll('#navLinks a');
  navLinks.forEach(link => {
    const href = link.getAttribute('href');
    if (href === page) {
      link.classList.add('text-blue-600', 'bg-blue-50', 'font-semibold');
      link.classList.remove('text-gray-500', 'font-medium');
    } else {
      link.classList.remove('text-blue-600', 'bg-blue-50', 'font-semibold');
      link.classList.add('text-gray-500', 'font-medium');
    }
  });

  // Initialize profile dropdown
  initializeProfileDropdown();
});

// PROFILE DROPDOWN INITIALIZATION
function initializeProfileDropdown() {
  const profileBtn = document.getElementById('profileBtn');
  const profileMenu = document.getElementById('profileMenu');

  if (!profileBtn || !profileMenu) {
    console.warn('Profile dropdown elements not found');
    return;
  }

  let isOpen = false;

  // Toggle dropdown on button click
  const toggleDropdown = (e) => {
    e.stopPropagation();
    isOpen = !isOpen;
    profileMenu.classList.toggle('hidden', !isOpen);
    profileBtn.setAttribute('aria-expanded', isOpen);
  };

  // Close dropdown
  const closeDropdown = () => {
    isOpen = false;
    profileMenu.classList.add('hidden');
    profileBtn.setAttribute('aria-expanded', 'false');
  };

  // Button click handler
  profileBtn.addEventListener('click', toggleDropdown);

  // Keyboard navigation
  profileBtn.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      toggleDropdown(e);
    } else if (e.key === 'Escape' && isOpen) {
      closeDropdown();
    }
  });

  // Close on outside click
  document.addEventListener('click', (e) => {
    if (!profileBtn.contains(e.target) && !profileMenu.contains(e.target)) {
      closeDropdown();
    }
  });

  // Prevent menu clicks from closing
  profileMenu.addEventListener('click', (e) => {
    e.stopPropagation();
  });

  // Focus management
  profileMenu.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
      closeDropdown();
      profileBtn.focus();
    }
  });

  // Initialize ARIA attributes
  profileBtn.setAttribute('aria-haspopup', 'true');
  profileBtn.setAttribute('aria-expanded', 'false');
  profileMenu.setAttribute('role', 'menu');
}

// VIEW TOGGLE (for list/calendar views)
window.addEventListener('load', () => {
  const listBtn = document.getElementById('listViewBtn');
  const calBtn = document.getElementById('calendarViewBtn');
  const listContent = document.getElementById('listViewContent');
  const calContent = document.getElementById('calendarViewContent');

  if (listBtn && calBtn && listContent && calContent) {
    listBtn.onclick = () => {
      listBtn.classList.add('bg-white', 'text-blue-600', 'shadow');
      listBtn.classList.remove('text-gray-600');
      calBtn.classList.remove('bg-white', 'text-blue-600', 'shadow');
      calBtn.classList.add('text-gray-600');
      listContent.classList.remove('hidden');
      calContent.classList.add('hidden');
    };

    calBtn.onclick = () => {
      calBtn.classList.add('bg-white', 'text-blue-600', 'shadow');
      calBtn.classList.remove('text-gray-600');
      listBtn.classList.remove('bg-white', 'text-blue-600', 'shadow');
      listBtn.classList.add('text-gray-600');
      calContent.classList.remove('hidden');
      listContent.classList.add('hidden');
    };
  }
});

