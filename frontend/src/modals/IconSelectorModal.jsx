import { Modal } from "react-bootstrap";

const icons = [
  { name: "salary", path: "/icons/salary.png" },
  { name: "odd-jobs", path: "/icons/odd-jobs.png" },
  { name: "personal-savings", path: "icons/personal-savings.png" },
  { name: "groceries", path: "icons/groceries.png" },
  { name: "eating-out", path: "icons/eating-out.png" },
  { name: "bar", path: "icons/bar.png" },
  { name: "shopping", path: "icons/shopping.png" },
  { name: "fuel", path: "icons/fuel.png" },
  { name: "insurance", path: "icons/insurance.png" },
  { name: "entertainment", path: "icons/entertainment.png" },
  { name: "rent", path: "icons/rent.png" },
  { name: "bills", path: "icons/bills.png" },
  { name: "travel", path: "icons/travel.png" },
];

function IconSelectorModal({ show, onHide, onSelect }) {
  const handleIconSelect = (icon) => {
    onSelect(icon);
    onHide();
  };

  return (
    <Modal show={show} onHide={onHide} size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Select an Icon</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="d-flex flex-wrap gap-3 justify-content-center">
          {icons.map((icon) => (
            <div
              key={icon.name}
              onClick={() => handleIconSelect(icon.name)}
              className="d-flex flex-column align-items-center p-2 border rounded hover-shadow"
              style={{
                cursor: "pointer",
                minWidth: "80px",
                transition: "all 0.2s",
              }}
              role="button"
            >
              <img src={icon.path} alt={icon.name} />
            </div>
          ))}
        </div>
      </Modal.Body>
    </Modal>
  );
}

export default IconSelectorModal;
