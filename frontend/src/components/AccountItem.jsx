import { Card, Dropdown } from "react-bootstrap";
import { Pencil, Trash } from "react-bootstrap-icons";

function AccountItem({ account, onEdit, onDelete, variant = "full" }) {
  const formattedBalance = account.balance.toLocaleString("en-US", {
    style: "currency",
    currency: "CAD",
  });

  if (variant === "dashboard") {
    return (
      <div className="p-3">
        <div className="d-flex justify-content-between align-items-center">
          <div>
            <div className="fw-medium">{account.accountName}</div>
          </div>
          <span
            className={account.balance < 0 ? "text-danger" : "text-success"}
          >
            {formattedBalance}
          </span>
        </div>
      </div>
    );
  }

  return (
    <Card className="mb-3">
      <Card.Body>
        <div className="d-flex justify-content-between align-items-start">
          <div>
            <h6 className="mb-0 h5">{account.accountName}</h6>
          </div>
          <div className="text-end">
            <div className="mb-2">
              <span className="text-primary h4">{formattedBalance}</span>
            </div>
            <div className="d-flex gap-3 justify-content-end">
              <button
                onClick={() => onEdit(account)}
                className="btn btn-link p-0 text-primary"
              >
                <Pencil size={16} />
              </button>
              <button
                onClick={() => onDelete(account.id)}
                className="btn btn-link p-0 text-danger"
              >
                <Trash size={16} />
              </button>
            </div>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
}

export default AccountItem;
