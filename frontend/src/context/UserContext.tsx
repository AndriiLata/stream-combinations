import React, { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";
import { StreamingPackage } from "./SearchContext";

export interface User {
  balance: number;
  boughtPackageIds: number[];
}

interface UserContextType {
  user: User | null;
  boughtPackages: StreamingPackage[];
  refreshUser: () => void;
  buyPackage: (packageId: number, costInCents: number) => Promise<void>;
  topUp: (amount: number) => Promise<void>;
}

const UserContext = createContext<UserContextType>({} as UserContextType);

export const UserProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [user, setUser] = useState<User | null>(null);
  const [boughtPackages, setBoughtPackages] = useState<StreamingPackage[]>([]);
  const apiBaseUrl = process.env.REACT_APP_API_URL || "http://localhost:8080";

  const refreshUser = async () => {
    const { data } = await axios.get(`${apiBaseUrl}/user/status`);
    setUser(data);
  };

  const buyPackage = async (packageId: number, costInCents: number) => {
    await axios.post(`${apiBaseUrl}/user/buy`, null, {
      params: { packageId, costInCents },
    });
    await refreshUser();
  };

  const topUp = async (amount: number) => {
    await axios.post(`${apiBaseUrl}/user/top-up`, null, { params: { amount } });
    await refreshUser();
  };

  useEffect(() => {
    refreshUser();
  }, []);

  useEffect(() => {
    const fetchBoughtPackages = async () => {
      if (user && user.boughtPackageIds.length > 0) {
        const { data } = await axios.get(
          `${apiBaseUrl}/streaming/packages-by-ids`,
          {
            params: { ids: user.boughtPackageIds },
            paramsSerializer: (params) => {
              const searchParams = new URLSearchParams();
              params.ids.forEach((id: number) => {
                searchParams.append("ids", id.toString());
              });
              return searchParams.toString();
            },
          }
        );
        setBoughtPackages(data);
      } else {
        setBoughtPackages([]);
      }
    };
    fetchBoughtPackages();
  }, [user]);

  return (
    <UserContext.Provider
      value={{ user, boughtPackages, refreshUser, buyPackage, topUp }}
    >
      {children}
    </UserContext.Provider>
  );
};

export const useUserContext = () => useContext(UserContext);
