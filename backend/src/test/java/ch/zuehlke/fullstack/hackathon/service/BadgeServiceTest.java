package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Badge;
import ch.zuehlke.fullstack.hackathon.model.Rating;
import ch.zuehlke.fullstack.hackathon.model.Skill;
import ch.zuehlke.fullstack.hackathon.model.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BadgeServiceTest {
    private static final String VALID_ID = "feva";
    private RatingService ratingService;
    private SkillService skillService;
    private TrainingService trainingService;
    private BadgeService testee;

    @BeforeEach
    void setUp() {
        this.ratingService = mock(RatingService.class);
        this.skillService = mock(SkillService.class);
        this.trainingService = mock(TrainingService.class);
        this.testee = new BadgeService(ratingService, skillService, trainingService);
    }

    @Test
    void addBadgesForJavaKnowledge_overCountThresholdAndEarnedBadgesNull_throws() {
        assertThrows(NullPointerException.class,
                () -> testee.addBadgesForJavaKnowledge(null, 11));
    }

    @Test
    void addBadgesForJavaKnowledge_overCountThreshold_addsJavaBadgeToEarnedBadges() {
        List<Badge> earnedBadges = new ArrayList<>();

        testee.addBadgesForJavaKnowledge(earnedBadges, 11);

        assertEquals(1, earnedBadges.size());
        assertEquals(2, earnedBadges.get(0).getId());
        assertEquals("King of Java", earnedBadges.get(0).getDescription());
    }

    @Test
    void addBadgesForJavaKnowledge_underCountThreshold_nothingHappens() {
        List<Badge> earnedBadges = new ArrayList<>();

        testee.addBadgesForJavaKnowledge(earnedBadges, 9);

        assertEquals(0, earnedBadges.size());
    }

    @Test
    void addBadgesForWebKnowledge_overCountThresholdAndEarnedBadgesNull_throws() {
        assertThrows(NullPointerException.class,
                () -> testee.addBadgesForWebKnowledge(null, 11));
    }

    @Test
    void addBadgesForWebKnowledge_overCountThreshold_addsWebBadgeToEarnedBadges() {
        List<Badge> earnedBadges = new ArrayList<>();

        testee.addBadgesForWebKnowledge(earnedBadges, 11);

        assertEquals(1, earnedBadges.size());
        assertEquals(5, earnedBadges.get(0).getId());
        assertEquals("Master of Web Technologies", earnedBadges.get(0).getDescription());
    }

    @Test
    void addBadgesForWebKnowledge_underCountThreshold_nothingHappens() {
        List<Badge> earnedBadges = new ArrayList<>();

        testee.addBadgesForWebKnowledge(earnedBadges, 9);

        assertEquals(0, earnedBadges.size());
    }

    @Test
    void addBadgesForRatings_overPointsThresholdAndEarnedBadgesNull_throws() {
        Rating highRating = new Rating(VALID_ID, 0, 260);
        assertThrows(NullPointerException.class, () -> testee.addBadgesForRatings(null, List.of(highRating)));
    }

    @Test
    void addBadgesForRatings_overPointsThreshold_addsExpertVotesBadge() {
        List<Badge> earnedBadges = new ArrayList<>();
        Rating highRating = new Rating(VALID_ID, 0, 260);

        testee.addBadgesForRatings(earnedBadges, List.of(highRating));

        assertEquals(1, earnedBadges.size());
        assertEquals(3, earnedBadges.get(0).getId());
        assertEquals("5 Expert Votes for a skill", earnedBadges.get(0).getDescription());
    }

    @Test
    void addBadgesForRatings_twoRatingsOverPointsThreshold_addsExpertVotesBadgeOnce() {
        List<Badge> earnedBadges = new ArrayList<>();
        Rating highRating = new Rating(VALID_ID, 0, 260);
        Rating anotherHighRating = new Rating(VALID_ID, 2, 260);

        testee.addBadgesForRatings(earnedBadges, List.of(highRating, anotherHighRating));

        assertEquals(1, earnedBadges.size());
    }

    @Test
    void computeMatchingBadge_hackathonKeywordMatched_returnOptionalOfHackathonBadge() {
        Training aHackathonTraining = new Training("Full Stack Java Hackathon", 0, List.of(), 2);

        Optional<Badge> result = testee.computeMatchingBadge(aHackathonTraining);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId());
        assertEquals("Hackathon", result.get().getDescription());
    }

    @Test
    void computeMatchingBadge_noKeywordsMatched_returnEmptyOptional() {
        Training training = new Training("Test", 0, List.of(), 2);

        Optional<Badge> result = testee.computeMatchingBadge(training);

        assertTrue(result.isEmpty());
    }

    @Test
    void getBadges_withTrainingsAndRatingsMatchingThresholdsAndSkills_returnEarnedBadges() {
        Rating highRating = new Rating(VALID_ID, 0, 260);
        Training aHackathonTraining = new Training("Full Stack Java Hackathon", 0, List.of(), 2);
        Training aJavaTraining = new Training("Java Course", 1, List.of(), 3);
        Training aWebTraining = new Training("Javascript Course", 2, List.of(), 4);
        Skill javaInsightSkill = new Skill(0, "Java Ecosystem Description", "Java Ecosystem", "EXPERT");
        Skill webInsightSkill = new Skill(1, "Angular and html and css knowledge", "Web technologies", "PROFICIENT");

        when(trainingService.getTrainingsFromCurrentAndLastYear(VALID_ID)).thenReturn(List.of(aJavaTraining, aHackathonTraining, aWebTraining));
        when(ratingService.getRatings(VALID_ID)).thenReturn(List.of(highRating));
        when(skillService.getSkillsWithCalculatedPoints(VALID_ID)).thenReturn(
                new ArrayList<>(Arrays.asList(javaInsightSkill, webInsightSkill)));

        List<Badge> result = testee.getBadges(VALID_ID);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(badge ->
                badge.getId() == 1 && badge.getDescription().equals("Hackathon")
        ));
        assertTrue(result.stream().anyMatch(badge ->
                badge.getId() == 3 && badge.getDescription().equals("5 Expert Votes for a skill")
        ));
    }


}