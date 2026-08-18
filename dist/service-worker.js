// ─── Mavuno Church – Service Worker v2 ───────────────────────────────────────
// Strategy:
//   • Navigation requests  → Network-first → fallback to cached /index.html
//   • JS / CSS / font bundles → Stale-While-Revalidate (cache-first on offline)
//   • Images / icons       → Cache-first (long-lived)
// ─────────────────────────────────────────────────────────────────────────────

const CACHE_VERSION = 'v2';
const SHELL_CACHE   = `mavuno-shell-${CACHE_VERSION}`;
const RUNTIME_CACHE = `mavuno-runtime-${CACHE_VERSION}`;

const PRECACHE_ASSETS = [
  '/',
  '/index.html',
  '/offline.html',
  '/manifest.json',
  '/favicon.ico',
  '/icon-192.png',
  '/icon-512.png',
  '/icon.svg',
];

// ── Install ──────────────────────────────────────────────────────────────────
self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(SHELL_CACHE)
      .then((cache) => {
        console.log('[SW] Pre-caching app shell');
        return cache.addAll(PRECACHE_ASSETS);
      })
      .then(() => self.skipWaiting())
  );
});

// ── Activate (prune old caches) ──────────────────────────────────────────────
self.addEventListener('activate', (event) => {
  const KEEP = [SHELL_CACHE, RUNTIME_CACHE];
  event.waitUntil(
    caches.keys()
      .then((names) => Promise.all(
        names
          .filter((n) => !KEEP.includes(n))
          .map((n) => {
            console.log('[SW] Deleting old cache:', n);
            return caches.delete(n);
          })
      ))
      .then(() => self.clients.claim())
  );
});

// ── Helpers ──────────────────────────────────────────────────────────────────
function isNavigation(request) {
  return request.mode === 'navigate';
}

function isStaticAsset(url) {
  return (
    url.pathname.startsWith('/static/') ||
    /\.(js|css|woff2?|ttf|otf)(\?.*)?$/.test(url.pathname)
  );
}

function isImage(url) {
  return /\.(png|jpe?g|gif|svg|ico|webp)(\?.*)?$/.test(url.pathname);
}

async function networkFirst(request, cacheName) {
  try {
    const response = await fetch(request);
    if (response && response.status === 200) {
      const clone = response.clone();
      caches.open(cacheName).then((c) => c.put(request, clone));
    }
    return response;
  } catch (_) {
    const cached = await caches.match(request);
    if (cached) return cached;
    // Final fallback for navigation: serve offline page
    if (isNavigation(request)) {
      return caches.match('/offline.html');
    }
    return new Response('', { status: 408, statusText: 'Offline' });
  }
}

async function staleWhileRevalidate(request, cacheName) {
  const cache = await caches.open(cacheName);
  const cached = await cache.match(request);

  const fetchPromise = fetch(request)
    .then((response) => {
      if (response && response.status === 200) {
        cache.put(request, response.clone());
      }
      return response;
    })
    .catch(() => cached);

  return cached || fetchPromise;
}

async function cacheFirst(request, cacheName) {
  const cached = await caches.match(request);
  if (cached) return cached;
  try {
    const response = await fetch(request);
    if (response && response.status === 200) {
      const cache = await caches.open(cacheName);
      cache.put(request, response.clone());
    }
    return response;
  } catch (_) {
    return new Response('', { status: 408, statusText: 'Offline' });
  }
}

// ── Fetch ────────────────────────────────────────────────────────────────────
self.addEventListener('fetch', (event) => {
  // Ignore non-GET and cross-origin requests
  if (event.request.method !== 'GET') return;
  if (!event.request.url.startsWith(self.location.origin)) return;

  const url = new URL(event.request.url);

  if (isNavigation(event.request)) {
    // Network-first for HTML navigation; offline page as last resort
    event.respondWith(networkFirst(event.request, SHELL_CACHE));
    return;
  }

  if (isImage(url)) {
    event.respondWith(cacheFirst(event.request, RUNTIME_CACHE));
    return;
  }

  if (isStaticAsset(url)) {
    event.respondWith(staleWhileRevalidate(event.request, RUNTIME_CACHE));
    return;
  }

  // Default: stale-while-revalidate for everything else from our origin
  event.respondWith(staleWhileRevalidate(event.request, RUNTIME_CACHE));
});
