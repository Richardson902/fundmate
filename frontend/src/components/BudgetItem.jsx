import { Trash } from "react-bootstrap-icons";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";
import { ProgressBar } from "react-bootstrap";

function BudgetItem({ budget, onDelete, variant = "full" }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();

  const category = categories.find((c) => c.id === budget.category.id);
  const account = accounts.find((a) => a.id === budget.account.id);

  // Convert positive amounts into negative
  const spentAmount = -budget.spentAmount;
  const completionPercentage = (spentAmount / budget.amount) * 100;
  const residualAmount = budget.amount - spentAmount;

  const progressVariant = () => {
    if (completionPercentage >= 100) return "danger";
    if (completionPercentage >= 75) return "warning";
    return "success";
  };

  if (variant === "dashboard") {
    return (
      <div className="p-3">
        <div className="d-flex justify-content-between align-items-center mb-2">
          <div className="d-flex align-items-center">
            {category && (
              <img
                src={`/icons/${category.icon}.png`}
                alt={category.categoryName}
                style={{ width: "24px", height: "24px" }}
                className="me-2"
              />
            )}
            <div>{category?.categoryName}</div>
          </div>
          <div className="text-end">
            <div>
              ${spentAmount.toFixed(2)} / ${budget.amount.toFixed(2)}
            </div>
          </div>
        </div>
        <ProgressBar
          variant={progressVariant()}
          now={completionPercentage}
          label={`${completionPercentage.toFixed(0)}%`}
        />
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
            <div className="fw-bold">{category?.categoryName}</div>
            <div className="small text-muted">{account?.accountName}</div>
            <div className="small text-muted">
              {budget.duration.toLowerCase().replace("_", " ")} Budget
            </div>
          </div>
        </div>
        <div className="text-end">
          <div>
            <span className="text-muted">Spent: </span>
            <span
              className={
                budget.spentAmount > budget.amount ? "text-danger" : ""
              }
            >
              ${spentAmount.toFixed(2)}
            </span>
            <span className="text-muted"> of ${budget.amount.toFixed(2)}</span>
          </div>
          <div className="small text-muted">
            Remaining: ${residualAmount.toFixed(2)}
          </div>
          <div className="mt-2">
            <ProgressBar
              variant={progressVariant()}
              now={completionPercentage}
              label={`${completionPercentage.toFixed(0)}%`}
            />
          </div>
          <div className="d-flex justify-content-end mt-2">
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

export default BudgetItem;
