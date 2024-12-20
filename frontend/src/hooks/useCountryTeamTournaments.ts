import { useState, useEffect } from "react";
import axios from "axios";

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

export const useCountryTeamTournaments = () => {
  const [data, setData] = useState<DataType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    axios
      .get("/search/country-team-tournaments")
      .then((response) => {
        setData(response.data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  return { data, loading, error };
};
