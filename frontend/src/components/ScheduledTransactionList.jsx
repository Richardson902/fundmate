import { useState, useEffect } from "react";
import { Card } from "react-bootstrap";
import { useScheduledTransactions } from "../contexts/ScheduledTransactionContext";
import ScheduledTransactionItem from "./ScheduledTransactionItem";

function ScheduledTransactionList({ variant = "full" }) {
  const {
    scheduledTransactions,
    loadAllScheduledTransactions,
    deleteScheduledTransaction,
  } = useScheduledTransactions();

  const [error, setError] = useState("");

  useEffect(() => {
    const loadTransactions = async () => {
      try {
        await loadAllScheduledTransactions();
      } catch (error) {
        setError(error.message);
      }
    };
    loadTransactions();
  }, []);

  const handleDelete = async (id) => {
    if (
      window.confirm(
        "Are you sure you want to delete this scheduled transaction?"
      )
    ) {
      try {
        await deleteScheduledTransaction(id);
      } catch (error) {
        setError(error.message);
      }
    }
  };

  if (variant === "dashboard") {
    const recentTransactions = transactions.slice(0, 5);
    return (
      <Card>
        <Card.Body className="p-0">
          {recentTransactions.map((transaction) => (
            <ScheduledTransactionItem
              key={transaction.id}
              transaction={transaction}
              variant="dashboard"
            />
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
              {scheduledTransactions.map((transaction, index) => (
                <div key={transaction.id}>
                  <ScheduledTransactionItem
                    transaction={transaction}
                    onDelete={() => handleDelete(transaction.id)}
                  />
                  {index < scheduledTransactions.length - 1 && (
                    <hr className="my-0" />
                  )}
                </div>
              ))}
            </Card.Body>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default ScheduledTransactionList;
