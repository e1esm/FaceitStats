package com.esm.faceitstats.utils;



public class AdditionalStatsCalculator {
    private static final double IMPACT_MAGIC_NUM = 0.41;
    private static final double RATING_MAGIN_NUM = 0.1587;
    private static final double KAST_RATE = 0.0073;
    private static final double KPR_RATE = 0.3591;
    private static final double DPR_RATE = 0.5239;
    private static final double IMPACT_RATE = 0.2732;
    private static final double ADR_RATE = 0.0032;
    private static final double IMPACT_KPR_RATE = 2.13;
    private static final double APR_RATE = 0.42;

/*
    public static MatchStatsEnriched calculateAdditionalStats(MatchStatsEnriched matchHLTVStat) {
        Integer overallRounds = AdditionalStatsCalculator.getOverallRounds(matchHLTVStat);
        double kpr = (double) matchHLTVStat.getMatchStat().getKills() / overallRounds * KPR_RATE;
        double dpr = (double) matchHLTVStat.getMatchStat().getDeaths() / overallRounds * DPR_RATE;
        double impact = getImpactOfMatch(matchHLTVStat, kpr, overallRounds);

       return matchHLTVStat;

    }

    private static double getImpactOfMatch(MatchHLTVStat matchHLTVStat, double kpr, Integer overallRounds) {
        return IMPACT_KPR_RATE * kpr * matchHLTVStat.getMatchStat().getAssists() / (double) overallRounds - IMPACT_MAGIC_NUM;
    }

    private static double getCastOfMatch(MatchHLTVStat matchHLTVStat, Integer overallRounds) {
        Integer totalRoundsKillHappened = 0;
        Integer totalRoundsAlive = overallRounds - matchHLTVStat.getMatchStat().getDeaths();
        return 0;
    }


    private static Integer getOverallRounds(MatchHLTVStat matchHLTVStat) {
        String score = matchHLTVStat.getMatchStat().getScore();
        String[] roundsPerTeams = score.split("/");
        return Integer.parseInt(roundsPerTeams[0].trim()) + Integer.parseInt(roundsPerTeams[1].trim());
    }

 */
}
