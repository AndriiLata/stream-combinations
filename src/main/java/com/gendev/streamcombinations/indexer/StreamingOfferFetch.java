package com.gendev.streamcombinations.indexer;
import com.gendev.streamcombinations.model.StreamingOffer;
import lombok.Getter;

import java.util.*;


public class StreamingOfferFetch {
    private final Map<Integer, List<StreamingOffer>> offersByGameId = new HashMap<>();
    private final Map<Integer, List<StreamingOffer>> offersByPackageId = new HashMap<>();

    public StreamingOfferFetch(List<StreamingOffer> offersList) {
        for (StreamingOffer offer: offersList){
            offersByGameId.computeIfAbsent(offer.getId(), k -> new ArrayList<>()).add(offer);
            offersByPackageId.computeIfAbsent(offer.getStreaming_package_id(), k -> new ArrayList<>()).add(offer);
        }
    }

    public List<StreamingOffer> getOffersByGameId(int gameId) {
        return offersByGameId.getOrDefault(gameId, Collections.emptyList());
    }

    public List<StreamingOffer> getOffersByPackageId(int packageId) {
        return offersByPackageId.getOrDefault(packageId, Collections.emptyList());
    }

}
