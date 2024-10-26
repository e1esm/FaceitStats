package com.esm.faceitstats.service;

import com.esm.faceitstats.dto.*;
import com.esm.faceitstats.exception.UserNotFoundException;
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
            throw new UserNotFoundException("user was not found");
        }

        return resp.getUserResponse()[0];
    }

    public ArrayList<Match> getStatsOfUserBy(String id, GetStatsParams params){
        return this.statisticsService.getStatisticsOfUserID(id, params.isAllMatchesRequired());
    }

}
