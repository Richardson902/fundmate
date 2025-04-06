import api from "./api";

export const scheduledTransactionService = {
  async getScheduledTransactionByAccount(accountId) {
    const response = await api.get(
      `/scheduled-transactions/account/${accountId}`
    );
    return response.data;
  },

  async createScheduledTransaction(transactionData) {
    const response = await api.post("/scheduled-transactions", transactionData);
    return response.data;
  },

  async updateScheduledTransaction(id, transactionData) {
    const response = await api.put(
      `/scheduled-transactions/${id}`,
      transactionData
    );
    return response.data;
  },

  async deleteScheduledTransaction(id) {
    const response = await api.delete(`/scheduled-transactions/${id}`);
    return response.data;
  },
};
