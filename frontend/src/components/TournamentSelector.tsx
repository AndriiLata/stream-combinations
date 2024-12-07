import React, { useMemo } from "react";
import { useSearchContext } from "../context/SearchContext";

interface DataType {
  country: string;
  teams: string[];
  tournaments: string[];
}

interface TournamentSelectorProps {
  data: DataType[];
  selectedCountries: string[];
  searchQuery: string;
  showOnlyPrior: boolean; // new prop
}

const TournamentSelector: React.FC<TournamentSelectorProps> = ({
  data,
  selectedCountries,
  searchQuery,
  showOnlyPrior,
}) => {
  const {
    selectedTournaments,
    setSelectedTournaments,
    mode,
    previouslySearchedTournaments,
  } = useSearchContext();

  const getTournaments = () => {
    let filteredData = data;
    if (selectedCountries.length > 0) {
      filteredData = data.filter((item) =>
        selectedCountries.includes(item.country)
      );
    }

    // Flatten and deduplicate tournaments
    let tournaments = Array.from(
      new Set(filteredData.flatMap((item) => item.tournaments))
    );

    // Filter based on search query
    if (searchQuery) {
      tournaments = tournaments.filter((tournament) =>
        tournament.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }

    // If showOnlyPrior is active, show only previously searched tournaments
    if (showOnlyPrior) {
      tournaments = tournaments.filter((t) =>
        previouslySearchedTournaments.includes(t)
      );
    }

    return tournaments;
  };

  const tournaments = getTournaments();

  const emojiMap = useMemo(() => {
    const emojis = ["🏆", "🎉", "⚽", "🏅"];
    const map: Record<string, string> = {};
    tournaments.forEach((tournament, index) => {
      map[tournament] = emojis[index % emojis.length];
    });
    return map;
  }, [tournaments]);

  const toggleTournamentSelection = (tournament: string) => {
    if (mode === "results") return; // can't edit in results mode
    setSelectedTournaments((prevSelected) =>
      prevSelected.includes(tournament)
        ? prevSelected.filter((t) => t !== tournament)
        : [...prevSelected, tournament]
    );
  };

  return (
    <div className="flex flex-wrap mt-1">
      {tournaments.map((tournament) => {
        const isPreviouslySearched =
          previouslySearchedTournaments.includes(tournament);

        return (
          <div
            key={tournament}
            className={`flex items-center m-1 p-2 rounded-full cursor-pointer ${
              selectedTournaments.includes(tournament)
                ? "bg-purple-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => toggleTournamentSelection(tournament)}
          >
            <span className="text-xl mr-2">{emojiMap[tournament]}</span>
            <span className="font-medium">{tournament}</span>
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
