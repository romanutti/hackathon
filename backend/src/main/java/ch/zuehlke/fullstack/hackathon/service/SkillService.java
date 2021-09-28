package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Rating;
import ch.zuehlke.fullstack.hackathon.model.Skill;
import ch.zuehlke.fullstack.hackathon.service.http.InsightClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SkillService {

    private final InsightClient insightClient;
    private final RatingService ratingService;

    public SkillService(InsightClient insightClient,
                        RatingService ratingService) {
        this.insightClient = insightClient;
        this.ratingService = ratingService;
    }

    public List<Skill> getSkills(String userId) {
        HashMap<Long, Skill> skillMap = new HashMap<>();
        
        List<Skill> insightSkills = insightClient.getSkills(userId);
        insightSkills.forEach(skill -> skillMap.put(skill.getSkillId(), skill));
        
        List<Rating> ratings = ratingService.getRatings(userId);
        for(Rating rating : ratings) {
            Skill existingSkill = skillMap.get(rating.getSkillId());
            if(existingSkill != null) {
                existingSkill.addPoints(rating.getPoints());
                skillMap.replace(rating.getSkillId(), existingSkill);
            }
        }
        
        return new ArrayList<>(skillMap.values());
    }
}