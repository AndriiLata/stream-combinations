import React, { createContext, useState, useContext } from "react";

interface SearchContextType {
  selectedCountries: string[];
  setSelectedCountries: React.Dispatch<React.SetStateAction<string[]>>;
  selectedTeams: string[];
  setSelectedTeams: React.Dispatch<React.SetStateAction<string[]>>;
  selectedTournaments: string[];
  setSelectedTournaments: React.Dispatch<React.SetStateAction<string[]>>;
  startDate: string | null;
  setStartDate: React.Dispatch<React.SetStateAction<string | null>>;
  endDate: string | null;
  setEndDate: React.Dispatch<React.SetStateAction<string | null>>;
  mode: "edit" | "results";
  setMode: React.Dispatch<React.SetStateAction<"edit" | "results">>;
}

const SearchContext = createContext<SearchContextType>({} as SearchContextType);

interface SearchProviderProps {
  children: React.ReactNode;
}

export const SearchProvider: React.FC<SearchProviderProps> = ({ children }) => {
  const [selectedCountries, setSelectedCountries] = useState<string[]>([]);
  const [selectedTeams, setSelectedTeams] = useState<string[]>([]);
  const [selectedTournaments, setSelectedTournaments] = useState<string[]>([]);
  const [startDate, setStartDate] = useState<string | null>(null);
  const [endDate, setEndDate] = useState<string | null>(null);
  const [mode, setMode] = useState<"edit" | "results">("edit");

  return (
    <SearchContext.Provider
      value={{
        selectedCountries,
        setSelectedCountries,
        selectedTeams,
        setSelectedTeams,
        selectedTournaments,
        setSelectedTournaments,
        startDate,
        setStartDate,
        endDate,
        setEndDate,
        mode,
        setMode,
      }}
    >
      {children}
    </SearchContext.Provider>
  );
};

export const useSearchContext = () => useContext(SearchContext);
