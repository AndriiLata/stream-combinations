import { StreamingPackage } from "../context/SearchContext";

interface OffersToPackageID {
  [key: string]: {
    [gameId: string]: {
      live: boolean;
      highlights: boolean;
    };
  };
}

interface GamesByTournament {
  [tournament: string]: {
    [gameId: string]: any;
  };
}

interface UseCoverageStatsParams {
  gamesByTournament: GamesByTournament;
  offersToPackageID: OffersToPackageID;
}

export function useCoverageStats({
  gamesByTournament,
  offersToPackageID,
}: UseCoverageStatsParams) {
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

    const coverageRatio =
      totalGames > 0 ? (totalCovered / totalGames) * 100 : 0;
    let coveragePercentage =
      coverageRatio > 99 && coverageRatio < 100 ? 99 : Math.ceil(coverageRatio);

    const liveRatio = totalCovered > 0 ? (liveCount / totalCovered) * 100 : 0;
    let livePercentage =
      liveRatio > 99 && liveRatio < 100 ? 99 : Math.ceil(liveRatio);

    const highlightsRatio =
      totalCovered > 0 ? (highlightsCount / totalCovered) * 100 : 0;
    let highlightsPercentage =
      highlightsRatio > 99 && highlightsRatio < 100
        ? 99
        : Math.ceil(highlightsRatio);

    return { coveragePercentage, livePercentage, highlightsPercentage };
  };

  return { calculateStats };
}
