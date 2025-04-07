import { useState, useEffect } from "react";
import { Card } from "react-bootstrap";
import { useBudgets } from "../contexts/BudgetContext";
import BudgetItem from "./BudgetItem";

function BudgetList({ variant = "full", accountId, itemLimit }) {
  const { budgets, loading, loadAllBudgets, deleteBudget } = useBudgets();

  const [error, setError] = useState("");

  useEffect(() => {
    const loadBudgets = async () => {
      try {
        await loadAllBudgets();
      } catch (error) {
        setError(error.message);
      }
    };
    loadBudgets();
  }, [accountId]);

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this budget?")) {
      try {
        await deleteBudget(id);
      } catch (error) {
        setError(error.message);
      }
    }
  };

  if (loading) return <div>Loading...</div>;

  if (variant === "dashboard") {
    const activeBudgets = budgets
      .filter((budget) => budget.completionPercentage < 100)
      .sort((a, b) => b.completionPercentage - a.completionPercentage)
      .slice(0, itemLimit || 3);

    return (
      <Card>
        <Card.Body className="p-0">
          {activeBudgets.map((budget) => (
            <BudgetItem key={budget.id} budget={budget} variant="dashboard" />
          ))}
        </Card.Body>
      </Card>
    );
  }

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <Card>
            <Card.Body className="p-0">
              {budgets.map((budget, index) => (
                <div key={budget.id}>
                  <BudgetItem
                    budget={budget}
                    onDelete={() => handleDelete(budget.id)}
                    variant={variant}
                  />
                  {index < budgets.length - 1 && <hr className="my-0" />}
                </div>
              ))}
            </Card.Body>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default BudgetList;
