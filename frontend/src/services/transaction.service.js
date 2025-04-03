import api from "./api";

export const transactionService = {
  async getTransactionsByAccount(accountId) {
    const response = await api.get(`/transactions/account/${accountId}`);
    return response.data;
  },

  async getTransactionByDateRange(accountId, startDate, endDate) {
    const response = await api.get(
      `/transactions/accounts/${accountId}/date-range`,
      {
        params: { startDate, endDate },
      }
    );
    return response.data;
  },

  async createTransaction(transactionData) {
    const response = await api.post("/transactions", transactionData);
    return response.data;
  },

  async updateTransaction(id, transactionData) {
    const response = await api.put(`/transactions/${id}`, transactionData);
    return response.data;
  },

  async deleteTransaction(id) {
    const response = await api.delete(`/transactions/${id}`);
    return response.data;
  },
};
