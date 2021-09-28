package ch.zuehlke.fullstack.hackathon.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Skill {
    private final long skillId;
    private final String description;
    private final String name;
    // todo: find out how to map this
    private final long pictureId = 1;
    // todo: calculate rank via trainings
    private final Level level;
    private int rank;
    private long points = 0;

    @JsonCreator
    public Skill(@JsonProperty("Id") long skillId,
                 @JsonProperty("ShortDescription") String description,
                 @JsonProperty("Name") String name,
                 @JsonProperty("ExperienceText") Level level) {
        this.skillId = skillId;
        this.description = description;
        this.name = name;
        this.level = level;
    }

    @JsonProperty("skillId")
    public long getSkillId() {
        return skillId;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("pictureId")
    public long getPictureId() {
        return pictureId;
    }

    @JsonProperty("rank")
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @JsonIgnore
    public long getPoints() {
        return points;
    }

    @JsonIgnore
    public void addPoints(long points) {
        this.points += points;
    }
    
    public Level getLevel() {
        return level;
    }
}