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
}

const TournamentSelector: React.FC<TournamentSelectorProps> = ({
  data,
  selectedCountries,
  searchQuery,
}) => {
  const { selectedTournaments, setSelectedTournaments, mode } =
    useSearchContext();

  const getTournaments = () => {
    let filteredData = data;
    if (selectedCountries.length > 0) {
      filteredData = data.filter((item) =>
        selectedCountries.includes(item.country)
      );
    }

    // Flatten and deduplicate tournaments
    const tournaments = Array.from(
      new Set(filteredData.flatMap((item) => item.tournaments))
    );

    return searchQuery
      ? tournaments.filter((tournament) =>
          tournament.toLowerCase().includes(searchQuery.toLowerCase())
        )
      : tournaments;
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
      {tournaments.map((tournament) => (
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
        </div>
      ))}
    </div>
  );
};

export default TournamentSelector;
