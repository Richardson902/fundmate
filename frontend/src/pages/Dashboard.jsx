import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ThreeDotsVertical } from "react-bootstrap-icons";
import AccountList from "../components/AccountList";
import DashboardListSettingsModal from "../modals/DashboardListSettingsModal";

function Dashboard() {
  const navigate = useNavigate();
  const [showAccountSettings, setShowAccountSettings] = useState(false);
  const [accountsToShow, setAccountsToShow] = useState(3);

  const handleAccountSettingsSave = (value) => {
    setAccountsToShow(value);
    localStorage.setItem("accountsToShow", value);
  };

  return (
    <div className="container-fluid">
      <div className="card mb-4">
        <div className="card-header bg-white">
          <div className="d-flex justify-content-between align-items-center">
            <h5 className="mb-0">Accounts</h5>
            <button
              className="btn btn-link p-0 text-dark"
              onClick={() => setShowAccountSettings(true)}
            >
              <ThreeDotsVertical />
            </button>
          </div>
        </div>
        <div
          onClick={() => navigate("/accounts")}
          style={{ cursor: "pointer" }}
        >
          <AccountList variant="dashboard" itemLimit={accountsToShow} />
        </div>
      </div>

      <DashboardListSettingsModal
        show={showAccountSettings}
        onHide={() => setShowAccountSettings(false)}
        onSave={handleAccountSettingsSave}
        currentValue={accountsToShow}
        listName="Accounts"
      />
    </div>
  );
}

export default Dashboard;
