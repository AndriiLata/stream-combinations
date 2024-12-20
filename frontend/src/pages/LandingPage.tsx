import { useNavigate } from "react-router-dom";
import { useBestPackages } from "../hooks/useBestPackages";
import { useState } from "react";

function LandingPage() {
  const navigate = useNavigate();
  const { fetchBestPackages } = useBestPackages();
  const [loading, setLoading] = useState(false);
  const [showNoPreCacheModal, setShowNoPreCacheModal] = useState(false);

  const features = [
    {
      title: "Best Streaming Combo",
      description:
        "Search for the best streaming combination for your favorite teams and tournaments. The system considers live coverage and price to give you the optimal packages.",
      icon: "🎯",
    },
    {
      title: "Purchase Packages",
      description:
        "Buy packages directly from the platform. Owned packages cost 0 in future searches, improving your outcomes.",
      icon: "💳",
    },
    {
      title: "Smart Caching",
      description:
        "All search requests are cached. If a search is repeated, results are returned instantly for a smoother experience.",
      icon: "⚡",
    },
  ];

  const handlePreCache = async () => {
    setLoading(true);

    // Predefined searches
    const predefinedSearches = [
      // 1: No parameters
      {},
      // 2: Bayern München
      { teams: ["Bayern München"] },
      // 3: Hatayspor, Deutschland, Bayern München and Real Madrid
      { teams: ["Hatayspor", "Deutschland", "Bayern München", "Real Madrid"] },
      // 4: Oxford United, Los Angeles FC, AS Rom
      { teams: ["Oxford United", "Los Angeles FC", "AS Rom"] },
    ];

    await Promise.all(
      predefinedSearches.map((params) => fetchBestPackages(params))
    );

    setLoading(false);
    navigate("/select");
  };

  const handleWithoutPreCache = () => {
    setShowNoPreCacheModal(true);
  };

  const proceedWithoutPreCache = () => {
    setShowNoPreCacheModal(false);
    navigate("/select");
  };

  return (
    <div className="bg-base-200 min-h-screen flex flex-col">
      <div className="hero bg-base-200 flex-grow">
        <div className="hero-content text-center flex flex-col max-w-4xl">
          <h1 className="text-5xl font-bold mt-10 mb-4 text-blue-900">
            Welcome to combinationCHECK
          </h1>
          <p className="mb-10 text-lg text-gray-700">
            Get the best streaming package combinations for your favorite teams
            and tournaments.
          </p>

          {/* Features section */}
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 mb-10">
            {features.map((feature, idx) => (
              <div
                key={idx}
                className="bg-white p-6 rounded-lg shadow-md hover:shadow-xl transform hover:-translate-y-1 transition-all flex flex-col items-center text-center"
              >
                <div className="text-3xl mb-3">{feature.icon}</div>
                <h2 className="text-xl font-semibold text-gray-800 mb-2">
                  {feature.title}
                </h2>
                <p className="text-sm text-gray-600">{feature.description}</p>
              </div>
            ))}
          </div>

          <div className="flex flex-col sm:flex-row gap-4">
            <button
              className="btn btn-lg bg-gray-300 hover:bg-gray-400 text-black border-none"
              onClick={handleWithoutPreCache}
            >
              Continue without Pre-Caching
            </button>
            <button
              className={`btn btn-warning btn-lg ${loading ? "loading" : ""}`}
              onClick={handlePreCache}
              disabled={loading}
            >
              {loading ? "Pre-Caching..." : "Continue with Pre-Caching"}
            </button>
          </div>

          <footer className="mt-10 text-gray-500 text-sm">
            © 2024 CHECK24 GenDev Streaming Package Comparison Challenge. All
            rights reserved.
          </footer>
        </div>
      </div>

      {/* Modal for no pre-cache warning */}
      {showNoPreCacheModal && (
        <div className="modal modal-open">
          <div className="modal-box">
            <h3 className="font-bold text-lg">Potential Slow First Query</h3>
            <p className="py-4 text-sm text-gray-700">
              The very first query might be slower without pre-caching due to
              system startup. Are you sure you want to continue without
              pre-caching?
            </p>
            <div className="modal-action">
              <button
                className="btn btn-error"
                onClick={() => setShowNoPreCacheModal(false)}
              >
                Cancel
              </button>
              <button
                className="btn btn-gray-300"
                onClick={proceedWithoutPreCache}
              >
                Proceed Anyway
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default LandingPage;
