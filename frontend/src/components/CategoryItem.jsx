import { Trash } from "react-bootstrap-icons";

function CategoryItem({ category, onDelete }) {
  return (
    <div className="p-3">
      <div className="d-flex justify-content-between align-items-center">
        <div className="d-flex align-items-center">
          <img
            src={`/icons/${category.icon}.png`}
            alt={category.icon}
            style={{ width: "24px", height: "24px" }}
            className="me-2"
          />
          <h6 className="mb-0">{category.categoryName}</h6>
        </div>
        <div className="d-flex gap-3">
          <button
            onClick={() => onDelete(category.id)}
            className="btn btn-link p-0 text-danger"
          >
            <Trash size={16} />
          </button>
        </div>
      </div>
    </div>
  );
}

export default CategoryItem;
