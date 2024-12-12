package com.esm.faceitstats;

import com.esm.faceitstats.dto.UserStat;
import com.esm.faceitstats.utils.AdditionalStatsCalculator;
import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class AdditionalStatsCalculatorBenchmarkTest {

    @Test
    public void
    launchBenchmark() throws Exception {

        Options opt = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode (Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(2)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(10)
                .threads(10)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();

        new Runner(opt).run();
    }

    @Benchmark
    public void benchmarkAdditionalStatsCalculator() {
        for(int i = 0; i < 1000; i++){
            AdditionalStatsCalculator.calculateHLTVRating(generateUserStat());
        }
    }

    private UserStat generateUserStat() {
        return UserStat.builder()
                .kd(new Random().nextDouble(10))
                .aces(new Random().nextInt(5))
                .kr(new Random().nextInt(5))
                .quads(new Random().nextInt(5))
                .triples(new Random().nextInt(5))
                .mvps(new Random().nextInt(13))
                .score("13 / 0")
                .deaths(new Random().nextInt(13))
                .kills(new Random().nextInt(65))
                .assists(new Random().nextInt(50))
                .headshotPercentage(new Random().nextInt(100))
                .map(getRandomMap())
                .hltvRating(new Random().nextDouble(2))
                .averageDamage(new Random().nextDouble(500))
                .build();
    }

    private String getRandomMap(){
        List<String> map = List.of("de_dust2", "de_mirage", "de_inferno", "de_ancient", "de_vertigo", "de_train", "de_cobblestone", "de_overpass");
        return map.get(new Random().nextInt(map.size()));
    }
}
