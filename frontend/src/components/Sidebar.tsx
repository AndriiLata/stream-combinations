import React from "react";
import { useSearchContext } from "../context/SearchContext";
import { useNavigate } from "react-router-dom";
import axios from "axios";

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
  } = useSearchContext();
  const navigate = useNavigate();

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
      // Perform GET request
      console.log("Performing search...");
      try {
        const params = {
          teams: selectedTeams.length > 0 ? selectedTeams : undefined,
          tournaments:
            selectedTournaments.length > 0 ? selectedTournaments : undefined,
          startDate: startDate || undefined,
          endDate: endDate || undefined,
        };
        console.log("Search params:", params);
        const { data } = await axios.get("/streaming/cheapest-packages", {
          params: {
            teams: selectedTeams,
          },
          paramsSerializer: (params) => {
            const searchParams = new URLSearchParams();
            Object.keys(params).forEach((key) => {
              const value = params[key];
              if (Array.isArray(value)) {
                value.forEach((v) => searchParams.append(key, v));
              } else if (value != null) {
                searchParams.append(key, value);
              }
            });
            return searchParams.toString();
          },
        });
        console.log("Cheapest packages:", data);
        // Store the data somewhere if needed, then navigate
        setMode("results");
        navigate("/results");
      } catch (error) {
        console.error("Error fetching cheapest packages", error);
      }
    } else {
      // If currently in results mode, "EDIT" was clicked:
      setMode("edit");
      navigate("/select");
    }
  };

  return (
    <div
      className=" h-full overflow-hidden bg-blue-500 text-white flex flex-col p-6"
      style={{ width: "300px", flexShrink: 0 }}
    >
      <h2 className="text-2xl font-bold mb-6">combinationCHECK</h2>
      <div className="flex flex-col flex-grow overflow-hidden mt-4">
        <h3 className="text-lg font-semibold mb-2">Selected Teams: </h3>
        <div className="card border border-base-300 rounded-lg overflow-y-auto shadow-lg flex-1 mb-4">
          <div className="card-body p-3">
            {selectedTeams.map((team) => (
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
            ))}
          </div>
        </div>

        <h3 className="text-lg font-semibold mb-2">Selected Tournaments</h3>
        <div className="card border border-base-300 rounded-lg overflow-y-auto shadow-lg flex-1 mb-4">
          <div className="card-body p-3">
            {selectedTournaments.map((tourney) => (
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
            ))}
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

      <button onClick={handleSearchOrEdit} className="btn btn-primary mt-auto">
        {isEditMode ? "SEARCH" : "EDIT"}
      </button>
    </div>
  );
};

export default Sidebar;
