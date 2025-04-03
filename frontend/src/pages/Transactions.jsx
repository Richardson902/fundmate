import { useState } from "react";
import { Plus, Dash } from "react-bootstrap-icons";
import TransactionList from "../components/TransactionList";
import AddIncomeModal from "../modals/AddIncomeModal";
import AddExpenseModal from "../modals/AddExpenseModal";
import { useTransactions } from "../contexts/TransactionContext";

function Transactions() {
  const [showAddModal, setShowAddModal] = useState(false);
  const [showExpenseModal, setShowExpenseModal] = useState(false);
  const { addTransaction } = useTransactions();
  const [error, setError] = useState("");

  const handleAddTransaction = async (transactionData) => {
    try {
      await addTransaction(transactionData);
      setShowAddModal(true);
    } catch (error) {
      setError(error.message);
    }
  };

  const handleAddExpense = async (transactionData) => {
    try {
      await addTransaction(transactionData);
      setShowExpenseModal(true);
    } catch (error) {
      setError(error.message);
    }
  };

  return (
    <div className="container-fluid">
      <TransactionList variant="full" />

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
          onClick={() => setShowAddModal(true)}
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

      <AddIncomeModal
        show={showAddModal}
        onHide={() => setShowAddModal(false)}
        onAdd={handleAddTransaction}
      />

      <AddExpenseModal
        show={showExpenseModal}
        onHide={() => setShowExpenseModal(false)}
        onAdd={handleAddExpense}
      />
    </div>
  );
}

export default Transactions;
