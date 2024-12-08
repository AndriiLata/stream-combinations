import React, { useState } from "react";
import { useUserContext } from "../context/UserContext";
import { useSearchContext } from "../context/SearchContext";

const TopBar: React.FC = () => {
  const { user, boughtPackages } = useUserContext();
  const [showBoughtModal, setShowBoughtModal] = useState(false);

  return (
    <div className="w-full h-16 bg-white flex items-center justify-between px-4 border-b border-gray-300">
      <div className="text-blue-500 font-bold text-2xl ml-2">
        combinationCHECK
      </div>

      <div className="flex items-center">
        <div className="text-gray-700 font-semibold mr-4">
          {user ? user.balance : "..."} CheckPoints
        </div>
        <button
          className="btn btn-warning btn-sm mr-5"
          onClick={() => setShowBoughtModal(true)}
        >
          My Packages
        </button>
      </div>

      {showBoughtModal && (
        <div className="modal modal-open">
          <div className="modal-box bg-base-100 border border-base-300">
            <h3 className="font-bold text-lg text-blue-700">
              Your Bought Packages
            </h3>
            {boughtPackages.length > 0 ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-4">
                {boughtPackages.map((pkg) => (
                  <div
                    key={pkg.id}
                    className="card border border-base-300 bg-blue-50 shadow-md p-4 flex items-center gap-4"
                  >
                    <img
                      src={require(`../images/${pkg.name}.png`)}
                      alt={pkg.name}
                      className="w-12 h-12 object-contain rounded"
                    />
                    <div className="text-blue-900 font-semibold">
                      {pkg.name}
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="mt-4">You have not bought any packages yet.</div>
            )}
            <div className="modal-action">
              <button className="btn" onClick={() => setShowBoughtModal(false)}>
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default TopBar;
