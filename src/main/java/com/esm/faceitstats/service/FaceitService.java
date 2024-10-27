package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class FaceitService {

    StatisticsService statisticsService;

    UserService userService;

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @Autowired
    public void setUsernameResolverService(UserService userResolverService){
        this.userService = userResolverService;
    }


    public UserResponse getIDByUsername(String username){
        var resp = this.userService.getUserByUsername(username);
        if(resp.getUserResponse().length == 0){
            throw new EntityNotFoundException("user was not found");
        }

        return resp.getUserResponse()[0];
    }

    public ArrayList<Match> getStatsOfUserBy(String id, GetStatsParams params){
         var resp = this.statisticsService.getMatchesOfUserById(id, params.isAllMatchesRequired());
         if(resp.isEmpty()){
             throw new EntityNotFoundException("stats were not found");
         }

         ArrayList<Match> matchesWithRating = new ArrayList<>();
         if(params.isHLTVRequired()){

         }

         return resp;
    }

}
