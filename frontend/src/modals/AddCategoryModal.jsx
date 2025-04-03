import { useState } from "react";
import { Modal, Form, Button } from "react-bootstrap";
import IconSelectorModal from "./IconSelectorModal";

function AddCategoryModal({ show, onHide, onAdd }) {
  const [name, setName] = useState("");
  const [icon, setIcon] = useState("");
  const [error, setError] = useState("");
  const [showIconSelector, setShowIconSelector] = useState(false);

  const handleIconSelect = (selectedIcon) => {
    setIcon(selectedIcon);
    setShowIconSelector(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!name.trim()) {
      setError("Category name is required");
      return;
    }

    try {
      await onAdd({
        categoryName: name.trim(),
        icon: icon.trim(),
      });
      handleClose();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleClose = () => {
    setName("");
    setIcon("");
    setError("");
    onHide();
  };

  return (
    <>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Add Category</Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            {error && (
              <div className="alert alert-danger" role="alert">
                {error}
              </div>
            )}
            <Form.Group className="mb-3">
              <Form.Label>Category Name</Form.Label>
              <Form.Control
                type="text"
                value={name}
                onChange={(e) => setName(e.target.value)}
                placeholder="Enter category name"
                required
              />
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Icon</Form.Label>
              <div className="d-flex align-items-center gap-2">
                {icon && (
                  <img
                    src={`/icons/${icon}.png`}
                    alt={icon}
                    style={{ width: "24px", height: "24px" }}
                  />
                )}
                <Button
                  variant="outline-secondary"
                  onClick={() => setShowIconSelector(true)}
                  type="button"
                >
                  {icon ? "Change Icon" : "Select Icon"}
                </Button>
              </div>
            </Form.Group>
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClose}>
              Cancel
            </Button>
            <Button variant="primary" type="submit">
              Add Category
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
      <IconSelectorModal
        show={showIconSelector}
        onHide={() => setShowIconSelector(false)}
        onSelect={handleIconSelect}
      />
    </>
  );
}

export default AddCategoryModal;
