import React from "react";
import { Outlet } from "react-router-dom";
import Sidebar from "./Sidebar";

const Layout: React.FC = () => {
  return (
    <div className="flex w-screen h-screen overflow-hidden">
      <Sidebar />
      <div className="flex-1 overflow-auto p-4">
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
