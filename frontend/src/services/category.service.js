import api from "./api";

export const categoryService = {
  async getCategories() {
    const response = await api.get("/categories");
    return response.data;
  },

  async createCategory(categoryData) {
    const response = await api.post("/categories", categoryData);
    return response.data;
  },

  async deleteCategory(id) {
    const response = await api.delete(`/categories/${id}`);
    return response.data;
  },
};
