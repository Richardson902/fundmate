import { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";
import { useNavigate } from "react-router-dom";

function AddBudgetModal({ show, onHide, onAdd }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();
  const [amount, setAmount] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [accountId, setAccountId] = useState("");
  const [duration, setDuration] = useState("ONE_MONTH");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!amount || !categoryId || !accountId) {
      setError("Amount, category, and account are required");
      return;
    }

    try {
      await onAdd({
        amount: Math.abs(parseFloat(amount)),
        categoryId,
        accountId,
        duration,
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
    setDuration("ONE_MONTH");
    setError("");
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Add Budget</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
          <div className="row mb-3">
            <div className="col">
              <Form.Group>
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
            </div>
            <div className="col">
              <Form.Group>
                <Form.Label>Amount</Form.Label>
                <Form.Control
                  type="number"
                  step="0.01"
                  min="0"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  required
                />
              </Form.Group>
            </div>
          </div>

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
            <Form.Label>Duration</Form.Label>
            <Form.Select
              value={duration}
              onChange={(e) => setDuration(e.target.value)}
              required
            >
              <option value="ONE_WEEK">1 Week</option>
              <option value="ONE_MONTH">1 Month</option>
              <option value="ONE_YEAR">1 Year</option>
            </Form.Select>
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" type="submit">
            Add Budget
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default AddBudgetModal;
