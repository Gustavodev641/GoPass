// ============================================
//  GOPASS — SHARED JS (INTEGRADO COM BACK-END)
// ============================================

function getUser() {
  try { return JSON.parse(localStorage.getItem('gopass_user')); } catch { return null; }
}
function saveUser(user) {
  localStorage.setItem('gopass_user', JSON.stringify(user));
}
function logout() {
  localStorage.removeItem('gopass_user');
  window.location.href = 'login.html';
}

// ---- Navbar render ----
function renderNavbar(activePage) {
  const user = getUser();
  const nav = document.getElementById('navbar');
  if (!nav) return;

  nav.innerHTML = `
    <div class="container nav-inner">
      <a href="dashboard.html" class="nav-logo">
        <div class="logo-gem">◈</div>
        <span class="logo-text">GoPass</span>
      </a>
      <div class="nav-links">
        <a href="dashboard.html" class="nav-link ${activePage === 'dashboard' ? 'active' : ''}">Eventos</a>
        ${user?.role === 'ADMIN' ? `
          <a href="admin-dashboard.html" class="nav-link nav-link-admin ${activePage === 'admin-dashboard' ? 'active' : ''}">
            <span class="admin-dot"></span>Dashboard
          </a>
          <a href="admin.html" class="nav-link nav-link-admin ${activePage === 'admin' ? 'active' : ''}">
            Eventos
          </a>
          <a href="clientes.html" class="nav-link nav-link-admin ${activePage === 'clientes' ? 'active' : ''}">
            Clientes
          </a>` : ''}
      </div>
      <div class="nav-actions">
        ${user ? `
          <div class="user-chip">
            <div class="user-avatar">${user.name[0].toUpperCase()}</div>
            <span class="user-name hide-mobile">${user.name}</span>
          </div>
          <button class="btn btn-ghost btn-sm" onclick="logout()">Sair</button>
        ` : `
          <a href="login.html" class="btn btn-primary btn-sm">Entrar</a>
        `}
      </div>
    </div>
  `;
}

// ---- Toast ----
let toastContainer;
function getToastContainer() {
  if (!toastContainer) {
    toastContainer = document.createElement('div');
    toastContainer.id = 'toast-container';
    document.body.appendChild(toastContainer);
  }
  return toastContainer;
}
function showToast(message, type = 'success', duration = 3500) {
  const c = getToastContainer();
  const t = document.createElement('div');
  t.className = `toast toast-${type}`;
  t.innerHTML = `<span class="toast-icon">${type === 'success' ? '✓' : '✕'}</span>${message}`;
  t.onclick = () => t.remove();
  c.appendChild(t);
  setTimeout(() => { t.style.opacity = '0'; t.style.transition = 'opacity 0.3s'; setTimeout(() => t.remove(), 300); }, duration);
}

// ---- API CONFIG ----
const API_BASE = '/api';  // APONTANDO PARA EC2
const API_URL  = `${API_BASE}/event`;

async function fetchEvents(page = 0, size = 9) {
  const res = await fetch(`${API_URL}?page=${page}&size=${size}`);
  if (!res.ok) throw new Error('Erro ao buscar eventos');
  return res.json();
}

async function createEvent(formData) {
  const res = await fetch(API_URL, {
    method: 'POST',
    body: formData
  });
  if (!res.ok) throw new Error('Erro ao criar evento no servidor');
  return res.json();
}

async function addCoupon(eventId, couponData) {
  const res = await fetch(`${API_BASE}/coupon/event/${eventId}`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(couponData)
  });
  if (!res.ok) throw new Error('Erro ao adicionar cupom');
  return res.json();
}

// ---- Helpers ----
function formatDate(dateStr) {
  if (!dateStr) return 'Data não informada';
  const d = new Date(dateStr);
  return d.toLocaleDateString('pt-BR', { day: '2-digit', month: 'short', year: 'numeric' });
}

function debounce(fn, delay = 300) {
  let t;
  return (...args) => { clearTimeout(t); t = setTimeout(() => fn(...args), delay); };
}

// ---- Mock Customers (para Clientes.html) ----
const MOCK_CUSTOMERS = [
  { id: '1', name: 'Ana Silva',       email: 'ana.silva@email.com',   city: 'São Paulo',       state: 'SP', plan: 'Premium',  joined: '2024-03-12', tickets: 8,  spent: 420.00,  status: 'ativo'   },
  { id: '2', name: 'Carlos Mendes',   email: 'carlos.m@gmail.com',    city: 'Rio de Janeiro',  state: 'RJ', plan: 'Gratuito', joined: '2024-05-20', tickets: 2,  spent: 0.00,    status: 'ativo'   },
  { id: '3', name: 'Beatriz Costa',   email: 'bea.costa@hotmail.com', city: 'Curitiba',        state: 'PR', plan: 'Premium',  joined: '2024-01-08', tickets: 15, spent: 780.00,  status: 'ativo'   },
  { id: '4', name: 'Diego Fernandes', email: 'diego.f@empresa.com',   city: 'Belo Horizonte',  state: 'MG', plan: 'Business', joined: '2023-11-30', tickets: 34, spent: 2200.00, status: 'ativo'   },
  { id: '5', name: 'Fernanda Lima',   email: 'fernanda.l@gmail.com',  city: 'Porto Alegre',    state: 'RS', plan: 'Gratuito', joined: '2024-07-15', tickets: 1,  spent: 0.00,    status: 'inativo' },
  { id: '6', name: 'Gabriel Rocha',   email: 'gabriel.r@outlook.com', city: 'Fortaleza',       state: 'CE', plan: 'Premium',  joined: '2024-02-28', tickets: 6,  spent: 310.00,  status: 'ativo'   }
];

function getCustomers() { return MOCK_CUSTOMERS; }