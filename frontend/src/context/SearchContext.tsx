import React, { createContext, useState, useContext } from "react";

interface Game {
  id: number;
  team_home: string;
  team_away: string;
  starts_at: string;
  tournament_name: string;
}

interface StreamingPackage {
  id: number;
  name: string;
  monthly_price_cents: number;
  monthly_price_yearly_subscription_in_cents: number;
}

interface OffersInfo {
  live: boolean;
  highlights: boolean;
}

interface ResponseData {
  gamesByTournament: {
    [tournament: string]: {
      [gameId: string]: Game;
    };
  };
  streamingPackages: StreamingPackage[];
  offersToPackageID: {
    [packageId: string]: {
      [gameId: string]: OffersInfo;
    };
  };
}

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

  // New state to hold the search results from the GET request
  searchResultData: ResponseData | null;
  setSearchResultData: React.Dispatch<
    React.SetStateAction<ResponseData | null>
  >;
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
  const [searchResultData, setSearchResultData] = useState<ResponseData | null>(
    null
  );

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
        searchResultData,
        setSearchResultData,
      }}
    >
      {children}
    </SearchContext.Provider>
  );
};

export const useSearchContext = () => useContext(SearchContext);
