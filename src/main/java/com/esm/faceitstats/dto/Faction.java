package com.esm.faceitstats.dto;

public enum Faction {
    FIRST_FACTION("faction1"),
    SECOND_FACTION("faction2"),
    NONE_FACTION("none");

    private final String faction;

    Faction(String faction) {
        this.faction = faction;
    }

    public String getFaction() {
        return faction;
    }
}
