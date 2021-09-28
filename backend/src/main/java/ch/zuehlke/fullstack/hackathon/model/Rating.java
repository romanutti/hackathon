package ch.zuehlke.fullstack.hackathon.model;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Rating {

    private final String userId;
    private final long skillId;
    private final long points;

    @PersistenceConstructor
    public Rating(String userId, long skillId, long points) {
        this.userId = userId;
        this.skillId = skillId;
        this.points = points;
    }


    public String getUserId() {
        return userId;
    }

    public long getPoints() {
        return points;
    }

    public long getSkillId() {
        return skillId;
    }
}