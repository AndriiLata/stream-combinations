import React, { useState } from "react";
import { useSearchContext } from "../../context/SearchContext";

interface DetailedComparisonModalProps {
  open: boolean;
  onClose: () => void;
  displayPackages: any[]; // Change this to the correct type of your packages if you want stricter typing.
}

const DetailedComparisonModal: React.FC<DetailedComparisonModalProps> = ({
  open,
  onClose,
  displayPackages,
}) => {
  const { searchResultData } = useSearchContext();

  const [expandedTournaments, setExpandedTournaments] = useState<
    Record<string, boolean>
  >({});

  if (!searchResultData) return null;

  const { gamesByTournament, offersToPackageID } = searchResultData;

  const toggleTournament = (tournament: string) => {
    setExpandedTournaments((prev) => ({
      ...prev,
      [tournament]: !prev[tournament],
    }));
  };

  const coverageColorClass = (coverageCount: number, total: number) => {
    if (coverageCount === 0) {
      return "bg-gray-300";
    } else if (coverageCount === total) {
      return "bg-green-500";
    } else {
      return "bg-orange-400"; // partial coverage
    }
  };

  const coverageCircle = (coverageCount: number, total: number) => (
    <div
      className={`w-4 h-4 rounded-full ${coverageColorClass(
        coverageCount,
        total
      )}`}
    ></div>
  );

  const getTournamentCoverage = (tournament: string, pkgId: number) => {
    const tournamentGames = Object.values(gamesByTournament[tournament]);
    const totalGames = tournamentGames.length;
    const offers = offersToPackageID[pkgId.toString()] || {};

    let liveCount = 0;
    let highlightsCount = 0;
    for (const game of tournamentGames) {
      const gameOffer = offers[game.id.toString()];
      if (gameOffer?.live) {
        liveCount++;
      }
      if (gameOffer?.highlights) {
        highlightsCount++;
      }
    }

    return { liveCount, highlightsCount, totalGames };
  };

  const getGameCoverage = (gameId: number, pkgId: number) => {
    const offers = offersToPackageID[pkgId.toString()] || {};
    const gameOffer = offers[gameId.toString()];
    if (!gameOffer) {
      return { live: false, highlights: false };
    }
    return {
      live: !!gameOffer.live,
      highlights: !!gameOffer.highlights,
    };
  };

  const tournaments = Object.keys(gamesByTournament);

  return (
    <div className={`modal ${open ? "modal-open" : ""}`}>
      <div className="modal-box w-full max-w-5xl">
        <h2 className="text-xl font-bold mb-4">Detailed Comparison</h2>

        <div className="overflow-auto max-h-[70vh]">
          <table className="table table-compact w-full border-collapse text-sm leading-tight">
            <thead>
              <tr>
                <th className="bg-base-200 sticky top-0 z-10 px-2 py-2 text-left">
                  Tournament / Game
                </th>
                {displayPackages.map((pkg, idx) => (
                  <th
                    key={pkg.id}
                    className={`bg-base-200 sticky top-0 z-10 font-bold text-center px-2 py-2 ${
                      idx === 0 ? "border-l-0" : "border-l-2 border-base-300"
                    }`}
                    colSpan={2}
                  >
                    {pkg.name}
                  </th>
                ))}
              </tr>
              <tr>
                <th className="bg-base-200 sticky top-0 z-10 px-2 py-1"></th>
                {displayPackages.map((pkg, idx) => (
                  <React.Fragment key={pkg.id}>
                    <th
                      className={`bg-base-200 sticky top-0 z-10 font-normal text-center px-2 py-1 ${
                        idx === 0 ? "border-l-0" : "border-l-2 border-base-300"
                      }`}
                    >
                      LIVE
                    </th>
                    <th className="bg-base-200 sticky top-0 z-10 font-normal text-center px-2 py-1">
                      HIGHLIGHTS
                    </th>
                  </React.Fragment>
                ))}
              </tr>
            </thead>
            <tbody>
              {tournaments.map((tournament) => {
                const tournamentGames = Object.values(
                  gamesByTournament[tournament]
                );
                return (
                  <React.Fragment key={tournament}>
                    <tr
                      className="hover:bg-base-100 cursor-pointer"
                      onClick={() => toggleTournament(tournament)}
                    >
                      <td className="font-bold px-2 py-1">
                        <div className="flex items-center gap-2">
                          <button className="btn btn-xs btn-circle btn-outline">
                            {expandedTournaments[tournament] ? "-" : "+"}
                          </button>
                          {tournament}
                        </div>
                      </td>
                      {displayPackages.map((pkg, idx) => {
                        const { liveCount, highlightsCount, totalGames } =
                          getTournamentCoverage(tournament, pkg.id);
                        return (
                          <React.Fragment key={pkg.id}>
                            <td
                              className={`text-center px-2 py-1 ${
                                idx === 0 ? "" : "border-l-2 border-base-300"
                              }`}
                            >
                              <div className="flex justify-center items-center">
                                {coverageCircle(liveCount, totalGames)}
                              </div>
                            </td>
                            <td className="text-center px-2 py-1">
                              <div className="flex justify-center items-center">
                                {coverageCircle(highlightsCount, totalGames)}
                              </div>
                            </td>
                          </React.Fragment>
                        );
                      })}
                    </tr>

                    {expandedTournaments[tournament] &&
                      tournamentGames.map((game) => {
                        return (
                          <tr key={game.id} className="hover:bg-base-100">
                            <td className="pl-10 py-1">
                              {game.team_home} vs {game.team_away} (
                              {new Date(game.starts_at).toLocaleString()})
                            </td>
                            {displayPackages.map((pkg, idx) => {
                              const { live, highlights } = getGameCoverage(
                                game.id,
                                pkg.id
                              );
                              const liveCircleColor = live
                                ? "bg-green-500"
                                : "bg-gray-300";
                              const highlightCircleColor = highlights
                                ? "bg-green-500"
                                : "bg-gray-300";
                              return (
                                <React.Fragment key={pkg.id}>
                                  <td
                                    className={`text-center py-1 ${
                                      idx === 0
                                        ? ""
                                        : "border-l-2 border-base-300"
                                    }`}
                                  >
                                    <div className="flex justify-center items-center">
                                      <div
                                        className={`w-4 h-4 rounded-full ${liveCircleColor}`}
                                      ></div>
                                    </div>
                                  </td>
                                  <td className="text-center py-1">
                                    <div className="flex justify-center items-center">
                                      <div
                                        className={`w-4 h-4 rounded-full ${highlightCircleColor}`}
                                      ></div>
                                    </div>
                                  </td>
                                </React.Fragment>
                              );
                            })}
                          </tr>
                        );
                      })}
                  </React.Fragment>
                );
              })}
            </tbody>
          </table>
        </div>

        <div className="modal-action">
          <button className="btn btn-primary" onClick={onClose}>
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default DetailedComparisonModal;
