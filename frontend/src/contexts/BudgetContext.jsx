import { createContext, useContext, useState, useEffect } from "react";
import { budgetService } from "../services/budget.service";
import { useAccounts } from "./AccountContext";

const BudgetContext = createContext();

export function BudgetProvider({ children }) {
  const [budgets, setBudgets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { accounts } = useAccounts();

  const loadAllBudgets = async () => {
    try {
      setLoading(true);
      const allBudgets = [];

      for (const account of accounts) {
        const accountBudgets = await budgetService.getBudgetsByAccount(
          account.id
        );
        allBudgets.push(...accountBudgets);
      }

      setBudgets(allBudgets);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const addBudget = async (budgetData) => {
    try {
      setLoading(true);
      const newBudget = await budgetService.createBudget(budgetData);
      setBudgets([...budgets, newBudget]);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(true);
    }
  };

  const deleteBudget = async (id) => {
    try {
      setLoading(true);
      await budgetService.deleteBudget(id);
      setBudgets(budgets.filter((budget) => budget.id !== id));
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (accounts.length > 0) {
      loadAllBudgets();
    }
  }, [accounts]);

  const value = {
    budgets,
    loading,
    error,
    loadAllBudgets,
    addBudget,
    deleteBudget,
  };

  return (
    <BudgetContext.Provider value={value}>{children}</BudgetContext.Provider>
  );
}

export function useBudgets() {
  const context = useContext(BudgetContext);
  if (!context) {
    throw new Error("useBudgets must be used within a BudgetProvider");
  }
  return context;
}
