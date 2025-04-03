import { Pencil, Trash } from "react-bootstrap-icons";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";

function TransactionItem({ transaction, onEdit, onDelete, variant = "full" }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();

  console.log("Transaction:", transaction);
  console.log("Categories:", categories);
  console.log("Accounts:", accounts);

  const category = categories.find((c) => c.id === transaction.category.id);
  const account = accounts.find((a) => a.id === transaction.account.id);

  console.log("Found category:", category);
  console.log("Found account:", account);

  const formattedDate = new Date(transaction.date).toLocaleDateString();

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
          <div className="small text-muted">{formattedDate}</div>
          <div className="d-flex gap-2 mt-2">
            {onEdit && (
              <button
                onClick={onEdit}
                className="btn btn-link p-0 text-primary"
              >
                <Pencil size={16} />
              </button>
            )}
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

export default TransactionItem;
