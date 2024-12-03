package com.gendev.streamcombinations.util;

import com.google.ortools.Loader;

public class ORToolsInitializer {
    public static void initialize() {
        Loader.loadNativeLibraries(); // Loads the native libraries
    }
}

