import React from "react";
import { useSearchContext } from "../../context/SearchContext";
import { useNavigate } from "react-router-dom";
import { useBestPackages } from "../../hooks/useBestPackages";

const Sidebar: React.FC = () => {
  const {
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
    setSearchResultData,
    previouslySearchedTeams,
    setPreviouslySearchedTeams,
    previouslySearchedTournaments,
    setPreviouslySearchedTournaments,
  } = useSearchContext();

  const navigate = useNavigate();
  const { fetchBestPackages, error, loading } = useBestPackages();

  const isEditMode = mode === "edit";

  const removeTeam = (team: string) => {
    if (!isEditMode) return;
    setSelectedTeams((prev) => prev.filter((t) => t !== team));
  };

  const removeTournament = (tourney: string) => {
    if (!isEditMode) return;
    setSelectedTournaments((prev) => prev.filter((t) => t !== tourney));
  };

  const handleSearchOrEdit = async () => {
    if (isEditMode) {
      const params = {
        teams: selectedTeams.length > 0 ? selectedTeams : undefined,
        tournaments:
          selectedTournaments.length > 0 ? selectedTournaments : undefined,
        startDate: startDate || undefined,
        endDate: endDate || undefined,
      };

      const data = await fetchBestPackages(params);

      if (data) {
        setSearchResultData(data);

        // Update previously searched items
        setPreviouslySearchedTeams((prev) =>
          Array.from(new Set([...prev, ...selectedTeams]))
        );
        setPreviouslySearchedTournaments((prev) =>
          Array.from(new Set([...prev, ...selectedTournaments]))
        );

        setMode("results");
        navigate("/results");
      }
    } else {
      setMode("edit");
      navigate("/select");
    }
  };

  return (
    <div
      className="h-full overflow-hidden bg-blue-500 text-white flex flex-col p-6"
      style={{ width: "300px", flexShrink: 0 }}
    >
      <div className="flex flex-col flex-grow overflow-hidden mt-2">
        <h3 className="text-lg font-semibold mb-2">Selected Teams: </h3>
        <div className="card border border-base-300 rounded-lg overflow-y-auto shadow-lg flex-1 mb-4">
          <div className="card-body p-3">
            {selectedTeams.length === 0 ? (
              <div className="text-center">No teams selected</div>
            ) : (
              selectedTeams.map((team) => (
                <div
                  key={team}
                  className="flex items-center bg-white text-black rounded p-2 mb-1 relative"
                >
                  <div className="w-1 h-full bg-orange-500 absolute left-0 top-0 rounded-l"></div>
                  <span className="ml-3 flex-grow">{team}</span>
                  {isEditMode && (
                    <button
                      onClick={() => removeTeam(team)}
                      className="ml-2 text-red-500 hover:text-red-700"
                    >
                      X
                    </button>
                  )}
                </div>
              ))
            )}
          </div>
        </div>

        <h3 className="text-lg font-semibold mb-2">Selected Tournaments</h3>
        <div className="card border border-base-300 rounded-lg overflow-y-auto shadow-lg flex-1 mb-4">
          <div className="card-body p-3 ">
            {selectedTournaments.length === 0 ? (
              <div className="text-center">No tournaments selected</div>
            ) : (
              selectedTournaments.map((tourney) => (
                <div
                  key={tourney}
                  className="flex items-center bg-white text-black rounded p-2 mb-1 relative"
                >
                  <div className="w-1 h-full bg-orange-500 absolute left-0 top-0 rounded-l"></div>
                  <span className="ml-3 flex-grow">{tourney}</span>
                  {isEditMode && (
                    <button
                      onClick={() => removeTournament(tourney)}
                      className="ml-2 text-red-500 hover:text-red-700"
                    >
                      X
                    </button>
                  )}
                </div>
              ))
            )}
          </div>
        </div>
      </div>

      <div className="mb-7">
        <h3 className="text-lg font-semibold mb-2">Date Range</h3>

        <input
          type="date"
          className="input input-bordered input-sm w-full mb-2 text-black"
          value={startDate ?? ""}
          disabled={!isEditMode}
          onChange={(e) => setStartDate(e.target.value || null)}
        />
        <input
          type="date"
          className="input input-bordered input-sm w-full text-black"
          value={endDate ?? ""}
          disabled={!isEditMode}
          onChange={(e) => setEndDate(e.target.value || null)}
        />
      </div>

      {error && <p className="text-red-600 mb-2">{error}</p>}

      <button onClick={handleSearchOrEdit} className="btn btn-warning mt-auto">
        {isEditMode &&
        selectedTournaments.length === 0 &&
        selectedTeams.length === 0
          ? "SEARCH ALL GAMES"
          : isEditMode
          ? "SEARCH"
          : "EDIT"}
      </button>
    </div>
  );
};

export default Sidebar;
