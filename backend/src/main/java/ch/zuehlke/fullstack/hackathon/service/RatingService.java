package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Level;
import ch.zuehlke.fullstack.hackathon.model.Rating;
import ch.zuehlke.fullstack.hackathon.service.db.RatingRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RatingService {
    private final RatingRepository repository;


    public RatingService(RatingRepository repository) {
        this.repository = repository;
    }

    public List<Rating> getRatings(String userId) {
        return repository.getRatings(userId);
    }

    public void addRating(String userId, long skillId, Level level) {
        long additionalPoints = getPoints(level);
        repository.insertOrUpdateRating(userId, skillId, additionalPoints);
    }

    public long getPoints(Level level) {
        switch (level) {
            case BEGINNER:
                return 1;
            case PROFICIENT:
                return 10;
            case EXPERT:
                return 50;
            default:
                return 0;
        }
    }
}