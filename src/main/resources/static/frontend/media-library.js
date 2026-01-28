const mediaGrid = document.getElementById("mediaGrid");
const uploadInput = document.getElementById("mediaUpload");
const rightSidebar = document.getElementById("rightSidebar");
const rightSidebarOverlay = document.getElementById("rightSidebarOverlay");

let mediaItems = [];
let currentSelectedMedia = null;

// Upload handler
uploadInput.addEventListener("change", (e) => {
  const files = Array.from(e.target.files);

  files.forEach(file => {
    const url = URL.createObjectURL(file);
    const uploadDate = new Date().toLocaleString();
    const fileSize = formatFileSize(file.size);
    
    const mediaItem = {
      id: Math.random().toString(36).substring(2, 11),
      type: file.type.startsWith("image") ? "image" : "video",
      url,
      name: file.name,
      size: file.size,
      fileSize,
      uploadDate,
      file: file
    };

    mediaItems.push(mediaItem);
  });

  renderMedia("all");
  uploadInput.value = ""; // Reset input
});

function formatFileSize(bytes) {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return Math.round(bytes / Math.pow(k, i) * 100) / 100 + ' ' + sizes[i];
}

function renderMedia(filter) {
  mediaGrid.innerHTML = "";

  const filteredItems = mediaItems
    .filter(item => filter === "all" || item.type === filter);

  if (filteredItems.length === 0) {
    const emptyState = document.createElement('div');
    emptyState.className = 'col-span-full text-center py-24 text-slate-400';
    emptyState.innerHTML = `
      <svg xmlns="http://www.w3.org/2000/svg" class="h-16 w-16 mx-auto mb-4 text-slate-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="1.5" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
      </svg>
      <p class="text-lg font-semibold mb-1">No media uploaded yet</p>
      <p class="text-sm">Upload images or videos to get started</p>
    `;
    mediaGrid.appendChild(emptyState);
    return;
  }

  filteredItems.forEach((item) => {
    const card = document.createElement("div");
    card.className = "media-card group";

    card.innerHTML = `
      ${item.type === "image"
        ? `<img src="${item.url}" class="media-card-image" alt="${item.name}" />`
        : `<video src="${item.url}" class="media-card-image" muted></video>`
      }
      
      <div class="media-overlay">
        <div class="flex gap-3">
          <button class="overlay-btn" onclick="openMediaDetails('${item.id}')">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
            </svg>
          </button>
          <button class="overlay-btn delete-btn" onclick="deleteMedia('${item.id}', event)">
            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
            </svg>
          </button>
        </div>
      </div>

      <div class="media-card-content">
        <div class="media-card-type">
          ${item.type === "image" ? "🖼️ Image" : "🎬 Video"}
        </div>
        <p class="text-xs text-slate-600 mt-2 truncate" title="${item.name}">${item.name}</p>
      </div>
    `;

    card.addEventListener("click", () => openMediaDetails(item.id));
    mediaGrid.appendChild(card);
  });
}

function filterMedia(type, event) {
  document.querySelectorAll(".filter-btn").forEach(btn => btn.classList.remove("active"));
  event.target.classList.add("active");
  renderMedia(type);
}

function openMediaDetails(mediaId) {
  const media = mediaItems.find(m => m.id === mediaId);
  if (!media) return;

  currentSelectedMedia = media;

  // Set preview
  const previewContainer = document.getElementById("detailPreview");
  previewContainer.innerHTML = media.type === "image"
    ? `<img src="${media.url}" class="w-full h-full object-cover" />`
    : `<video src="${media.url}" class="w-full h-full object-cover" controls></video>`;

  // Set info
  document.getElementById("detailType").textContent = media.type === "image" ? "Image" : "Video";
  document.getElementById("detailSize").textContent = media.fileSize;
  document.getElementById("detailDate").textContent = media.uploadDate;
  document.getElementById("detailFileName").textContent = media.name;

  // Handle dimensions and duration
  if (media.type === "image") {
    document.getElementById("dimensionsBox").classList.remove("hidden");
    document.getElementById("durationBox").classList.add("hidden");

    const img = new Image();
    img.onload = () => {
      document.getElementById("detailDimensions").textContent = `${img.width} × ${img.height} px`;
    };
    img.src = media.url;
  } else {
    document.getElementById("durationBox").classList.remove("hidden");
    document.getElementById("dimensionsBox").classList.add("hidden");

    const video = document.createElement("video");
    video.onloadedmetadata = () => {
      const minutes = Math.floor(video.duration / 60);
      const seconds = Math.floor(video.duration % 60);
      document.getElementById("detailDuration").textContent = `${minutes}m ${seconds}s`;
    };
    video.src = media.url;
  }

  // Open sidebar
  rightSidebar.classList.add("open");
  rightSidebarOverlay.classList.add("open");
}
function closeMediaDetails() {
  rightSidebar.classList.remove("open");
  rightSidebarOverlay.classList.remove("open");
  currentSelectedMedia = null;
}

function deleteMedia(mediaId, event) {
  event.stopPropagation();
  
  if (!confirm("Are you sure you want to delete this media?")) return;

  mediaItems = mediaItems.filter(m => m.id !== mediaId);
  const activeFilter = document.querySelector(".filter-btn.active")?.textContent?.toLowerCase() || "all";
  
  let filterType = "all";
  if (activeFilter.includes("image")) filterType = "image";
  if (activeFilter.includes("video")) filterType = "video";
  
  renderMedia(filterType);
}

function deleteCurrentMedia() {
  if (!currentSelectedMedia) return;
  
  if (!confirm("Are you sure you want to delete this media?")) return;

  deleteMedia(currentSelectedMedia.id, { stopPropagation: () => {} });
  closeMediaDetails();
}

function downloadMedia() {
  if (!currentSelectedMedia) return;

  const link = document.createElement("a");
  link.href = currentSelectedMedia.url;
  link.download = currentSelectedMedia.name;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
}

// Close sidebar on escape key
document.addEventListener("keydown", (e) => {
  if (e.key === "Escape") {
    closeMediaDetails();
  }
});

// ===== LOGOUT & PROFILE SETUP =====
function logout() {
  if (confirm('Are you sure you want to logout?')) {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userEmail');
    localStorage.removeItem('userName');
    window.location.href = 'login.html';
  }
}