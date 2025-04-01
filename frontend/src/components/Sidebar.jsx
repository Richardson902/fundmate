import { Link, useLocation } from "react-router-dom";

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
      <ul className="nav nav-pills flex-column mb-auto p-3">
        {menuItems.map((item) => (
          <li key={item.path} className="nav-item">
            <Link
              to={item.path}
              className={`nav-link ${
                location.pathname === item.path ? "active" : ""
              }`}
            >
              {item.name}
            </Link>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Sidebar;
