import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import SelectionPage from "./pages/SelectionPage";
import ResultsPage from "./pages/ResultsPage";
import Layout from "./components/layout/Layout";
import { SearchProvider } from "./context/SearchContext";

function App() {
  return (
    <SearchProvider>
      <Router>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route element={<Layout />}>
            <Route path="/select" element={<SelectionPage />} />
            <Route path="/results" element={<ResultsPage />} />
          </Route>
        </Routes>
      </Router>
    </SearchProvider>
  );
}

export default App;
