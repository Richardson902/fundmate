import api from "./api";

export const authService = {
  async login(email, password) {
    localStorage.removeItem("token");
    const response = await api.post("/auth/authenticate", { email, password });
    localStorage.setItem("token", response.data.token);
    return response.data;
  },

  async register(email, password) {
    localStorage.removeItem("token");
    return api.post("/auth/register", { email, password });
  },

  logout() {
    localStorage.removeItem("token");
    window.location.href = "/login";
  },
};
