import { useState } from "react";
import { useCategories } from "../contexts/CategoryContext";
import { Plus } from "react-bootstrap-icons";
import CategoryList from "../components/CategoryList";
import AddCategoryModal from "../modals/AddCategoryModal";

function Categories() {
  const { categories, addCategory, loading, error } = useCategories();
  const [showAddModal, setShowAddModal] = useState(false);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="container-fluid">
      {error && (
        <div className="alert alert-danger" role="alert">
          {error}
        </div>
      )}

      <CategoryList categories={categories} />

      <button
        className="btn btn-primary rounded-circle d-flex align-items-center justify-content-center position-fixed"
        onClick={() => setShowAddModal(true)}
        style={{
          width: "56px",
          height: "56px",
          bottom: "2rem",
          right: "2rem",
          zIndex: 1030,
          boxShadow: "0 2px 5px rgba(0,0,0,0.2)",
        }}
      >
        <Plus size={24} />
      </button>

      <AddCategoryModal
        show={showAddModal}
        onHide={() => setShowAddModal(false)}
        onAdd={addCategory}
      />
    </div>
  );
}

export default Categories;
