import { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import { useCategories } from "../contexts/CategoryContext";
import { useAccounts } from "../contexts/AccountContext";

function AddScheduledIncomeModal({ show, onHide, onAdd }) {
  const { categories } = useCategories();
  const { accounts } = useAccounts();
  const [amount, setAmount] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [accountId, setAccountId] = useState("");
  const [executionDate, setExecutionDate] = useState(
    new Date().toISOString().split("T")[0]
  );
  const [recurrenceType, setRecurrenceType] = useState("MONTHLY");
  const [recurrenceInterval, setRecurrenceInterval] = useState(1);
  const [occurrences, setOccurrences] = useState(1);
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
        amount: Math.abs(parseFloat(amount)),
        categoryId,
        accountId,
        executionDate,
        recurrenceType,
        recurrenceInterval: parseInt(recurrenceInterval),
        occurrences: parseInt(occurrences),
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
    setExecutionDate(new Date().toISOString().split("T")[0]);
    setRecurrenceType("MONTHLY");
    setRecurrenceInterval(1);
    setOccurrences(1);
    setFromName("");
    setNote("");
    setError("");
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Schedule Income</Modal.Title>
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
            <Form.Label>Start Date</Form.Label>
            <Form.Control
              type="date"
              value={executionDate}
              onChange={(e) => setExecutionDate(e.target.value)}
              required
            />
          </Form.Group>

          <div className="row mb-3">
            <div className="col">
              <Form.Group>
                <Form.Label>Repeats every</Form.Label>
                <Form.Control
                  type="number"
                  min="1"
                  value={recurrenceInterval}
                  onChange={(e) => setRecurrenceInterval(e.target.value)}
                />
              </Form.Group>
            </div>
            <div className="col">
              <Form.Group>
                <Form.Label>Frequency</Form.Label>
                <Form.Select
                  value={recurrenceType}
                  onChange={(e) => setRecurrenceType(e.target.value)}
                >
                  <option value="DAILY">Days</option>
                  <option value="WEEKLY">Weeks</option>
                  <option value="MONTHLY">Months</option>
                  <option value="YEARLY">Years</option>
                </Form.Select>
              </Form.Group>
            </div>
          </div>

          <Form.Group className="mb-3">
            <Form.Label>Number of times</Form.Label>
            <Form.Control
              type="number"
              min="1"
              value={occurrences}
              onChange={(e) => setOccurrences(e.target.value)}
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
              rows={2}
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
          <Button variant="success" type="submit">
            Schedule Income
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
}

export default AddScheduledIncomeModal;
