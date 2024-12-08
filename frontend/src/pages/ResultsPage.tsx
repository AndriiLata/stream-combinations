import React, { useEffect, useState } from "react";
import { useSearchContext } from "../context/SearchContext";
import DetailedComparisonModal from "../components/DetailedComparison";
import { StreamingPackage } from "../context/SearchContext";
import { useUserContext } from "../context/UserContext";
import axios from "axios";
import TermAndAgreements from "../components/terms_and_agreements/TermAndAgreements";

const ResultsPage: React.FC = () => {
  const { setMode, searchResultData, setSearchResultData } = useSearchContext();
  const { user, buyPackage, refreshUser } = useUserContext();
  const [showModal, setShowModal] = useState(false);
  const [showMore, setShowMore] = useState(false);

  // Error and top-up states
  const [error, setError] = useState<string | null>(null);
  const [showTopUpModal, setShowTopUpModal] = useState(false);

  // New states for confirmation flow
  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [showTermsModal, setShowTermsModal] = useState(false);
  const [termsAccepted, setTermsAccepted] = useState(false);
  const [packageToBuy, setPackageToBuy] = useState<StreamingPackage | null>(
    null
  );

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
          Sorry, but there are currently no Streaming Packages in our dataset
          that cover your search.
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

  const initiateBuy = (pkg: StreamingPackage) => {
    // Instead of buying immediately, open confirmation modal
    setPackageToBuy(pkg);
    setShowConfirmationModal(true);
    setTermsAccepted(false);
  };

  const confirmPurchase = async () => {
    if (!packageToBuy || !termsAccepted) return;
    const costInCents = packageToBuy.monthly_price_yearly_subscription_in_cents;
    try {
      await buyPackage(packageToBuy.id, costInCents);
      setShowConfirmationModal(false);
      setPackageToBuy(null);
    } catch (e: any) {
      if (e.response && e.response.status === 400) {
        // Not enough CheckPoints
        setError(e.response.data.message || "Not enough CheckPoints.");
        setShowTopUpModal(true);
      } else {
        console.error("Unknown error buying package:", e);
      }
      setShowConfirmationModal(false);
      setPackageToBuy(null);
    }
  };

  const handleTopUp = async () => {
    // Top up 100 CheckPoints
    await axios.post("/user/top-up", null, { params: { amount: 100 } });
    await refreshUser();
    setShowTopUpModal(false);
    setError(null);
  };

  const renderPackageCard = (pkg: StreamingPackage) => {
    const { coveragePercentage, livePercentage, highlightsPercentage } =
      calculateStats(pkg);

    const isBought = user?.boughtPackageIds.includes(pkg.id);
    const displayPrice = isBought
      ? "BOUGHT"
      : pkg.monthly_price_yearly_subscription_in_cents === 0
      ? "FREE"
      : `€${(pkg.monthly_price_yearly_subscription_in_cents / 100).toFixed(
          2
        )}/mo (yearly)`;

    return (
      <div
        key={pkg.id}
        className="card border border-base-300 bg-blue-50 shadow-md p-4
                   grid grid-cols-[280px_minmax(0,1fr)_210px] items-center gap-4 relative group"
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

        <div className="flex flex-col items-center border-l border-base-300 pl-4 w-[210px] relative">
          <div className="text-xl font-bold text-blue-800 mb-1">
            {displayPrice}
          </div>
          {pkg.monthly_price_cents !== 7777 &&
            pkg.monthly_price_cents !== 0 &&
            !isBought && (
              <div className="text-sm text-blue-800">
                Normal: €{(pkg.monthly_price_cents / 100).toFixed(2)}/mo
              </div>
            )}

          {/* If not bought and not free, show buy button on hover */}
          {!isBought && pkg.monthly_price_yearly_subscription_in_cents > 0 && (
            <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
              <button
                className="btn btn-xl btn-warning"
                onClick={() => initiateBuy(pkg)}
              >
                Buy for {pkg.monthly_price_yearly_subscription_in_cents / 100}{" "}
                CheckPoints
              </button>
            </div>
          )}
        </div>
      </div>
    );
  };

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

      {showTopUpModal && (
        <div className="modal modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg">Not Enough CheckPoints</h3>
            <p className="mt-4">
              You don't have enough CheckPoints to buy this package.
            </p>
            <p className="mt-2">Your current balance: {user?.balance}</p>
            <div className="modal-action flex justify-between mt-4">
              <button
                className="btn btn-error"
                onClick={() => setShowTopUpModal(false)}
              >
                Cancel
              </button>
              <button className="btn btn-warning" onClick={handleTopUp}>
                Top Up CheckPoints
              </button>
            </div>
          </div>
        </div>
      )}

      {showConfirmationModal && packageToBuy && (
        <div className="modal modal-open">
          <div className="modal-box bg-base-100 border border-base-300">
            <h3 className="font-bold text-xl">Confirm Purchase</h3>
            <p className="mt-4">
              Are you sure you want to subscribe to{" "}
              <span className="font-semibold">{packageToBuy.name}</span> for{" "}
              <span className="font-semibold">
                {packageToBuy.monthly_price_yearly_subscription_in_cents / 100}
              </span>{" "}
              CheckPoints?
            </p>
            <div className="mt-4 flex items-center gap-2">
              <input
                type="checkbox"
                className="checkbox"
                checked={termsAccepted}
                onChange={() => setTermsAccepted(!termsAccepted)}
              />
              <span className="text-sm">
                I have read and agree to the{" "}
                <button
                  className="text-blue-600 underline"
                  onClick={() => setShowTermsModal(true)}
                >
                  Terms and Agreements
                </button>
                .
              </span>
            </div>

            <div className="modal-action mt-6 flex justify-between">
              <button
                className="btn btn-error"
                onClick={() => {
                  setShowConfirmationModal(false);
                  setPackageToBuy(null);
                  setTermsAccepted(false);
                }}
              >
                Cancel
              </button>
              <button
                className={`btn btn-warning ${
                  !termsAccepted && "btn-disabled"
                }`}
                onClick={confirmPurchase}
              >
                Subscribe
              </button>
            </div>
          </div>
        </div>
      )}

      {showTermsModal && (
        <div className="modal modal-open">
          <div className="modal-box bg-base-100 border border-base-300 max-h-[80vh] overflow-auto">
            <TermAndAgreements />
            <div className="modal-action mt-4">
              <button className="btn" onClick={() => setShowTermsModal(false)}>
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ResultsPage;
