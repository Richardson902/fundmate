import { useState } from "react";
import { Card } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { ThreeDotsVertical } from "react-bootstrap-icons";
import AccountList from "../components/AccountList";
import TransactionList from "../components/TransactionList";
import BudgetList from "../components/BudgetList";
import DashboardListSettingsModal from "../modals/DashboardListSettingsModal";
import MonthlyTransactionChart from "../components/MonthlyTransactionChart";
import DashboardSummary from "../components/DashboardSummary";

function Dashboard() {
  const navigate = useNavigate();
  const [showAccountSettings, setShowAccountSettings] = useState(false);
  const [showTransactionSettings, setShowTransactionSettings] = useState(false);
  const [showBudgetSettings, setShowBudgetSettings] = useState(false);
  const [accountsToShow, setAccountsToShow] = useState(3);
  const [transactionsToShow, setTransactionsToShow] = useState(5);
  const [budgetsToShow, setBudgetsToShow] = useState(3);

  const handleAccountSettingsSave = (value) => {
    setAccountsToShow(value);
    localStorage.setItem("accountsToShow", value);
  };

  const handleTransactionSettingsSave = (value) => {
    setTransactionsToShow(value);
    localStorage.setItem("transactionsToShow", value);
  };

  const handleBudgetSettingsSave = (value) => {
    setBudgetsToShow(value);
    localStorage.setItem("budgetsToShow", value);
  };

  return (
    <div className="container-fluid px-4">
      <div className="row mb-4">
        <div className="col-md-6">
          <DashboardSummary />
        </div>
        <div className="col-md-6">
          <MonthlyTransactionChart />
        </div>
      </div>
      <Card className="mb-4">
        <Card.Header className="bg-white border-bottom-0">
          <div className="d-flex justify-content-between align-items-center">
            <h5 className="mb-0">Accounts</h5>
            <button
              className="btn btn-link p-0 text-dark"
              onClick={() => setShowAccountSettings(true)}
            >
              <ThreeDotsVertical />
            </button>
          </div>
        </Card.Header>
        <div
          onClick={() => navigate("/accounts")}
          style={{ cursor: "pointer" }}
        >
          <AccountList variant="dashboard" itemLimit={accountsToShow} />
        </div>
      </Card>

      <Card className="mb-4">
        <Card.Header className="bg-white border-bottom-0">
          <div className="d-flex justify-content-between align-items-center">
            <h5 className="mb-0">Transactions</h5>
            <button
              className="btn btn-link p-0 text-dark"
              onClick={() => setShowTransactionSettings(true)}
            >
              <ThreeDotsVertical />
            </button>
          </div>
        </Card.Header>
        <div
          onClick={() => navigate("/transactions")}
          style={{ cursor: "pointer" }}
        >
          <TransactionList variant="dashboard" itemLimit={transactionsToShow} />
        </div>
      </Card>

      <div className="card mb-4">
        <div className="card-header bg-white">
          <div className="d-flex justify-content-between align-items-center">
            <h5 className="mb-0">Active Budgets</h5>
            <button
              className="btn btn-link p-0 text-dark"
              onClick={() => setShowBudgetSettings(true)}
            >
              <ThreeDotsVertical />
            </button>
          </div>
        </div>
        <div onClick={() => navigate("/budgets")} style={{ cursor: "pointer" }}>
          <BudgetList variant="dashboard" itemLimit={budgetsToShow} />
        </div>
      </div>

      <DashboardListSettingsModal
        show={showAccountSettings}
        onHide={() => setShowAccountSettings(false)}
        onSave={handleAccountSettingsSave}
        currentValue={accountsToShow}
        listName="Accounts"
      />

      <DashboardListSettingsModal
        show={showTransactionSettings}
        onHide={() => setShowTransactionSettings(false)}
        onSave={handleTransactionSettingsSave}
        currentValue={transactionsToShow}
        listName="Transactions"
      />

      <DashboardListSettingsModal
        show={showBudgetSettings}
        onHide={() => setShowBudgetSettings(false)}
        onSave={handleBudgetSettingsSave}
        currentValue={budgetsToShow}
        listName="Budgets"
      />
    </div>
  );
}

export default Dashboard;
