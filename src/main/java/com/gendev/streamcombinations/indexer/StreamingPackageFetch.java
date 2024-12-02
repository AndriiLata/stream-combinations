package com.gendev.streamcombinations.indexer;
import com.gendev.streamcombinations.model.main.StreamingPackage;
import com.gendev.streamcombinations.util.FetchData;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StreamingPackageFetch {
    private final Map<Integer, StreamingPackage> packagesById = new HashMap<>();

    public StreamingPackageFetch(FetchData fetchData) {
        List<StreamingPackage> packagesList = fetchData.getStreamingPackages();
        if(packagesList != null) {
            for (StreamingPackage streamingPackage: packagesList){
                packagesById.put(streamingPackage.getId(), streamingPackage);
            }
        } else {
            System.out.println("No streaming packages found! Cant read your CSV file");
        }

    }
    public StreamingPackage getPackageById(int id) {
        return packagesById.get(id);
    }
}
