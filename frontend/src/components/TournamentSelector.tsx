import React, { useMemo } from "react";
import { useSearchContext } from "../context/SearchContext";

interface TournamentInfo {
  tournament: string;
  startDate: string;
  endDate: string;
}

interface DataType {
  country: string;
  teams: string[];
  tournaments: TournamentInfo[];
}

interface TournamentSelectorProps {
  data: DataType[];
  selectedCountries: string[];
  searchQuery: string;
  showOnlyPrior: boolean;
  showFinished: boolean; // new prop
  showLive: boolean; // new prop
  showUpcoming: boolean; // new prop
}

const TournamentSelector: React.FC<TournamentSelectorProps> = ({
  data,
  selectedCountries,
  searchQuery,
  showOnlyPrior,
  showFinished,
  showLive,
  showUpcoming,
}) => {
  const {
    selectedTournaments,
    setSelectedTournaments,
    mode,
    previouslySearchedTournaments,
  } = useSearchContext();

  const now = new Date();

  const tournaments = useMemo(() => {
    // Filter by selectedCountries
    let filteredData = data;
    if (selectedCountries.length > 0) {
      filteredData = filteredData.filter((item) =>
        selectedCountries.includes(item.country)
      );
    }

    // Flatten and deduplicate tournaments by their name
    const uniqueMap = new Map<string, TournamentInfo>();
    for (const item of filteredData) {
      for (const t of item.tournaments) {
        if (!uniqueMap.has(t.tournament)) {
          uniqueMap.set(t.tournament, t);
        }
      }
    }

    let result = Array.from(uniqueMap.values());

    // Filter by search query
    if (searchQuery) {
      const q = searchQuery.toLowerCase();
      result = result.filter((t) => t.tournament.toLowerCase().includes(q));
    }

    // Filter by previously searched if showOnlyPrior is active
    if (showOnlyPrior) {
      result = result.filter((t) =>
        previouslySearchedTournaments.includes(t.tournament)
      );
    }

    result = result.filter((t) => {
      const start = new Date(t.startDate);
      const end = new Date(t.endDate);
      const isLive = now >= start && now <= end;
      const isUpcoming = now < start;
      const isFinished = now > end;

      // If none of the checkboxes are selected, show all
      if (!showFinished && !showLive && !showUpcoming) {
        return true;
      }

      // Otherwise, filter based on selected statuses
      if (showFinished && isFinished) return true;
      if (showLive && isLive) return true;
      if (showUpcoming && isUpcoming) return true;
      return false;
    });

    return result;
  }, [
    data,
    selectedCountries,
    searchQuery,
    showOnlyPrior,
    previouslySearchedTournaments,
    showFinished,
    showLive,
    showUpcoming,
  ]);

  const toggleTournamentSelection = (tournamentName: string) => {
    if (mode === "results") return;
    setSelectedTournaments((prev) =>
      prev.includes(tournamentName)
        ? prev.filter((t) => t !== tournamentName)
        : [...prev, tournamentName]
    );
  };

  const emojiMap = useMemo(() => {
    const emojis = ["🏆", "🎉", "⚽", "🏅"];
    const map: Record<string, string> = {};
    tournaments.forEach((t, index) => {
      map[t.tournament] = emojis[index % emojis.length];
    });
    return map;
  }, [tournaments]);

  return (
    <div className="flex flex-wrap mt-1">
      {tournaments.map((t) => {
        const start = new Date(t.startDate);
        const end = new Date(t.endDate);
        const isPreviouslySearched = previouslySearchedTournaments.includes(
          t.tournament
        );
        const isSelected = selectedTournaments.includes(t.tournament);
        const isLive = now >= start && now <= end;

        return (
          <div
            key={t.tournament}
            className={`flex items-center m-1 p-2 rounded-full cursor-pointer ${
              isSelected
                ? "bg-purple-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => toggleTournamentSelection(t.tournament)}
          >
            <span className="text-xl mr-2">{emojiMap[t.tournament]}</span>
            <span className="font-medium">{t.tournament}</span>
            {isLive && <span className="text-red-500 ml-2">(LIVE)</span>}

            {isPreviouslySearched && (
              <span className="badge badge-neutral ml-2">prior</span>
            )}
          </div>
        );
      })}
    </div>
  );
};

export default TournamentSelector;
