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
    // Registration errors
    if (error.config.url === "/auth/register") {
      return Promise.reject(
        new Error(error.response?.data?.message || "Registration failed")
      );
    }

    // Invalid credentials for login attempt
    if (
      error.response?.status === 403 &&
      error.config.url === "/auth/authenticate"
    ) {
      return Promise.reject(new Error("Invalid email or password"));
    }

    // Auth errors handle 401 and 403 except for login/register
    if (error.response?.status === 401 || error.response?.status === 403) {
      localStorage.removeItem("token");
      window.location.href = "/login";
      return Promise.reject(new Error("Please login again"));
    }

    // Email exists conflict
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
