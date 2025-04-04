import React from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import ProtectedRoute from "./ProtectedRoute";
import Layout from "../components/Layout";

import Login from "../pages/Login";
import Register from "../pages/Register";
import Dashboard from "../pages/Dashboard";
import Accounts from "../pages/Accounts";
import Budgets from "../pages/Budgets";
import ScheduledTransactions from "../pages/ScheduledTransactions";
import Transactions from "../pages/Transactions";
import { AccountProvider } from "../contexts/AccountContext";
import { CategoryProvider } from "../contexts/CategoryContext";
import Categories from "../pages/Categories";

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/dashboard" replace />} />
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      <Route element={<ProtectedRoute />}>
        <Route
          element={
            <AccountProvider>
              <CategoryProvider>
                <Layout />
              </CategoryProvider>
            </AccountProvider>
          }
        >
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/transactions" element={<Transactions />} />
          <Route
            path="/scheduled-transactions"
            element={<ScheduledTransactions />}
          />
          <Route path="/accounts" element={<Accounts />} />
          <Route path="/budgets" element={<Budgets />} />
          <Route path="/categories" element={<Categories />} />
        </Route>
      </Route>
    </Routes>
  );
}

export default AppRoutes;
