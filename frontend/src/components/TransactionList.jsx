import { useState, useEffect } from "react";
import { Card } from "react-bootstrap";
import { useTransactions } from "../contexts/TransactionContext";
import TransactionItem from "./TransactionItem";
import EditTransactionModal from "../modals/EditTransactionModal";

function TransactionList({ variant = "full", accountId, itemLimit }) {
  const {
    transactions,
    loadAllTransactions,
    loadTransactionsByAccount,
    deleteTransaction,
    updateTransaction,
  } = useTransactions();

  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedTransaction, setSelectedTransaction] = useState(null);
  const [error, setError] = useState(""); // set errors later

  useEffect(() => {
    const loadTransactions = async () => {
      try {
        if (accountId) {
          await loadTransactionsByAccount(accountId);
        } else {
          await loadAllTransactions();
        }
      } catch (error) {
        setError(error.message);
      }
    };
    loadTransactions();
  }, [accountId]);

  const handleEdit = (transaction) => {
    setSelectedTransaction(transaction);
    setShowEditModal(true);
  };

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this transaction?")) {
      try {
        await deleteTransaction(id);
      } catch (error) {
        setError(error.message);
      }
    }
  };

  // Will fix later
  if (variant === "dashboard") {
    const recentTransactions = transactions.slice(0, itemLimit || 5);
    return (
      <div className="p-0">
        {recentTransactions.map((transaction, index) => (
          <div key={transaction.id}>
            <TransactionItem transaction={transaction} variant="dashboard" />
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <Card>
            <Card.Body className="p-0">
              {transactions.map((transaction, index) => (
                <div key={transaction.id}>
                  <TransactionItem
                    variant="full"
                    transaction={transaction}
                    onEdit={() => handleEdit(transaction)}
                    onDelete={() => handleDelete(transaction.id)}
                  />
                  {index < transactions.length - 1 && <hr className="my-0" />}
                </div>
              ))}
            </Card.Body>
          </Card>
        </div>
      </div>

      <EditTransactionModal
        show={showEditModal}
        onHide={() => {
          setShowEditModal(false);
        }}
        onEdit={updateTransaction}
        transaction={selectedTransaction}
      />
    </div>
  );
}

export default TransactionList;
