package com.esm.faceitstats.utils;

import com.esm.faceitstats.dto.MapAverageStats;
import com.esm.faceitstats.dto.UserStat;

public class AverageCalculationsHelper {
    public static MapAverageStats add(MapAverageStats a, MapAverageStats b) {
        var matchStats = new MapAverageStats();
        if(a == null || b == null) {
            throw new IllegalArgumentException("MapAverageStats cannot be null");
        }

        if(!a.getMap().equalsIgnoreCase(b.getMap())){
            throw new IllegalArgumentException("Maps do not match");
        }

        matchStats.setWins(a.getWins() + b.getWins());
        matchStats.setMap(b.getMap());
        matchStats.setKd(a.getKd() + b.getKd());
        matchStats.setAces(a.getAces() + b.getAces());
        matchStats.setKr(a.getKr() + b.getKr());
        matchStats.setAdr(a.getAdr() + b.getAdr());
        matchStats.setAssists(a.getAssists() + b.getAssists());
        matchStats.setDeaths(a.getDeaths() + b.getDeaths());
        matchStats.setAces(a.getAces() + b.getAces());
        matchStats.setMvps(a.getMvps() + b.getMvps());
        matchStats.setTrippleKills(a.getTrippleKills() + b.getTrippleKills());
        matchStats.setQuadroKills(a.getQuadroKills() + b.getQuadroKills());
        matchStats.setHltvRating(a.getHltvRating() + b.getHltvRating());
        matchStats.setHeadshotPercentage(a.getHeadshotPercentage() + b.getHeadshotPercentage());
        matchStats.setKills(a.getKills() + b.getKills());

        return matchStats;
    }

    public static MapAverageStats averageMapStatsFromUserStats(String map, UserStat userStat, boolean isWin){
        return new MapAverageStats(

                map,
                userStat.getKd(),
                userStat.getAces(),
                userStat.getKr(),
                userStat.getQuads(),
                userStat.getTriples(),
                userStat.getDoubleKills(),
                userStat.getMvps(),
                userStat.getDeaths(),
                userStat.getKills(),
                userStat.getAssists(),
                userStat.getHeadshotPercentage(),
                userStat.getHltvRating(),
                userStat.getAverageDamage(),
                isWin ? 1 : 0
        );
    }
}
