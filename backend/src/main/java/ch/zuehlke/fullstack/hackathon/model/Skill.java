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
    private final Level level;
    private long points;

    @JsonCreator
    public Skill(@JsonProperty("Id") long skillId,
                 @JsonProperty("ShortDescription") String description,
                 @JsonProperty("Name") String name,
                 @JsonProperty("ExperienceText") String text) {
        this.skillId = skillId;
        this.description = description;
        this.name = name;
        this.level = Level.fromString(text);
        this.points = Level.getPoints(level);
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

    @JsonProperty("points")
    public long getPoints() {
        return points;
    }

    @JsonIgnore
    public void addPoints(long points) {
        this.points += points;
    }

    @JsonIgnore
    public Level getLevel() {
        return level;
    }
}