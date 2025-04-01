import { useLocation } from "react-router-dom";

function TopNavbar({ toggleSidebar }) {
  const location = useLocation();

  const getPageTitle = () => {
    const path = location.pathname.substring(1);
    if (!path) return "Dashboard";

    return path
      .split("-")
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
      .join(" ");
  };

  return (
    <nav className="navbar navbar-expand-lg top-navbar">
      <div className="container-fluid">
        <div className="d-flex align-items-center">
          <button
            className="sidebar-toggle"
            onClick={toggleSidebar}
            aria-label="Toggle Sidebar"
          >
            ☰
          </button>
          <span className="navbar-brand ms-3 mb-0 h1">{getPageTitle()}</span>
        </div>
      </div>
    </nav>
  );
}

export default TopNavbar;
