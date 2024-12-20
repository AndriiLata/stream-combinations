// src/pages/ResultsPage.tsx
import React, { useEffect, useState } from "react";
import { useSearchContext } from "../context/SearchContext";
import DetailedComparisonModal from "../components/results/DetailedComparison";
import { StreamingPackage } from "../context/SearchContext";
import { useUserContext } from "../context/UserContext";
import axios from "axios";
import TermAndAgreements from "../components/z_other/TermAndAgreements";
import ConfirmationModal from "../components/results/ConfirmationModal";
import TopUpModal from "../components/results/TopUpModal";
import PackageCard from "../components/results/PackageCard";
import { useCoverageStats } from "../hooks/useCoverageStats";

const ResultsPage: React.FC = () => {
  const { setMode, searchResultData } = useSearchContext();
  const { user, buyPackage, refreshUser } = useUserContext();

  const [showModal, setShowModal] = useState(false);
  const [showMore, setShowMore] = useState(false);

  // Error and top-up states
  const [error, setError] = useState<string | null>(null);
  const [showTopUpModal, setShowTopUpModal] = useState(false);

  const [showConfirmationModal, setShowConfirmationModal] = useState(false);
  const [showTermsModal, setShowTermsModal] = useState(false);
  const [termsAccepted, setTermsAccepted] = useState(false);
  const [packageToBuy, setPackageToBuy] = useState<StreamingPackage | null>(
    null
  );

  useEffect(() => {
    setMode("results");
  }, [setMode]);

  const gamesByTournament = searchResultData?.gamesByTournament ?? {};
  const offersToPackageID = searchResultData?.offersToPackageID ?? {};

  const { calculateStats } = useCoverageStats({
    gamesByTournament,
    offersToPackageID,
  });

  if (!searchResultData) {
    return (
      <div className="p-6">
        <h1 className="text-2xl font-bold mb-4">Search Results</h1>
        <p>Loading results...</p>
      </div>
    );
  }

  const { streamingPackages, otherPackages } = searchResultData;

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

  const initiateBuy = (pkg: StreamingPackage) => {
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

  const apiBaseUrl = process.env.REACT_APP_API_URL || "http://localhost:8080";

  const handleTopUp = async () => {
    // Top up 100 CheckPoints
    await axios.post(`${apiBaseUrl}/user/top-up`, null, {
      params: { amount: 100 },
    });
    await refreshUser();
    setShowTopUpModal(false);
    setError(null);
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
          {streamingPackages.map((pkg) => (
            <PackageCard
              key={pkg.id}
              pkg={pkg}
              calculateStats={calculateStats}
              userHasBought={!!user?.boughtPackageIds.includes(pkg.id)}
              onBuy={() => initiateBuy(pkg)}
            />
          ))}
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
                {otherPackages.map((pkg) => (
                  <PackageCard
                    key={pkg.id}
                    pkg={pkg}
                    calculateStats={calculateStats}
                    userHasBought={!!user?.boughtPackageIds.includes(pkg.id)}
                    onBuy={() => initiateBuy(pkg)}
                  />
                ))}
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

      <TopUpModal
        open={showTopUpModal}
        user={user}
        onClose={() => setShowTopUpModal(false)}
        onTopUp={handleTopUp}
      />

      <ConfirmationModal
        open={showConfirmationModal}
        packageToBuy={packageToBuy}
        termsAccepted={termsAccepted}
        onClose={() => {
          setShowConfirmationModal(false);
          setPackageToBuy(null);
          setTermsAccepted(false);
        }}
        onConfirm={confirmPurchase}
        onToggleTerms={() => setTermsAccepted(!termsAccepted)}
        onShowTerms={() => setShowTermsModal(true)}
      />

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
