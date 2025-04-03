import { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";

function AddTransactionModal({ show, onHide, onAdd }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();
  const [amount, setAmount] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [accountId, setAccountId] = useState("");
  const [date, setDate] = useState(new Date().toISOString().split("T")[0]);
  const [fromName, setFromName] = useState("");
  const [note, setNote] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!amount || !categoryId || !accountId) {
      setError("Amount, category, and account are required");
      return;
    }

    try {
      await onAdd({
        amount: parseFloat(amount),
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
    setDate(new Date().toISOString().split("T")[0]);
    setFromName("");
    setNote("");
    setError("");
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Add Transaction</Modal.Title>
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
              placeholder="Enter source"
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Note</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              value={note}
              onChange={(e) => setNote(e.target.value)}
              placeholder="Add a note (optional)"
            />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" type="submit">
            Add Transaction
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default AddTransactionModal;
