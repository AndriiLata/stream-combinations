import React from "react";
import { StreamingPackage } from "../../context/SearchContext";

interface PackageCardProps {
  pkg: StreamingPackage;
  userHasBought: boolean;
  calculateStats: (pkg: StreamingPackage) => {
    coveragePercentage: number;
    livePercentage: number;
    highlightsPercentage: number;
  };
  onBuy?: () => void;
}

const PackageCard: React.FC<PackageCardProps> = ({
  pkg,
  userHasBought,
  calculateStats,
  onBuy,
}) => {
  const { coveragePercentage, livePercentage, highlightsPercentage } =
    calculateStats(pkg);

  const displayPrice = userHasBought
    ? "SUBSCRIBED"
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
          src={require(`../../images/${pkg.name}.png`)}
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
          <span className="text-sm text-blue-700 font-semibold mb-1">LIVE</span>
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
          !userHasBought && (
            <div className="text-sm text-blue-800">
              Normal: €{(pkg.monthly_price_cents / 100).toFixed(2)}/mo
            </div>
          )}

        {/* If not bought and not free, show buy button*/}
        {!userHasBought &&
          pkg.monthly_price_yearly_subscription_in_cents > 0 && (
            <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
              <button className="btn btn-xl btn-warning" onClick={onBuy}>
                Buy for {pkg.monthly_price_yearly_subscription_in_cents / 100}{" "}
                CheckPoints
              </button>
            </div>
          )}
      </div>
    </div>
  );
};

export default PackageCard;
