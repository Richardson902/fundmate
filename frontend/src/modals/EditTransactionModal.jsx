import { useState, useEffect } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";
import { useNavigate } from "react-router-dom";

function EditTransactionModal({ show, onHide, transaction, onEdit }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();
  const [amount, setAmount] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [accountId, setAccountId] = useState("");
  const [date, setDate] = useState("");
  const [fromName, setFromName] = useState("");
  const [note, setNote] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (transaction) {
      setAmount(Math.abs(transaction.amount).toString());
      setCategoryId(transaction.categoryId);
      setAccountId(transaction.accountId);
      setDate(transaction.date);
      setFromName(transaction.fromName || "");
      setNote(transaction.note || "");
    }
  }, [transaction]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!amount || !categoryId || !accountId) {
      setError("Amount, category, and account are required");
      return;
    }

    try {
      // Check if transaction is expense or income to avoid sending over positive value for expense
      const isExpense = transaction.amount < 0;
      await onEdit(transaction.id, {
        amount: isExpense
          ? -Math.abs(parseFloat(amount))
          : Math.abs(parseFloat(amount)),
        categoryId,
        accountId,
        date,
        fromName: fromName.trim(),
        note: note.trim(),
      });
      handleClose();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleClose = () => {
    setAmount("");
    setCategoryId("");
    setAccountId("");
    setDate("");
    setFromName("");
    setNote("");
    setError("");
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Edit Transaction</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
          <Form.Group className="mb-3">
            <Form.Label>Amount</Form.Label>
            <Form.Control
              type="number"
              step="0.01"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Category</Form.Label>
            <Form.Select
              value={categoryId}
              onChange={(e) => setCategoryId(e.target.value)}
              required
            >
              <option value="">Select a category</option>
              {categories.map((category) => (
                <option key={category.id} value={category.id}>
                  {category.categoryName}
                </option>
              ))}
            </Form.Select>
            <div className="mt-1">
              <button
                className="btn btn-link p-0 small text-primary text-decoration-none"
                onClick={(e) => {
                  e.preventDefault();
                  onHide();
                  navigate("/categories");
                }}
              >
                Add new category
              </button>
            </div>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Account</Form.Label>
            <Form.Select
              value={accountId}
              onChange={(e) => setAccountId(e.target.value)}
              required
            >
              <option value="">Select an account</option>
              {accounts.map((account) => (
                <option key={account.id} value={account.id}>
                  {account.accountName}
                </option>
              ))}
            </Form.Select>
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Date</Form.Label>
            <Form.Control
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>From</Form.Label>
            <Form.Control
              type="text"
              value={fromName}
              onChange={(e) => setFromName(e.target.value)}
              placeholder="Enter source/destination"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Note</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={note}
              onChange={(e) => setNote(e.target.value)}
              placeholder="Enter any additional notes"
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" type="submit">
            Save Changes
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default EditTransactionModal;
