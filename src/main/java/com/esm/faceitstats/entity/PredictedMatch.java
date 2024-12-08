package com.esm.faceitstats.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "comparison_history")
public class PredictedMatch {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "comparison_history_id_seq")
    @SequenceGenerator(name = "comparison_history_id_seq", sequenceName = "comparison_history_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Column(name = "faction_won")
    private String wonFaction;

    @Column(name = "played_map")
    private String playedMap;

    @Column(name = "match_link")
    private String matchLink;

    @Column(name = "prediction_was_right")
    private boolean wasPredictionRight;

    @Column(name = "failure_message")
    private String failureMessage;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "found_result_at", nullable = true)
    private Instant foundResultAt;
}
