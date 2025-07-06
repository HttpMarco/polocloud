import axios from 'axios';

const apiEndpoint = axios.create({
  // baseURL: import.meta.env.VITE_API_URL + import.meta.env.VITE_API_BASE_PATH,
  baseURL: import.meta.env.VITE_API_BASE_PATH,
  timeout: 15000,
  withCredentials: true,
});
export default apiEndpoint;
