import { useState } from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";
import TopNavbar from "./TopNavbar";
import "../styles/layout.css";

function Layout() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);

  const toggleSidebar = () => {
    setIsSidebarOpen(!isSidebarOpen);
  };

  return (
    <div className="d-flex">
      <Sidebar isOpen={isSidebarOpen} />
      <div className={`main-content ${!isSidebarOpen ? "sidebar-closed" : ""}`}>
        <TopNavbar toggleSidebar={toggleSidebar} />
        <div className="container-fluid p-4">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

export default Layout;
