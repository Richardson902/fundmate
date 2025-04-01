import axios from "axios";

const api = axios.create({
  baseURL: "/api",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Auth errors
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/login";
      return Promise.reject(new Error("Please login again"));
    }

    // Invalid credentials
    if (error.response?.status === 403) {
      return Promise.reject(new Error("Invalid email or password"));
    }

    // Email exists
    if (error.response?.status === 409) {
      return Promise.reject(new Error("Email already exists"));
    }

    // Other errors
    return Promise.reject(
      error.response?.data?.message || "Something went wrong"
    );
  }
);

export default api;
