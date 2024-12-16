package com.esm.faceitstats.dto;

import lombok.Getter;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class AIQuery {
    private String query;


    public AIQuery(String... params) {
        this.query = String.format("Based on the pair of maps decide the winning possibility of a team on each of the provided maps and then output all of them. Winning possibility should be ordered for each of the teams in the descending order. Probability must be between 0-1 with decimal points. If one team has a higher possibility of win on a map, another team cannot have a number which in sum with the another possibility will give more than 1 . First team: %s. Second team: %s. Output must be presented in JSON in the further manner: %s", params[0], params[1], "{\"first_faction_best_maps\":[{\"map\":\"string\",\"win_possibility\":\"number\"}],\"second_faction_best_maps\":[{\"map\":\"string\",\"win_possibility\":\"number\"}]}\n.Output just pure json with no other text provided and without mentioning its format at the beginning");
        this.query = URLEncoder.encode(this.query, StandardCharsets.UTF_8);
    }
}