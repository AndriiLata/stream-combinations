import React, { useEffect, useRef, useState } from "react";
import { useSearchContext } from "../../context/SearchContext";

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

interface CountrySelectorProps {
  data: DataType[];
}

const CountrySelector: React.FC<CountrySelectorProps> = ({ data }) => {
  const { selectedCountries, setSelectedCountries, mode } = useSearchContext();
  const countries = Array.from(new Set(data.map((item) => item.country)));

  const scrollContainerRef = useRef<HTMLDivElement>(null);
  const [canScrollLeft, setCanScrollLeft] = useState(false);
  const [canScrollRight, setCanScrollRight] = useState(false);

  useEffect(() => {
    const scrollContainer = scrollContainerRef.current;
    const updateScrollButtons = () => {
      if (scrollContainer) {
        const { scrollLeft, scrollWidth, clientWidth } = scrollContainer;
        setCanScrollLeft(scrollLeft > 0);
        setCanScrollRight(scrollLeft + clientWidth < scrollWidth);
      }
    };
    if (scrollContainer) {
      scrollContainer.addEventListener("scroll", updateScrollButtons);
      updateScrollButtons();
    }
    return () => {
      if (scrollContainer) {
        scrollContainer.removeEventListener("scroll", updateScrollButtons);
      }
    };
  }, [countries.length]);

  const toggleCountrySelection = (country: string) => {
    if (mode === "results") return; // can't edit in results mode
    if (selectedCountries.includes(country)) {
      setSelectedCountries(selectedCountries.filter((c) => c !== country));
    } else {
      setSelectedCountries([...selectedCountries, country]);
    }
  };

  const scrollLeft = () => {
    if (scrollContainerRef.current) {
      scrollContainerRef.current.scrollBy({
        left: -800,
        behavior: "smooth",
      });
    }
  };

  const scrollRight = () => {
    if (scrollContainerRef.current) {
      scrollContainerRef.current.scrollBy({
        left: 800,
        behavior: "smooth",
      });
    }
  };

  return (
    <div className="relative flex items-center">
      <button
        className={`absolute left-0 z-10 p-2 bg-gray-200 rounded-full shadow-md hover:bg-gray-300 focus:outline-none ${
          !canScrollLeft ? "opacity-50 cursor-not-allowed" : ""
        }`}
        onClick={scrollLeft}
        disabled={!canScrollLeft}
        aria-label="Scroll Left"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6 text-gray-800"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M15 19l-7-7 7-7"
          />
        </svg>
      </button>

      <div
        className="flex overflow-x-auto whitespace-nowrap mx-8"
        ref={scrollContainerRef}
      >
        {countries.map((country) => (
          <div
            key={country}
            className={`flex-shrink-0 flex items-center m-1 p-2 rounded-full cursor-pointer transition-colors duration-200 ${
              selectedCountries.includes(country)
                ? "bg-blue-500 text-white"
                : "bg-gray-200 text-gray-800"
            }`}
            onClick={() => toggleCountrySelection(country)}
          >
            <span className="text-xl mr-2">{getFlagEmoji(country)}</span>
            <span className="font-medium">{country}</span>
          </div>
        ))}
      </div>

      <button
        className={`absolute right-0 z-10 p-2 bg-gray-200 rounded-full shadow-md hover:bg-gray-300 focus:outline-none ${
          !canScrollRight ? "opacity-50 cursor-not-allowed" : ""
        }`}
        onClick={scrollRight}
        disabled={!canScrollRight}
        aria-label="Scroll Right"
      >
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-6 w-6 text-gray-800"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeWidth={2}
            d="M9 5l7 7-7 7"
          />
        </svg>
      </button>
    </div>
  );
};

function getFlagEmoji(countryName: string): string {
  const countryCodeMap: { [key: string]: string } = {
    Canada: "CA",
    Germany: "DE",
    France: "FR",
    Spain: "ES",
    Italy: "IT",
    "United Kingdom": "GB",
    "United States": "US",
    "Czech Republic": "CZ",
    Monaco: "MC",
    Cyprus: "CY",
    Greece: "GR",
    Azerbaijan: "AZ",
    Latvia: "LV",
    Moldova: "MD",
    Kosovo: "XK",
    Lithuania: "LT",
    Montenegro: "ME",
    Gibraltar: "GI",
    Malta: "MT",
    Liechtenstein: "LI",
    "French Guiana": "GF",
    "French Polynesia": "PF",
    "San Marino": "SM",
    "North Macedonia": "MK",
    Guadeloupe: "GP",
    China: "CN",
    Japan: "JP",
    "South Korea": "KR",
    Bosnia: "BA",
    Hungary: "HU",
    Israel: "IL",
    Ireland: "IE",
    Bulgaria: "BG",
    England: "GB",
    Croatia: "HR",
    "Northern Ireland": "GB",
    Luxembourg: "LU",
    Iceland: "IS",
    Kazakhstan: "KZ",
    Austria: "AT",
    Sweden: "SE",
    Scotland: "GB",
    Finland: "FI",
    Denmark: "DK",
    Wales: "GB",
    Georgia: "GE",
    Ukraine: "UA",
    Armenia: "AM",
    Netherlands: "NL",
    Estonia: "EE",
    Portugal: "PT",
    Serbia: "RS",
    Slovakia: "SK",
    "New Caledonia": "NC",
    Albania: "AL",
    "Saudi Arabia": "SA",
    Poland: "PL",
    Romania: "RO",
    Martinique: "MQ",
    USA: "US",
    Belarus: "BY",
    Belgium: "BE",
    Slovenia: "SI",
    Andorra: "AD",
    Switzerland: "CH",
    Norway: "NO",
    Turkey: "TR",
    "Faroe Islands": "FO",
    "Bosnia and Herzegovina": "BA",
  };

  const countryCode = countryCodeMap[countryName];
  if (!countryCode) return "🏳️";
  return countryCodeToFlagEmoji(countryCode);
}

function countryCodeToFlagEmoji(countryCode: string): string {
  const codePoints = countryCode
    .toUpperCase()
    .split("")
    .map((char) => 127397 + char.charCodeAt(0));
  return String.fromCodePoint(...codePoints);
}

export default CountrySelector;
