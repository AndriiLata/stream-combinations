package com.gendev.streamcombinations.indexer;
import com.gendev.streamcombinations.model.main.StreamingOffer;
import com.gendev.streamcombinations.util.FetchData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StreamingOfferFetch {
    private final Map<Integer, List<StreamingOffer>> offersByGameId = new HashMap<>();
    private final Map<Integer, List<StreamingOffer>> offersByPackageId = new HashMap<>();

    public StreamingOfferFetch(FetchData fetchData) {
        List<StreamingOffer> offersList = fetchData.getStreamingOffers();
        if(offersList != null) {
            for (StreamingOffer offer : offersList) {
                offersByGameId.computeIfAbsent(offer.getId(), k -> new ArrayList<>()).add(offer);
                offersByPackageId.computeIfAbsent(offer.getStreaming_package_id(), k -> new ArrayList<>()).add(offer);
            }
        } else {
            System.out.println("No streaming offers found! Cant read your CSV file");
        }
    }

    public List<StreamingOffer> getOffersByGameId(int gameId) {
        return offersByGameId.getOrDefault(gameId, Collections.emptyList());
    }

    public List<StreamingOffer> getOffersByPackageId(int packageId) {
        return offersByPackageId.getOrDefault(packageId, Collections.emptyList());
    }

}
