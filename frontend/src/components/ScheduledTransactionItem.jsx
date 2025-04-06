import { Trash } from "react-bootstrap-icons";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";

function ScheduledTransactionItem({ transaction, onDelete, variant = "full" }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();

  const category = categories.find((c) => c.id === transaction.category.id);
  const account = accounts.find((a) => a.id === transaction.account.id);
  const formattedDate = new Date(
    transaction.executionDate + "T00:00:00"
  ).toLocaleDateString();

  const getRecurrenceText = () => {
    return `Every ${
      transaction.recurrenceInterval
    } ${transaction.recurrenceType.toLowerCase()}(s)`;
  };

  if (variant === "dashboard") {
    return (
      <div className="p-3">
        <div className="d-flex justify-content-between align-items-center">
          <div className="d-flex align-items-center">
            {category && (
              <img
                src={`/icons/${category.icon}.png`}
                alt={category.categoryName}
                style={{ width: "24px", height: "24px" }}
                className="me-2"
              />
            )}
            <div>
              <div className="small text-muted">{formattedDate}</div>
              <div>{transaction.fromName || category?.categoryName}</div>
            </div>
          </div>
          <span
            className={transaction.amount < 0 ? "text-danger" : "text-success"}
          >
            ${Math.abs(transaction.amount).toFixed(2)}
          </span>
        </div>
      </div>
    );
  }

  return (
    <div className="p-3">
      <div className="d-flex justify-content-between align-items-start">
        <div className="d-flex align-items-center">
          {category && (
            <img
              src={`/icons/${category.icon}.png`}
              alt={category.categoryName}
              style={{ width: "48px", height: "48px" }}
              className="me-2"
            />
          )}
          <div>
            <div>{transaction.fromName || category?.categoryName}</div>
            <div className="small text-muted">{account?.accountName}</div>
            <div className="small text-muted">{getRecurrenceText()}</div>
            {transaction.note && (
              <div className="small text-muted">{transaction.note}</div>
            )}
          </div>
        </div>
        <div className="text-end">
          <div
            className={transaction.amount < 0 ? "text-danger" : "text-success"}
          >
            ${Math.abs(transaction.amount).toFixed(2)}
          </div>
          <div className="small text-muted">Next: {formattedDate}</div>
          <div className="small text-muted">
            {transaction.occurrences} occurrence(s)
          </div>
          <div className="d-flex gap-2 mt-2">
            {onDelete && (
              <button
                onClick={onDelete}
                className="btn btn-link p-0 text-danger"
              >
                <Trash size={16} />
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ScheduledTransactionItem;
