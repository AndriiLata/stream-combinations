import React, { useEffect, useState } from "react";
import { useSearchContext } from "../context/SearchContext";
import DetailedComparisonModal from "../components/DetailedComparison";
import { StreamingPackage } from "../context/SearchContext";

const ResultsPage: React.FC = () => {
  const { setMode, searchResultData } = useSearchContext();
  const [showModal, setShowModal] = useState(false);
  const [showMore, setShowMore] = useState(false);

  useEffect(() => {
    setMode("results");
  }, [setMode]);

  if (!searchResultData) {
    return (
      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Search Results</h1>
        <p>Loading results...</p>
      </div>
    );
  }

  const {
    gamesByTournament,
    streamingPackages,
    offersToPackageID,
    otherPackages,
  } = searchResultData;

  if (streamingPackages.length === 0) {
    return (
      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Search Results</h1>
        <p>
          Sorry but there are currently no Streaming Packages in our Dataset
          which cover your search. Reach out to Check24 for new and current
          Datasets.
        </p>
      </div>
    );
  }

  const allGames = Object.values(gamesByTournament).flatMap((t) =>
    Object.values(t)
  );
  const totalGames = allGames.length;

  const calculateStats = (pkg: StreamingPackage) => {
    const offersForPkg = offersToPackageID[pkg.id.toString()] || {};
    const coveredGameIds = Object.keys(offersForPkg);
    const totalCovered = coveredGameIds.length;
    const liveCount = coveredGameIds.filter(
      (gameId) => offersForPkg[gameId].live
    ).length;
    const highlightsCount = coveredGameIds.filter(
      (gameId) => offersForPkg[gameId].highlights
    ).length;

    const coveragePercentage =
      totalGames > 0 ? Math.ceil((totalCovered / totalGames) * 100) : 0;
    const livePercentage =
      totalCovered > 0 ? Math.ceil((liveCount / totalCovered) * 100) : 0;
    const highlightsPercentage =
      totalCovered > 0 ? Math.ceil((highlightsCount / totalCovered) * 100) : 0;

    return { coveragePercentage, livePercentage, highlightsPercentage };
  };

  const renderPackageCard = (pkg: StreamingPackage) => {
    const { coveragePercentage, livePercentage, highlightsPercentage } =
      calculateStats(pkg);

    return (
      <div
        key={pkg.id}
        className="card border border-base-300 bg-blue-50 shadow-md p-4
                   grid grid-cols-[280px_minmax(0,1fr)_210px] items-center gap-4 relative"
      >
        <div className="flex items-center w-[280px] overflow-hidden">
          <img
            src={require(`../images/${pkg.name}.png`)}
            alt={pkg.name}
            className="max-w-[64px] max-h-[64px] object-contain rounded flex-shrink-0"
          />
          <div className="ml-4 text-blue-900 font-semibold break-words whitespace-normal max-w-[230px]">
            {pkg.name}
          </div>
        </div>

        <div className="flex flex-row items-center justify-center gap-8">
          <div className="flex flex-col items-center">
            <span className="text-sm text-blue-700 font-semibold mb-1">
              Games Covered
            </span>
            <div
              className="radial-progress bg-blue-100 text-blue-600"
              style={{ "--value": coveragePercentage } as React.CSSProperties}
            >
              {coveragePercentage}%
            </div>
          </div>

          <div className="flex flex-col items-center">
            <span className="text-sm text-blue-700 font-semibold mb-1">
              LIVE
            </span>
            <div
              className="radial-progress bg-green-100 text-green-600"
              style={{ "--value": livePercentage } as React.CSSProperties}
            >
              {livePercentage}%
            </div>
          </div>

          <div className="flex flex-col items-center">
            <span className="text-sm text-blue-700 font-semibold mb-1">
              HIGHLIGHTS
            </span>
            <div
              className="radial-progress bg-yellow-50 text-yellow-400"
              style={{ "--value": highlightsPercentage } as React.CSSProperties}
            >
              {highlightsPercentage}%
            </div>
          </div>
        </div>

        <div className="flex flex-col items-center border-l border-base-300 pl-4 w-[210px]">
          <div className="text-xl font-bold text-blue-800 mb-1">
            {pkg.monthly_price_yearly_subscription_in_cents === 0
              ? "FREE"
              : `€${(
                  pkg.monthly_price_yearly_subscription_in_cents / 100
                ).toFixed(2)}/mo (yearly)`}
          </div>
          {pkg.monthly_price_cents !== 7777 &&
            pkg.monthly_price_cents !== 0 && (
              <div className="text-sm text-blue-800">
                Normal: €{(pkg.monthly_price_cents / 100).toFixed(2)}/mo
              </div>
            )}
        </div>
      </div>
    );
  };

  // Determine which packages to show in the detailed comparison modal
  const displayPackages = showMore
    ? [...streamingPackages, ...otherPackages]
    : streamingPackages;

  return (
    <div className="p-6 relative">
      <div className="card bg-base-100 shadow-xl border border-base-300 p-6 mb-6">
        <h1 className="text-2xl font-bold mb-4 text-blue-700">
          Best Combination of Packages:
        </h1>
        <div className="flex flex-col gap-4">
          {streamingPackages.map(renderPackageCard)}
        </div>

        {otherPackages && otherPackages.length > 0 && (
          <div className="mt-6">
            <button
              className="btn btn-outline to-blue-700"
              onClick={() => setShowMore(!showMore)}
            >
              {showMore ? "-  HIDE OTHER PACKAGES" : "+  SHOW MORE PACKAGES"}
            </button>
            {showMore && (
              <div className="flex flex-col gap-4 mt-4">
                {otherPackages.map(renderPackageCard)}
              </div>
            )}
          </div>
        )}
      </div>

      {/* Fixed Button to open modal */}
      <div className="fixed bottom-5 right-10">
        <button
          className="btn btn-wide btn-warning"
          onClick={() => setShowModal(true)}
        >
          Detailed Comparison
        </button>
      </div>

      <DetailedComparisonModal
        open={showModal}
        onClose={() => setShowModal(false)}
        displayPackages={displayPackages}
      />
    </div>
  );
};

export default ResultsPage;
