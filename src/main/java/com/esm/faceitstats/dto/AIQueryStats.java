package com.esm.faceitstats.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AIQueryStats {
    private String map;
    private double kd;
    private double kr;
    private int wins;
    private int amount;
}
