import { useState } from "react";
import { useAccounts } from "../contexts/AccountContext";
import { Plus } from "react-bootstrap-icons";
import AccountList from "../components/AccountList";
import AccountSummary from "../components/AccountSummary";
import AddAccountModal from "../modals/AddAccountModal";

function Accounts() {
  const { accounts, addAccount, error } = useAccounts();
  const [showAddModal, setShowAddModal] = useState(false);

  const getTotalBalance = () => {
    return accounts.reduce((sum, account) => sum + account.balance, 0);
  };

  return (
    <div className="container-fluid">
      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      <AccountSummary totalBalance={getTotalBalance()} />
      <AccountList variant="full" />
      <button
        className="btn btn-primary rounded-circle d-flex align-items-center justify-content-center position-fixed"
        onClick={() => setShowAddModal(true)}
        style={{
          width: "56px",
          height: "56px",
          bottom: "2rem",
          right: "2rem",
          zIndex: 1030,
          boxShadow: "0 2px 5px rgba(0,0,0,0.2)",
        }}
      >
        <Plus size={24} />
      </button>

      <AddAccountModal
        show={showAddModal}
        onHide={() => setShowAddModal(false)}
        onAdd={addAccount}
      />
    </div>
  );
}

export default Accounts;
