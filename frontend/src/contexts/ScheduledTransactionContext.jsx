import { createContext, useContext, useState, useEffect } from "react";
import { scheduledTransactionService } from "../services/schedule.service";
import { useAccounts } from "./AccountContext";

const ScheduledTransactionContext = createContext();

export function ScheduledTransactionProvider({ children }) {
  const [scheduledTransactions, setScheduledTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const { accounts, loadAccounts } = useAccounts();
  const [error, setError] = useState("");

  const loadAllScheduledTransactions = async () => {
    try {
      setLoading(true);

      const allTransactions = [];

      for (const account of accounts) {
        const accountTransactions =
          await scheduledTransactionService.getScheduledTransactionByAccount(
            account.id
          );
        allTransactions.push(...accountTransactions);
      }

      allTransactions.sort((a, b) => new Date(b.date) - new Date(a.date));

      setScheduledTransactions(allTransactions);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const addScheduledTransaction = async (transactionData) => {
    try {
      setLoading(true);
      const newTransaction =
        await scheduledTransactionService.createScheduledTransaction(
          transactionData
        );
      await loadAccounts();
      setScheduledTransactions([...scheduledTransactions, newTransaction]);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const deleteScheduledTransaction = async (id) => {
    try {
      setLoading(true);
      await scheduledTransactionService.deleteScheduledTransaction(id);
      setScheduledTransactions(
        scheduledTransactions.filter((trans) => trans.id !== id)
      );
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (accounts.length > 0) {
      loadAllScheduledTransactions();
    }
  }, [accounts]);

  const value = {
    scheduledTransactions,
    loading,
    error,
    loadAllScheduledTransactions,
    addScheduledTransaction,
    deleteScheduledTransaction,
  };

  return (
    <ScheduledTransactionContext.Provider value={value}>
      {children}
    </ScheduledTransactionContext.Provider>
  );
}

export function useScheduledTransactions() {
  const context = useContext(ScheduledTransactionContext);
  if (!context) {
    throw new Error(
      "useScheduledTransactions must be used within a ScheduledTransactionProvider"
    );
  }
  return context;
}
