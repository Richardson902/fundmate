import api from "./api";

export const budgetService = {
  async getBudgetsByAccount(accountId) {
    const response = await api.get(`/budgets/account/${accountId}`);
    return response.data;
  },

  async createBudget(budgetData) {
    const response = await api.post("/budgets", budgetData);
    return response.data;
  },

  async getBudgetById(id) {
    const response = await api.get(`/budgets/${id}`);
    return response.data;
  },

  async deleteBudget(id) {
    const response = await api.delete(`/budgets/${id}`);
    return response.data;
  },
};
