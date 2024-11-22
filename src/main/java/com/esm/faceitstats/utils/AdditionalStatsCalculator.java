package com.esm.faceitstats.utils;

import com.esm.faceitstats.dto.UserStat;

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


    public static Double calculateHLTVRating(UserStat stats) {
        Integer overallRounds = AdditionalStatsCalculator.getOverallRounds(stats);
        double kpr = (double) stats.getKills() / overallRounds * KPR_RATE;
        double dpr = (double) stats.getDeaths() / overallRounds * DPR_RATE;
        double apr = (double) stats.getAssists() / overallRounds * APR_RATE;
        double impact = IMPACT_KPR_RATE * (double) stats.getKills() / overallRounds * apr - IMPACT_MAGIC_NUM;
        double kast = getKastOfMatch(stats, overallRounds);

       return KAST_RATE * kast +
               KPR_RATE * kpr -
               DPR_RATE * dpr +
               IMPACT_RATE * impact +
               ADR_RATE * stats.getAverageDamage() +
               RATING_MAGIN_NUM;
    }

    private static double getKastOfMatch(UserStat stats, Integer overallRounds) {
        return (double) (stats.getKills() + stats.getAssists() + stats.getDeaths()) / overallRounds * 100;
    }


    public static Integer getOverallRounds(UserStat stat) {
        String score = stat.getScore();
        String[] roundsPerTeams = score.split("/");
        return Integer.parseInt(roundsPerTeams[0].trim()) + Integer.parseInt(roundsPerTeams[1].trim());
    }

}
