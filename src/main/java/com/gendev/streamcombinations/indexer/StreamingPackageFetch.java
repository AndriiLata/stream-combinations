package com.gendev.streamcombinations.indexer;
import com.gendev.streamcombinations.model.StreamingPackage;
import lombok.Getter;

import java.util.*;


public class StreamingPackageFetch {
    private final Map<Integer, StreamingPackage> packagesById = new HashMap<>();

    public StreamingPackageFetch(List<StreamingPackage> packagesList) {
        for (StreamingPackage streamingPackage: packagesList){
            packagesById.put(streamingPackage.getId(), streamingPackage);
        }
    }
    public StreamingPackage getPackageById(int id) {
        return packagesById.get(id);
    }
}
