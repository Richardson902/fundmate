import { useState } from "react";
import { Plus } from "react-bootstrap-icons";
import AddBudgetModal from "../modals/AddBudgetModal";
import { useBudgets } from "../contexts/BudgetContext";
import BudgetList from "../components/BudgetList";

function Budgets() {
  const [showAddModal, setShowAddModal] = useState(false);
  const { addBudget } = useBudgets();
  const [error, setError] = useState("");

  const handleAddBudget = async (budgetData) => {
    try {
      await addBudget(budgetData);
      setShowAddModal(false);
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div className="container-fluid">
      <BudgetList />
      <div
        className="position-fixed"
        style={{
          bottom: "2rem",
          right: "2rem",
          zIndex: 1030,
        }}
      >
        <button
          className="btn btn-primary rounded-circle d-flex align-items-center justify-content-center"
          onClick={() => setShowAddModal(true)}
          style={{
            width: "56px",
            height: "56px",
            boxShadow: "0 2px 5px rgba(0,0,0,0.2)",
          }}
        >
          <Plus size={24} />
        </button>
      </div>

      <AddBudgetModal
        show={showAddModal}
        onHide={() => setShowAddModal(false)}
        onAdd={handleAddBudget}
      />
    </div>
  );
}

export default Budgets;
