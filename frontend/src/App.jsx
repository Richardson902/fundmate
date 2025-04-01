import { BrowserRouter } from "react-router-dom";
import AppRoutes from "./routes/AppRoutes";

import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "./App.css";

function App() {
  return (
    <BrowserRouter>
      <AppRoutes />
    </BrowserRouter>
  );
}

export default App;
