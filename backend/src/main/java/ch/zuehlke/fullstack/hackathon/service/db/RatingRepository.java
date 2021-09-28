package ch.zuehlke.fullstack.hackathon.service.db;

import ch.zuehlke.fullstack.hackathon.model.Rating;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingRepository {
    
    private final MongoTemplate mongoTemplate;
    
    public RatingRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    public List<Rating> getRatings(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Rating.class);
    }

    public void insertOrUpdateRating(String userId, long skillId, long additionalPoints) {
        Query query = new Query(Criteria
                .where("userId").is(userId)
                .and("skillId").is(skillId));
        Update update = new Update();
        update.inc("points", additionalPoints);

        List<Rating> ratings = mongoTemplate.find(query, Rating.class);

        if (ratings.size() > 0) {
            mongoTemplate.updateFirst(query, update, Rating.class);
        } else {
            mongoTemplate.save(new Rating(userId, skillId, additionalPoints));
        }
    }
}