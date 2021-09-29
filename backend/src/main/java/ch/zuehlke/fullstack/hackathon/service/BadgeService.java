package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class BadgeService {

    private final RatingService ratingService;
    private final SkillService skillService;
    private final TrainingService trainingService;

    private final Map<BadgeType, List<Badge>> availableBadges;

    public BadgeService(RatingService ratingService,
                        SkillService skillService,
                        TrainingService trainingService) {
        this.ratingService = ratingService;
        this.skillService = skillService;
        this.trainingService = trainingService;
        availableBadges = createAvailableBadges();
    }

    private Map<BadgeType, List<Badge>> createAvailableBadges() {
        HashMap<BadgeType, List<Badge>> badges = new HashMap<>();
        badges.put(BadgeType.LEARNING, List.of(new Badge(1, "Hackathon")));
        badges.put(BadgeType.SKILLS, List.of(new Badge(2, "King of Java"),
                new Badge(5, "Master of Web Technologies")));
        badges.put(BadgeType.RATING, List.of(new Badge(3, "5 Expert Votes for a skill")));
        badges.put(BadgeType.PROJECT, List.of(new Badge(4, "Practitioner")));
        return badges;
    }

    public List<Badge> getBadges(String userId) {
        List<Badge> earnedBadges = new ArrayList<>();

        AtomicInteger javaCount = new AtomicInteger();
        AtomicInteger webCount = new AtomicInteger();

        // trainings
        List<Training> trainingResponseList = trainingService.getTrainingsFromLastTwoYears(userId);
        trainingResponseList.forEach(training -> {
            Optional<Badge> matchingBadge = computeMatchingBadge(training);
            matchingBadge.ifPresent(earnedBadges::add);
            if (matchesJavaKeywords(training.getName())) javaCount.getAndIncrement();
            if (matchesWebKeywords(training.getName())) webCount.getAndIncrement();
        });

        // ratings
        List<Rating> ratings = ratingService.getRatings(userId);
        addBadgesForRatings(earnedBadges, ratings);

        // skills
        List<Skill> skills = skillService.getSkillsWithCalculatedPoints(userId);
        skills.sort(Comparator.comparing(Skill::getPoints).reversed());
        for (Skill skill : skills) {
            if (skill.getPoints() == 0) break;
            if (matchesJavaKeywords(skill.getName() + ": " + skill.getDescription())) javaCount.getAndIncrement();
            if (matchesWebKeywords(skill.getName() + ": " + skill.getDescription())) webCount.getAndIncrement();
        }

        addBadgesForJavaKnowledge(earnedBadges, javaCount.get());
        addBadgesForWebKnowledge(earnedBadges, webCount.get());

        return earnedBadges;
    }

    private void addBadgesForJavaKnowledge(List<Badge> earnedBadges, int count) {
        if (count > 10) earnedBadges.add(availableBadges.get(BadgeType.SKILLS).get(0));
    }

    private void addBadgesForWebKnowledge(List<Badge> earnedBadges, int count) {
        if (count > 10) earnedBadges.add(availableBadges.get(BadgeType.SKILLS).get(1));
    }

    private void addBadgesForRatings(List<Badge> earnedBadges, List<Rating> ratings) {
        for (Rating rating : ratings) {
            if (rating.getPoints() >= 250) {
                earnedBadges.add(availableBadges.get(BadgeType.RATING).get(0));
                break;
            }
        }
    }

    private Optional<Badge> computeMatchingBadge(Training training) {
        if (matchesHackathonKeywords(training.getName())) {
            return Optional.of(availableBadges.get(BadgeType.LEARNING).get(0));
        }
        return Optional.empty();
    }

    private boolean matchesWebKeywords(String text) {
        String[] keywords = {"web", "html", "css", "angular", "js", "javascript", "ts", "typescript"};
        return Stream.of(keywords).anyMatch(keyword -> text.toLowerCase().contains(keyword));
    }

    private boolean matchesJavaKeywords(String text) {
        String[] keywords = {"java", "spring"};
        return Stream.of(keywords).anyMatch(keyword -> text.toLowerCase().contains(keyword));
    }

    private boolean matchesHackathonKeywords(String text) {
        String[] keywords = {"hackathon", "hack"};
        return Stream.of(keywords).anyMatch(keyword -> text.toLowerCase().contains(keyword));
    }

}