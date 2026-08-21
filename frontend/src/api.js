const API = '/api/v1';

export async function api(path, options = {}) {
  const headers = { ...(options.body ? { 'Content-Type': 'application/json' } : {}), ...(options.headers || {}) };
  const token = localStorage.getItem('carpinaon_token');
  if (token) headers.Authorization = `Bearer ${token}`;
  const response = await fetch(`${API}${path}`, { ...options, headers });
  const text = await response.text();
  let data = null;
  try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!response.ok) throw new Error(data?.erro || data?.message || `Erro ${response.status}`);
  return data;
}

export const endpoints = {
  login: (body) => api('/auth/login', { method: 'POST', body: JSON.stringify(body) }),
  register: (body) => api('/auth/cadastrar', { method: 'POST', body: JSON.stringify(body) }),
  categories: () => api('/categorias'),
  services: () => api('/servicos'),
  requests: () => api('/solicitacoes'),
  createRequest: (body) => api('/solicitacoes', { method: 'POST', body: JSON.stringify(body) }),
  requestDetail: (protocol) => api(`/solicitacoes/${encodeURIComponent(protocol)}`),
  notifications: () => api('/notificacoes'),
  notificationCount: () => api('/notificacoes/nao-lidas'),
  markRead: (id) => api(`/notificacoes/${id}/lida`, { method: 'PATCH' }),
  events: () => api('/eventos'),
};
