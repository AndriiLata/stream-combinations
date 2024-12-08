import React, { useEffect, useState } from "react";
import axios from "axios";
import CountrySelector from "../components/CountrySelector";
import TeamSelector from "../components/TeamSelector";
import TournamentSelector from "../components/TournamentSelector";
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

const SelectionPage: React.FC = () => {
  const [data, setData] = useState<DataType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const {
    selectedCountries,
    setMode,
    previouslySearchedTeams,
    previouslySearchedTournaments,
  } = useSearchContext();
  const [searchQuery, setSearchQuery] = useState("");

  const [showOnlyPriorTeams, setShowOnlyPriorTeams] = useState(false);

  // New states for filtering tournaments by status
  const [showFinished, setShowFinished] = useState(false);
  const [showLive, setShowLive] = useState(false);
  const [showUpcoming, setShowUpcoming] = useState(false);

  useEffect(() => {
    setMode("edit"); // ensure we are in edit mode when on this page
    axios
      .get("/search/country-team-tournaments")
      .then((response) => {
        setData(response.data);
        setLoading(false);
      })
      .catch((error) => {
        setError(error.message);
        setLoading(false);
      });
  }, [setMode]);

  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  const hasPreviouslySearchedTeams = previouslySearchedTeams.length > 0;
  const hasPreviouslySearchedTournaments =
    previouslySearchedTournaments.length > 0;

  return (
    <div className="p-4 flex flex-col h-full overflow-hidden">
      {/*Country input*/}
      <CountrySelector data={data} />

      {/* Search input */}
      <label className="input input-bordered flex items-center gap-2 mt-5 flex-shrink-0">
        <input
          type="text"
          className="grow"
          placeholder="Search for teams or tournaments..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 16 16"
          fill="currentColor"
          className="h-4 w-4 opacity-70"
        >
          <path
            fillRule="evenodd"
            d="M9.965 11.026a5 5 0 1 1 1.06-1.06l2.755 2.754a.75.75 0 1 1-1.06 1.06l-2.755-2.754ZM10.5 7a3.5 3.5 0 1 1-7 0 3.5 3.5 0 0 1 7 0Z"
            clipRule="evenodd"
          />
        </svg>
      </label>

      <div className="flex flex-col flex-grow overflow-hidden mt-5">
        {/* Select the teams */}
        <div className="flex justify-between items-center mb-2">
          <h3 className="text-lg font-semibold">Select Teams: </h3>
          <div className="flex items-center">
            <span className="mr-2">Show Prior</span>
            <input
              type="checkbox"
              className="toggle"
              disabled={
                !hasPreviouslySearchedTeams && !hasPreviouslySearchedTournaments
              }
              checked={showOnlyPriorTeams}
              onChange={() => setShowOnlyPriorTeams(!showOnlyPriorTeams)}
            />
          </div>
        </div>
        <div className="card border border-base-300 rounded-lg overflow-y-auto flex-1 bg-slate-100">
          <div className="card-body p-3">
            <TeamSelector
              data={data}
              selectedCountries={selectedCountries}
              searchQuery={searchQuery}
              showOnlyPrior={showOnlyPriorTeams}
            />
          </div>
        </div>

        {/* Select the tournaments */}
        <div className="flex justify-between items-center mb-2 mt-6">
          <h3 className="text-lg font-semibold">Select Tournaments: </h3>
          <div className="flex items-center space-x-4">
            {/* Finished checkbox */}
            <label className="flex items-center space-x-2 cursor-pointer">
              <input
                type="checkbox"
                checked={showFinished}
                onChange={() => setShowFinished(!showFinished)}
                className="checkbox"
              />
              <span className="label-text">Finished</span>
            </label>

            {/* Live checkbox */}
            <label className="flex items-center space-x-2 cursor-pointer">
              <input
                type="checkbox"
                checked={showLive}
                onChange={() => setShowLive(!showLive)}
                className="checkbox"
              />
              <span className="label-text">Live</span>
            </label>

            {/* Upcoming checkbox */}
            <label className="flex items-center space-x-2 cursor-pointer">
              <input
                type="checkbox"
                checked={showUpcoming}
                onChange={() => setShowUpcoming(!showUpcoming)}
                className="checkbox"
              />
              <span className="label-text">Upcoming</span>
            </label>
          </div>
        </div>

        <div className="card border border-base-300 rounded-lg overflow-y-auto flex-1 bg-slate-100">
          <div className="card-body p-3">
            <TournamentSelector
              data={data}
              selectedCountries={selectedCountries}
              searchQuery={searchQuery}
              showOnlyPrior={showOnlyPriorTeams}
              showFinished={showFinished}
              showLive={showLive}
              showUpcoming={showUpcoming}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default SelectionPage;
