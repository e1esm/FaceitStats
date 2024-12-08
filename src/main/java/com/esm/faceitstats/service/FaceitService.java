package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.*;
import com.esm.faceitstats.utils.AverageCalculationsHelper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Stream;

@Service
public class FaceitService {

    StatisticsService statisticsService;
    FaceitUserService faceitUserService;

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @Autowired
    public void setUsernameResolverService(FaceitUserService userResolverService) {
        this.faceitUserService = userResolverService;
    }


    public UserResponse getIDByUsername(String username) {
        var resp = this.faceitUserService.getUserByUsername(username);
        if (resp.getUserResponse().length == 0) {
            throw new EntityNotFoundException("user was not found");
        }

        return resp.getUserResponse()[0];
    }

    public UserAverageStats getAverageStatsOfUserBy(String id, GetStatsParams params) {
        var matches = getMatchesOfUserByID(id, params);
        return this.statisticsService.getAverageStatsBasedOnMatches(matches);
    }

    public ArrayList<Match> getMatchesOfUserByID(String ID, GetStatsParams params) {
        var resp = this.statisticsService.getMatchesOfUserById(ID, params.isAllMatchesRequired(), params.isHLTVRequired());
        if (resp.isEmpty()) {
            throw new EntityNotFoundException("stats were not found");
        }

        return resp;
    }

    public List<MapAverageStats> getAverageStatsByFaction(LobbyResponse.Faction faction){
        List<Match> factionMatches = new ArrayList<>();
        for(LobbyResponse.User user : faction.getUsers()){
            var userMatches = this.statisticsService.getMatchesOfUserById(user.getId(), false, false);
            factionMatches.addAll(userMatches);
        }
        
        return getAverageStatsByMap(factionMatches);
    }

    public List<MapAverageStats> getAverageStatsByMap(List<Match> matches){
        Map<String, Integer> mapOccurrencies = new HashMap<>(matches.size());
        matches.forEach(match ->{
            mapOccurrencies.merge(match.getUserStat().getMap(), 1, Integer::sum);
        });

        Map<String, MapAverageStats> mapAverageStats = new HashMap<>();
        mapOccurrencies.
                entrySet().
                stream().
                sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEach((entry) ->{
                    matches.forEach((match) ->{
                        if(match.getUserStat().getMap() != null && match.getUserStat().getMap().equalsIgnoreCase(entry.getKey())){
                            mapAverageStats.merge(entry.getKey(), AverageCalculationsHelper.averageMapStatsFromUserStats(entry.getKey(), match.getUserStat(), match.isWin()), AverageCalculationsHelper::add);
                        }
                    });
                });


        return hashStatsToList(mapAverageStats, mapOccurrencies);
    }


    public List<MapAverageStats> getAverageStatsByMap(String id, GetStatsParams params) {
        var matches = getMatchesOfUserByID(id, params);
        if (matches.isEmpty()) {
            throw new EntityNotFoundException("stats were not found");
        }

        return getAverageStatsByMap(matches);
    }


    private List<MapAverageStats> hashStatsToList(Map<String, MapAverageStats> map, Map<String, Integer> mapOccurrencies){
        List<MapAverageStats> mapAverageStatsList = new ArrayList<>();

        map.entrySet().stream().sorted(Map.Entry.comparingByKey(Comparator.reverseOrder())).forEach((entry) -> {
            entry.getValue().setAverageOf(mapOccurrencies.get(entry.getKey()));
            MapAverageStats curr = entry.getValue();
            curr.setAmount(mapOccurrencies.get(entry.getKey()));
            mapAverageStatsList.add(curr);
        });

        return mapAverageStatsList;
    }
}