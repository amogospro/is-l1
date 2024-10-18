// src/lib/api.js
import axios, { AxiosError } from 'axios';
import { jwtDecode } from 'jwt-decode';
import { toast } from 'svelte-sonner';
import { writable } from 'svelte/store';

export const username = writable<string | null>(null);
export const role = writable('Guest');

function updateUserDetails() {
  const token = localStorage.getItem('jwt_token');
  if (token) {
    try {
      const decoded = jwtDecode(token) as unknown as { sub: string; role: string };
      console.log(decoded);
      username.set(decoded.sub as string); // Adjust based on token payload structure
      role.set(decoded.role as string); // Adjust based on token payload structure
    } catch (error) {
      console.error('Error decoding token:', error);
    }
  } else {
    username.set(null);
    role.set('Guest');
  }
}
updateUserDetails();

const api = axios.create({
  baseURL: 'http://localhost:8080/lab1-1.0-SNAPSHOT/api',
  headers: {
    'Content-Type': 'application/json'
  }
});

// Function to save token
export function setToken(token: string) {
  localStorage.setItem('jwt_token', token);
  updateUserDetails();
}

// Function to get token
export function getToken() {
  return localStorage.getItem('jwt_token');
}

export function clearToken() {
  localStorage.removeItem('jwt_token');
  updateUserDetails();
}

// Function to login
export async function login(credentials: { username: string; password: string }) {
  const response = await api.post('/auth/login', credentials);
  const { token } = response.data;
  setToken(token);
  return token;
}

// Function to register
export async function register(userData: {
  username: string;
  password: string;
  adminRequest: boolean;
}) {
  const response = await api.post('/auth/register', userData);
  return response.data;
}

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    toast.error(String(error.response?.data ?? error.message));
    console.log(error);
    return Promise.reject(error);
  }
);

export default api;
