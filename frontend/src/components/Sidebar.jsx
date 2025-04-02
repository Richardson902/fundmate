import { Link, useLocation } from "react-router-dom";
import { Nav } from "react-bootstrap";

function Sidebar({ isOpen }) {
  const location = useLocation();

  const menuItems = [
    { path: "/dashboard", name: "Overview" },
    { path: "/transactions", name: "Transactions" },
    { path: "/scheduled-transactions", name: "Scheduled transactions" },
    { path: "/accounts", name: "Accounts" },
    { path: "/budgets", name: "Budgets" },
  ];

  return (
    <div
      className={`d-flex flex-column flex-shrink-0 sidebar ${
        isOpen ? "open" : ""
      }`}
      style={{ width: "280px" }}
    >
      <div className="header-section">
        <span className="brand">FundMate</span>
      </div>
      <Nav className="flex-column p-3">
        {menuItems.map((item) => (
          <Nav.Item key={item.path}>
            <Nav.Link
              as={Link}
              to={item.path}
              active={location.pathname === item.path}
            >
              {item.name}
            </Nav.Link>
          </Nav.Item>
        ))}
      </Nav>
    </div>
  );
}

export default Sidebar;
