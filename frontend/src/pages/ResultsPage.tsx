import React, { useEffect } from "react";
import { useSearchContext } from "../context/SearchContext";

const ResultsPage: React.FC = () => {
  const { setMode } = useSearchContext();

  useEffect(() => {
    setMode("results");
  }, [setMode]);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Search Results</h1>
      <p>Here we would display results returned from the GET request.</p>
      {/* The sidebar is already present, no changes needed here. */}
    </div>
  );
};

export default ResultsPage;
