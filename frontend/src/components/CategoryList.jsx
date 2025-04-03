import { useState } from "react";
import { Card } from "react-bootstrap";
import { useCategories } from "../contexts/CategoryContext";
import CategoryItem from "./CategoryItem";

function CategoryList() {
  const { categories, deleteCategory } = useCategories();
  const [error, setError] = useState("");

  const handleDelete = async (id) => {
    if (window.confirm("Are you sure you want to delete this category?")) {
      try {
        await deleteCategory(id);
      } catch (error) {
        setError(error.message);
      }
    }
  };
  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-md-8">
          {error && (
            <div className="alert alert-danger" role="alert">
              {error}
            </div>
          )}
          <Card>
            <Card.Body className="p-0">
              {categories.map((category, index) => (
                <>
                  <CategoryItem
                    key={category.id}
                    category={category}
                    onDelete={() => handleDelete(category.id)}
                  />
                  {index < categories.length - 1 && <hr className="my-0" />}
                </>
              ))}
            </Card.Body>
          </Card>
        </div>
      </div>
    </div>
  );
}

export default CategoryList;
