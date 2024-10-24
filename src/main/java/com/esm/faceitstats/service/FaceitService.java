package com.esm.faceitstats.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaceitService {

    StatisticsService statisticsService;

    UsernameResolverService usernameResolverService;

    @Autowired
    public void setStatisticsService(StatisticsService statisticsService){
        this.statisticsService = statisticsService;
    }

    @Autowired
    public void setUsernameResolverService(UsernameResolverService userResolverService){
        this.usernameResolverService = userResolverService;
    }



}
