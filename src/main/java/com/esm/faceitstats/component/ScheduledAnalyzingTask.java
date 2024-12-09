package com.esm.faceitstats.component;


import com.esm.faceitstats.service.PredictAnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@EnableAsync
public class ScheduledAnalyzingTask {
    private PredictAnalyzerService predictAnalyzerService;

    @Autowired
    public void setPredictAnalyzerService(PredictAnalyzerService predictAnalyzerService) {
        this.predictAnalyzerService = predictAnalyzerService;
    }

    @Scheduled(cron = "0 00 00 28-31 * ?")
    //@Scheduled(fixedRate = 50000)
    @Async
    public void analyze() {

        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {
            this.predictAnalyzerService.doMonthlyAnalyzes();
        }
    }
}
