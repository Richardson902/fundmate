import { createContext, useContext, useState, useEffect } from "react";
import { accountService } from "../services/account.service";
import { useLoading } from "./LoadingContext";

const AccountContext = createContext();

export function AccountProvider({ children }) {
  const [accounts, setAccounts] = useState([]);
  const [error, setError] = useState("");
  const { setLoading } = useLoading();
  const loadAccounts = async () => {
    try {
      setLoading(true);
      const data = await accountService.getAccounts();
      setAccounts(data);
      setError("");
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const addAccount = async (accountData) => {
    try {
      setLoading(true);
      const newAccount = await accountService.createAccount(accountData);
      setAccounts([...accounts, newAccount]);
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const updateAccount = async (id, accountData) => {
    try {
      setLoading(true);
      const updatedAccount = await accountService.updateAccount(
        id,
        accountData
      );
      setAccounts(
        accounts.map((acc) => (acc.id === id ? updatedAccount : acc))
      );
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  const deleteAccount = async (id) => {
    try {
      setLoading(true);
      await accountService.deleteAccount(id);
      setAccounts(accounts.filter((acc) => acc.id !== id));
    } catch (error) {
      setError(error.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAccounts();
  }, []);

  return (
    <AccountContext.Provider
      value={{
        accounts,
        error,
        loadAccounts,
        addAccount,
        updateAccount,
        deleteAccount,
      }}
    >
      {children}
    </AccountContext.Provider>
  );
}

export function useAccounts() {
  const context = useContext(AccountContext);
  if (!context) {
    throw new Error("useAccounts must be used within an AccountProvider");
  }
  return context;
}
