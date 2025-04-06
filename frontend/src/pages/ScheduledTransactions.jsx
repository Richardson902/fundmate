import { useState } from "react";
import { Plus, Dash } from "react-bootstrap-icons";
import ScheduledTransactionList from "../components/ScheduledTransactionList";
import AddScheduledIncomeModal from "../modals/AddScheduledIncomeModal";
import AddScheduledExpenseModal from "../modals/AddScheduledExpenseModal";
import { useScheduledTransactions } from "../contexts/ScheduledTransactionContext";

function ScheduledTransactions() {
  const [showIncomeModal, setShowIncomeModal] = useState(false);
  const [showExpenseModal, setShowExpenseModal] = useState(false);
  const { addScheduledTransaction } = useScheduledTransactions();
  const [error, setError] = useState("");

  const handleAddScheduledIncome = async (transactionData) => {
    try {
      await addScheduledTransaction(transactionData);
      setShowAddModal(false);
    } catch (error) {
      setError(error.message);
    }
  };

  const handleAddScheduledExpense = async (transactionData) => {
    try {
      await addScheduledTransaction(transactionData);
      setShowExpenseModal(false);
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div className="container-fluid">
      <ScheduledTransactionList />

      <div
        className="position-fixed d-flex flex-column gap-2"
        style={{
          bottom: "2rem",
          right: "2rem",
          zIndex: 1030,
        }}
      >
        <button
          className="btn btn-primary rounded-circle d-flex align-items-center justify-content-center"
          onClick={() => setShowIncomeModal(true)}
          style={{
            width: "56px",
            height: "56px",
            boxShadow: "0 2px 5px rgba(0,0,0,0.2)",
          }}
        >
          <Plus size={24} />
        </button>
        <button
          className="btn btn-danger rounded-circle d-flex align-items-center justify-content-center"
          onClick={() => setShowExpenseModal(true)}
          style={{
            width: "56px",
            height: "56px",
            boxShadow: "0 2px 5px rgba(0,0,0,0.2)",
          }}
        >
          <Dash size={24} />
        </button>
      </div>

      <AddScheduledIncomeModal
        show={showIncomeModal}
        onHide={() => setShowIncomeModal(false)}
        onAdd={handleAddScheduledIncome}
      />

      <AddScheduledExpenseModal
        show={showExpenseModal}
        onHide={() => setShowExpenseModal(false)}
        onAdd={handleAddScheduledExpense}
      />
    </div>
  );
}

export default ScheduledTransactions;
