// src/lib/api.js
import axios, { AxiosError } from 'axios';
import { jwtDecode } from 'jwt-decode';
import { toast } from 'svelte-sonner';
import { writable } from 'svelte/store';
import type { Organization, Person, Product, ProductEdit } from './types';
import ReconnectingWebSocket from 'reconnecting-websocket';

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

api.interceptors.request.use(function (config) {
  const token = getToken();
  if (token) {
    config.headers.Authorization = 'Bearer ' + token;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    toast.error(String(error.response?.data ?? error.message));
    console.log(error);
    return Promise.reject(error);
  }
);

export default api;

export const products = writable<Product[]>([]);
export const createProduct = async (product: ProductEdit) => {
  const { data } = await api.post('/products', product);
  console.log(data);
};
export const updateProduct = async (product: ProductEdit) => {
  const { data } = await api.put(`/products/${product.id}`, product);
  console.log(data);
};
export const updateProducts = async () => {
  const { data } = await api.get('/products');
  products.set(data);
  console.log(data);
};
updateProducts();

export const organizations = writable<Organization[]>([]);
export const createOrganization = async (organization: Organization) => {
  const { data } = await api.post('/organizations', organization);
  console.log(data);
};
export const updateOrganizations = async () => {
  organizations.set((await api.get('/organizations')).data);
};
updateOrganizations();

export const persons = writable<Person[]>([]);
export const createPerson = async (person: Person) => {
  const { data } = await api.post('/persons', person);
  console.log(data);
};
export const updatePersons = async () => {
  persons.set((await api.get('/persons')).data);
};
updatePersons();

let socket: ReconnectingWebSocket | null = null;

export const connectToCommands = () => {
  if (socket) {
    socket.close(); // Close the current socket if it's open
  }

  // const params = `type=control`;
  socket = new ReconnectingWebSocket(`ws://localhost:8080/lab1-1.0-SNAPSHOT/updates`);

  socket.onmessage = async function (event) {
    console.log('Got control command', event.data);
    setTimeout(async () => {
      await Promise.all([updatePersons(), updateProducts(), updateOrganizations()]);
    }, 100);
  };

  socket.onopen = function () {
    console.log('WebSocket connection established');
  };

  socket.onerror = function (event) {
    console.error('WebSocket error observed:', event);
  };

  socket.onclose = function () {
    console.log('WebSocket connection closed');
  };
};
connectToCommands();
// export const sendCommand = (command: any) => {
//   if (!socket) throw new Error('no socket!');
//   socket.send(JSON.stringify(command));
// };

// /updates

// Reconnect
