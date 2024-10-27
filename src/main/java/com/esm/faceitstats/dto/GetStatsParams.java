package com.esm.faceitstats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetStatsParams {
    boolean allMatchesRequired;
    boolean isHLTVRequired;

    public static class GetsStatsBuilder{
        boolean allMatchesRequired;
        boolean isHLTVRequired;

        public GetsStatsBuilder withAllMatchesRequired(boolean allMatchesRequired){
            this.allMatchesRequired = allMatchesRequired;
            return this;
        }

        public GetsStatsBuilder withHLTVRequired(boolean isHLTVRequired){
            this.isHLTVRequired = isHLTVRequired;
            return this;
        }

        public GetStatsParams build(){
            return new GetStatsParams(allMatchesRequired, isHLTVRequired);
        }
    }
}
