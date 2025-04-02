import { Card, Dropdown } from "react-bootstrap";
import { Pencil, Trash } from "react-bootstrap-icons";

function AccountItem({ account, onEdit, onDelete, variant = "full" }) {
  const formattedBalance = account.balance.toLocaleString("en-US", {
    style: "currency",
    currency: "CAD",
  });

  if (variant === "dashboard") {
    return (
      <Card className="mb-2">
        <Card.Body className="py-2">
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h6 className="mb-0 small">{account.accountName}</h6>
            </div>
            <span className="text-primary small">{formattedBalance}</span>
          </div>
        </Card.Body>
      </Card>
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
