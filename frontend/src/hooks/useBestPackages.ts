import { useState } from "react";
import axios from "axios";

interface FetchParams {
  teams?: string[];
  tournaments?: string[];
  startDate?: string | null;
  endDate?: string | null;
}

export const useBestPackages = () => {
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const apiBaseUrl = process.env.REACT_APP_API_URL || "http://localhost:8080";

  const fetchBestPackages = async (params: FetchParams) => {
    setLoading(true);
    setError(null);

    try {
      const { data } = await axios.get(
        `${apiBaseUrl}/streaming/best-packages`,
        {
          params,
          paramsSerializer: (paramsObj) => {
            const searchParams = new URLSearchParams();
            Object.keys(paramsObj).forEach((key) => {
              const value = (paramsObj as any)[key];
              if (Array.isArray(value)) {
                value.forEach((v) => searchParams.append(key, v));
              } else if (value != null) {
                searchParams.append(key, value);
              }
            });
            return searchParams.toString();
          },
        }
      );
      setLoading(false);
      return data;
    } catch (err: any) {
      console.error("Error fetching best packages", err);
      setError(err.message || "An error occurred.");
      setLoading(false);
    }
  };

  return { fetchBestPackages, error, loading };
};
