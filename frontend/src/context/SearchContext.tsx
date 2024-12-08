// SearchContext.tsx
import React, { createContext, useState, useContext } from "react";

interface Game {
  id: number;
  team_home: string;
  team_away: string;
  starts_at: string;
  tournament_name: string;
}

export interface StreamingPackage {
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
  otherPackages: StreamingPackage[];
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
  searchResultData: ResponseData | null;
  setSearchResultData: React.Dispatch<
    React.SetStateAction<ResponseData | null>
  >;

  previouslySearchedTeams: string[];
  setPreviouslySearchedTeams: React.Dispatch<React.SetStateAction<string[]>>;
  previouslySearchedTournaments: string[];
  setPreviouslySearchedTournaments: React.Dispatch<
    React.SetStateAction<string[]>
  >;
}

const SearchContext = createContext<SearchContextType>({} as SearchContextType);

interface SearchProviderProps {
  children: React.ReactNode;
}

export const SearchProvider: React.FC<SearchProviderProps> = ({ children }) => {
  const [selectedCountries, setSelectedCountries] = useState<string[]>(() => {
    const stored = localStorage.getItem("selectedCountries");
    return stored ? JSON.parse(stored) : [];
  });

  const [selectedTeams, setSelectedTeams] = useState<string[]>(() => {
    const stored = localStorage.getItem("selectedTeams");
    return stored ? JSON.parse(stored) : [];
  });

  const [selectedTournaments, setSelectedTournaments] = useState<string[]>(
    () => {
      const stored = localStorage.getItem("selectedTournaments");
      return stored ? JSON.parse(stored) : [];
    }
  );

  const [startDate, setStartDate] = useState<string | null>(() => {
    return localStorage.getItem("startDate") || null;
  });

  const [endDate, setEndDate] = useState<string | null>(() => {
    return localStorage.getItem("endDate") || null;
  });

  const [mode, setMode] = useState<"edit" | "results">(() => {
    return (localStorage.getItem("mode") as "edit" | "results") || "edit";
  });

  const [searchResultData, setSearchResultData] = useState<ResponseData | null>(
    () => {
      const stored = localStorage.getItem("searchResultData");
      return stored ? JSON.parse(stored) : null;
    }
  );

  const [previouslySearchedTeams, setPreviouslySearchedTeams] = useState<
    string[]
  >(() => {
    const stored = localStorage.getItem("previouslySearchedTeams");
    return stored ? JSON.parse(stored) : [];
  });

  const [previouslySearchedTournaments, setPreviouslySearchedTournaments] =
    useState<string[]>(() => {
      const stored = localStorage.getItem("previouslySearchedTournaments");
      return stored ? JSON.parse(stored) : [];
    });

  React.useEffect(() => {
    localStorage.setItem(
      "selectedCountries",
      JSON.stringify(selectedCountries)
    );
  }, [selectedCountries]);

  React.useEffect(() => {
    localStorage.setItem("selectedTeams", JSON.stringify(selectedTeams));
  }, [selectedTeams]);

  React.useEffect(() => {
    localStorage.setItem(
      "selectedTournaments",
      JSON.stringify(selectedTournaments)
    );
  }, [selectedTournaments]);

  React.useEffect(() => {
    if (startDate) localStorage.setItem("startDate", startDate);
    else localStorage.removeItem("startDate");
  }, [startDate]);

  React.useEffect(() => {
    if (endDate) localStorage.setItem("endDate", endDate);
    else localStorage.removeItem("endDate");
  }, [endDate]);

  React.useEffect(() => {
    localStorage.setItem("mode", mode);
  }, [mode]);

  React.useEffect(() => {
    if (searchResultData) {
      localStorage.setItem(
        "searchResultData",
        JSON.stringify(searchResultData)
      );
    } else {
      localStorage.removeItem("searchResultData");
    }
  }, [searchResultData]);

  // Store previously searched items in localStorage
  React.useEffect(() => {
    localStorage.setItem(
      "previouslySearchedTeams",
      JSON.stringify(previouslySearchedTeams)
    );
  }, [previouslySearchedTeams]);

  React.useEffect(() => {
    localStorage.setItem(
      "previouslySearchedTournaments",
      JSON.stringify(previouslySearchedTournaments)
    );
  }, [previouslySearchedTournaments]);

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
        previouslySearchedTeams,
        setPreviouslySearchedTeams,
        previouslySearchedTournaments,
        setPreviouslySearchedTournaments,
      }}
    >
      {children}
    </SearchContext.Provider>
  );
};

export const useSearchContext = () => useContext(SearchContext);
