import { useLocation } from "react-router-dom";
import { Navbar, Container } from "react-bootstrap";
import { List } from "react-bootstrap-icons";

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
    <Navbar expand="lg" className="top-navbar">
      <Container fluid>
        <div className="d-flex align-items-center">
          <button
            className="sidebar-toggle"
            onClick={toggleSidebar}
            aria-label="Toggle Sidebar"
          >
            <List size={24} />
          </button>
          <Navbar.Brand className="ms-3 mb-0">{getPageTitle()}</Navbar.Brand>
        </div>
      </Container>
    </Navbar>
  );
}

export default TopNavbar;
