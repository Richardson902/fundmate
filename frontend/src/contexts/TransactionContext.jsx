import { createContext, useContext, useState, useEffect } from "react";
import { transactionService } from "../services/transaction.service";
import { useAccounts } from "../contexts/AccountContext";

const TransactionContext = createContext();

export function TransactionProvider({ children }) {
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const { accounts, loadAccounts } = useAccounts();

  const loadAllTransactions = async () => {
    try {
      setLoading(true);

      // Gross fix due to not having a way to return all of a user's transactions. Oopsies lol
      const allTransactions = [];

      for (const account of accounts) {
        const accountTransactions =
          await transactionService.getTransactionsByAccount(account.id);
        allTransactions.push(...accountTransactions);
      }

      allTransactions.sort((a, b) => new Date(b.date) - new Date(a.date));

      setTransactions(allTransactions);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const loadTransactionsByAccount = async (accountId) => {
    try {
      setLoading(true);
      const data = await transactionService.getTransactionsByAccount(accountId);
      setTransactions(data);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const addTransaction = async (transactionData) => {
    try {
      setLoading(true);
      const newTransaction = await transactionService.createTransaction(
        transactionData
      );
      await loadAccounts();
      setTransactions([...transactions, newTransaction]);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const updateTransaction = async (id, transactionData) => {
    try {
      setLoading(true);
      const updatedTransaction = await transactionService.updateTransaction(
        id,
        transactionData
      );
      setTransactions(
        transactions.map((trans) =>
          trans.id === id ? updatedTransaction : trans
        )
      );
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const deleteTransaction = async (id) => {
    try {
      setLoading(true);
      await transactionService.deleteTransaction(id);
      setTransactions(transactions.filter((trans) => trans.id !== id));
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (accounts.length > 0) {
      loadAllTransactions();
    }
  }, [accounts]);

  const value = {
    transactions,
    loading,
    error,
    loadAllTransactions,
    loadTransactionsByAccount,
    addTransaction,
    updateTransaction,
    deleteTransaction,
  };

  return (
    <TransactionContext.Provider value={value}>
      {children}
    </TransactionContext.Provider>
  );
}

export function useTransactions() {
  const context = useContext(TransactionContext);
  if (!context) {
    throw new Error(
      "useTransactions must be used within a TransactionProvider"
    );
  }
  return context;
}
