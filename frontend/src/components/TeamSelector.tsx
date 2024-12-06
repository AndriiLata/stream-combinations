import React, { useMemo } from "react";
import { useSearchContext } from "../context/SearchContext";

interface DataType {
  country: string;
  teams: string[];
  tournaments: string[];
}

interface TeamSelectorProps {
  data: DataType[];
  selectedCountries: string[];
  searchQuery: string;
}

const TeamSelector: React.FC<TeamSelectorProps> = ({
  data,
  selectedCountries,
  searchQuery,
}) => {
  const { selectedTeams, setSelectedTeams, mode } = useSearchContext();

  const getTeams = () => {
    let filteredData = data;
    if (selectedCountries.length > 0) {
      filteredData = data.filter((item) =>
        selectedCountries.includes(item.country)
      );
    }

    // Flatten and deduplicate teams
    const teams = Array.from(
      new Set(filteredData.flatMap((item) => item.teams))
    );

    // Filter based on search query
    return searchQuery
      ? teams.filter((team) =>
          team.toLowerCase().includes(searchQuery.toLowerCase())
        )
      : teams;
  };

  const teams = getTeams();

  const emojiMap = useMemo(() => {
    const emojis = ["\u{1F455}", "\u{1F45A}"];
    const map: Record<string, string> = {};
    teams.forEach((team, index) => {
      map[team] = emojis[index % emojis.length];
    });
    return map;
  }, [teams]);

  const toggleTeamSelection = (team: string) => {
    if (mode === "results") return; // can't edit in results mode
    setSelectedTeams((prevSelected) =>
      prevSelected.includes(team)
        ? prevSelected.filter((t) => t !== team)
        : [...prevSelected, team]
    );
  };

  return (
    <div className="flex flex-wrap mt-1">
      {teams.map((team) => (
        <div
          key={team}
          className={`flex items-center m-1 p-2 rounded-full cursor-pointer ${
            selectedTeams.includes(team)
              ? "bg-blue-500 text-white"
              : "bg-gray-200 text-gray-800"
          }`}
          onClick={() => toggleTeamSelection(team)}
        >
          <span className="text-xl mr-2">{emojiMap[team]}</span>
          <span className="font-medium">{team}</span>
        </div>
      ))}
    </div>
  );
};

export default TeamSelector;
