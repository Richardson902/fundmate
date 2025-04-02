import { Modal, Form, Button } from "react-bootstrap";
import { useState, useEffect } from "react";

function EditAccountModal({ show, onHide, onEdit, account }) {
  const [name, setName] = useState("");
  const [balance, setBalance] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (account) {
      setName(account.accountName);
      setBalance(account.balance.toString());
    }
  }, [account]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name.trim()) {
      setError("Account name is required");
      return;
    }

    try {
      await onEdit(account.id, {
        accountName: name.trim(),
        balance: parseFloat(balance) || 0,
      });
      handleClose();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleClose = () => {
    setName("");
    setBalance("");
    setError("");
    onHide();
  };

  return (
    <Modal show={show} onHide={handleClose}>
      <Modal.Header closeButton>
        <Modal.Title>Edit Account</Modal.Title>
      </Modal.Header>
      <Form onSubmit={handleSubmit}>
        <Modal.Body>
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
          <Form.Group className="mb-3">
            <Form.Label>Account Name</Form.Label>
            <Form.Control
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter account name"
              required
            />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Balance</Form.Label>
            <Form.Control
              type="number"
              value={balance}
              onChange={(e) => setBalance(e.target.value)}
              placeholder="0.00"
              step="0.01"
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

export default EditAccountModal;
