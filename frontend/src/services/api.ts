import axios from 'axios';

const formatBaseUrl = (url?: string): string => {
  let cleanUrl = (url || 'http://localhost:8080/api').trim();

  // Garante o protocolo https:// se não tiver especificado
  if (!cleanUrl.startsWith('http://') && !cleanUrl.startsWith('https://')) {
    cleanUrl = `https://${cleanUrl}`;
  }

  // Remove barras no final
  cleanUrl = cleanUrl.replace(/\/+$/, '');

  // Garante que termina com /api
  if (!cleanUrl.endsWith('/api')) {
    cleanUrl = `${cleanUrl}/api`;
  }

  return cleanUrl;
};

export const api = axios.create({
  baseURL: formatBaseUrl(import.meta.env.VITE_API_URL),
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;
