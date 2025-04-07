import { useState, useEffect } from "react";
import { Outlet, useLocation } from "react-router-dom";
import Sidebar from "./Sidebar";
import TopNavbar from "./TopNavbar";
import "../styles/layout.css";

function Layout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(window.innerWidth > 768);
  const [navbarHeight, setNavbarHeight] = useState(0);
  const location = useLocation();

  const handleNavbarHeight = (height) => {
    setNavbarHeight(height);
    // Update CSS variable
    document.documentElement.style.setProperty(
      "--navbar-height",
      `${height}px`
    );
  };

  useEffect(() => {
    if (window.innerWidth <= 768) {
      setIsSidebarOpen(false);
    }
  }, [location]);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth <= 768) {
        setIsSidebarOpen(false);
      }
    };

    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className="d-flex">
      <Sidebar isOpen={isSidebarOpen} />
      <div className={`main-content ${!isSidebarOpen ? "sidebar-closed" : ""}`}>
        <TopNavbar
          toggleSidebar={toggleSidebar}
          onHeightChange={handleNavbarHeight}
        />
        <div
          className="container-fluid p-4"
          style={{ marginTop: `${navbarHeight}px` }}
        >
          <Outlet />
        </div>
      </div>
    </div>
  );
}

export default Layout;
