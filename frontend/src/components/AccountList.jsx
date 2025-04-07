import { useState } from "react";
import { useAccounts } from "../contexts/AccountContext";
import AccountItem from "./AccountItem";
import EditAccountModal from "../modals/EditAccountModal";

function AccountList({ variant = "full", itemLimit }) {
  const { accounts, updateAccount, deleteAccount } = useAccounts();
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedAccount, setSelectedAccount] = useState(null);
  const [error, setError] = useState("");

  const handleEdit = (account) => {
    setSelectedAccount(account);
    setShowEditModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this account?")) {
      try {
        await deleteAccount(id);
      } catch (error) {
        setError(error.message);
      }
    }
  };

  if (variant === "dashboard") {
    const displayedAccounts = itemLimit
      ? accounts.slice(0, itemLimit)
      : accounts;

    return (
      <div className="p-0">
        {displayedAccounts.map((account, index) => (
          <>
            <AccountItem
              key={account.id}
              account={account}
              variant="dashboard"
            />
            {index < displayedAccounts.length - 1 && (
              <div className="d-flex justify-content-center">
                <hr className="my-0 opacity-25 w-90" style={{ width: "99%" }} />
              </div>
            )}
          </>
        ))}
      </div>
    );
  }

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-8">
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
          {accounts.map((account) => (
            <AccountItem
              key={account.id}
              account={account}
              onEdit={() => handleEdit(account)}
              onDelete={() => handleDelete(account.id)}
            />
          ))}
          <EditAccountModal
            show={showEditModal}
            onHide={() => setShowEditModal(false)}
            onEdit={updateAccount}
            account={selectedAccount}
          />
        </div>
      </div>
    </div>
  );
}

export default AccountList;
