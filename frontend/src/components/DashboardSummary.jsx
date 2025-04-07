import { useAccounts } from "../contexts/AccountContext";
import { Card } from "react-bootstrap";

function DashboardSummary() {
  const { accounts } = useAccounts();
  const totalBalance = accounts.reduce(
    (sum, account) => sum + account.balance,
    0
  );

  return (
    <Card className="mb-4">
      <Card.Body>
        <div
          style={{ height: "300px" }}
          className="d-flex flex-column justify-content-center align-items-center"
        >
          <h3 className="text-muted mb-4">Total Balance</h3>
          <div
            className={`display-2 ${
              totalBalance < 0 ? "text-danger" : "text-success"
            }`}
          >
            ${Math.abs(totalBalance).toFixed(2)}
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default DashboardSummary;
