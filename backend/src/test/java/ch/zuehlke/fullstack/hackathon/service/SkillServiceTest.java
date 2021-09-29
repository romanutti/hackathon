package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Rating;
import ch.zuehlke.fullstack.hackathon.model.Skill;
import ch.zuehlke.fullstack.hackathon.service.http.InsightClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SkillServiceTest {
    private static final String VALID_ID = "feva";
    private InsightClient insightClient;
    private RatingService ratingService;
    private SkillService testee;

    @BeforeEach
    void setUp() {
        this.insightClient = mock(InsightClient.class);
        this.ratingService = mock(RatingService.class);
        this.testee = new SkillService(insightClient, ratingService);
    }

    @Test
    void getSkillsWithCalculatedPoints_noRatings_returnsFetchedSkills() {
        Skill skill = new Skill(0, "Testing skill", "Test", "BEGINNER");
        List<Skill> skillList = List.of(skill);
        when(insightClient.getSkills(VALID_ID)).thenReturn(skillList);
        when(ratingService.getRatings(VALID_ID)).thenReturn(List.of());

        List<Skill> result = testee.getSkillsWithCalculatedPoints(VALID_ID);

        assertEquals(1, result.size());
        assertEquals(skill.getSkillId(), result.get(0).getSkillId());
        assertEquals(skill.getPoints(), result.get(0).getPoints());

    }

    @Test
    void getSkillsWithCalculatedPoints_withRatingForFetchedSkill_returnsSkillWithUpdatedPoints() {
        Skill skill = new Skill(0, "Testing skill", "Test", "BEGINNER");
        long startingPoints = skill.getPoints();
        when(insightClient.getSkills(VALID_ID)).thenReturn(List.of(skill));
        Rating rating = new Rating(VALID_ID, 0, 50);
        when(ratingService.getRatings(VALID_ID)).thenReturn(List.of(rating));

        List<Skill> result = testee.getSkillsWithCalculatedPoints(VALID_ID);

        assertEquals(1, result.size());
        assertEquals(skill.getSkillId(), result.get(0).getSkillId());
        assertNotEquals(startingPoints, result.get(0).getPoints());
        assertEquals(startingPoints + rating.getPoints(), result.get(0).getPoints());
    }

    @Test
    void getSkillsWithCalculatedPoints_withRatingForDifferentSkill_returnsFetchedSkill() {
        Skill skill = new Skill(0, "Testing skill", "Test", "BEGINNER");
        long startingPoints = skill.getPoints();
        when(insightClient.getSkills(VALID_ID)).thenReturn(List.of(skill));
        Rating rating = new Rating(VALID_ID, 1, 50);
        when(ratingService.getRatings(VALID_ID)).thenReturn(List.of(rating));

        List<Skill> result = testee.getSkillsWithCalculatedPoints(VALID_ID);

        assertEquals(1, result.size());
        assertEquals(skill.getSkillId(), result.get(0).getSkillId());
        assertEquals(startingPoints, result.get(0).getPoints());
    }
}