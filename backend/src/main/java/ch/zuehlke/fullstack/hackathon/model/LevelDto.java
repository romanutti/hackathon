package ch.zuehlke.fullstack.hackathon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LevelDto {
    private final Level level;

    @JsonCreator
    LevelDto(@JsonProperty("level") String string) {
        this.level = Level.fromString(string);
    }
    
    public Level getLevel() {
        return level;
    }
}