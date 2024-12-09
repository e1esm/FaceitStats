package com.esm.faceitstats.utils;

import com.esm.faceitstats.dto.AnalysisUser;

import java.util.Comparator;

public class AnalysisComparator implements Comparator<AnalysisUser> {
    @Override
    public int compare(AnalysisUser o1, AnalysisUser o2) {
       return Double.compare((double) o1.getOverallPredictionsFailed() / o1.getOverallMatchesPlayed(), (double) o2.getOverallPredictionsFailed() / o2.getOverallMatchesPlayed());
    }
}
