import { Modal, Form } from "react-bootstrap";
import { useState } from "react";

function DashboardListSettingsModal({
  show,
  onHide,
  onSave,
  currentValue,
  listName,
}) {
  const [itemsToShow, setItemsToShow] = useState(currentValue);

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(parseInt(itemsToShow));
    onHide();
  };

  return (
    <Modal show={show} onHide={onHide}>
      <Modal.Header closeButton>
        <Modal.Title>{listName} List Settings</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Number of items to show</Form.Label>
            <Form.Select
              value={itemsToShow}
              onChange={(e) => setItemsToShow(e.target.value)}
            >
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
              <option value="6">6</option>
              <option value="7">7</option>
              <option value="8">8</option>
              <option value="9">9</option>
            </Form.Select>
          </Form.Group>
          <div className="d-flex justify-content-end">
            <button type="submit" className="btn btn-primary">
              Save Changes
            </button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
}

export default DashboardListSettingsModal;
