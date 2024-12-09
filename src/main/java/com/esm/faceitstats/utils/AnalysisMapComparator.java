package com.esm.faceitstats.utils;


import com.esm.faceitstats.dto.AnalysisMap;

import java.util.Comparator;

public class AnalysisMapComparator implements Comparator<AnalysisMap> {

    @Override
    public int compare(AnalysisMap o1, AnalysisMap o2) {
        return Double.compare((double) o1.getOverallPredictionFailed() / o1.getOverallPlayed(), (double) o2.getOverallPredictionFailed() / o2.getOverallPlayed());
    }
}
