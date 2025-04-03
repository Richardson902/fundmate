import { createContext, useContext, useState, useEffect } from "react";
import { categoryService } from "../services/category.service";

const CategoryContext = createContext();

export function CategoryProvider({ children }) {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadCategories = async () => {
    try {
      setLoading(true);
      const data = await categoryService.getCategories();
      setCategories(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const addCategory = async (categoryData) => {
    try {
      setLoading(true);
      const newCategory = await categoryService.createCategory(categoryData);
      setCategories([...categories, newCategory]);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const deleteCategory = async (id) => {
    try {
      setLoading(true);
      await categoryService.deleteCategory(id);
      setCategories(categories.filter((cat) => cat.id !== id));
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadCategories();
  }, []);

  const value = {
    categories,
    loading,
    error,
    addCategory,
    deleteCategory,
  };

  return (
    <CategoryContext.Provider value={value}>
      {children}
    </CategoryContext.Provider>
  );
}

export function useCategories() {
  const context = useContext(CategoryContext);
  if (!context) {
    throw new Error("useCategories must be used within a CategoryProvider");
  }
  return context;
}
