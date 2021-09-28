package ch.zuehlke.fullstack.hackathon.controller;

import ch.zuehlke.fullstack.hackathon.model.Badge;
import ch.zuehlke.fullstack.hackathon.model.LevelDto;
import ch.zuehlke.fullstack.hackathon.model.Skill;
import ch.zuehlke.fullstack.hackathon.model.UserInfo;
import ch.zuehlke.fullstack.hackathon.service.BadgeService;
import ch.zuehlke.fullstack.hackathon.service.InsightClient;
import ch.zuehlke.fullstack.hackathon.service.RatingService;
import ch.zuehlke.fullstack.hackathon.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final InsightClient insightClient;
    private final BadgeService badgeService;
    private final SkillService skillService;
    private final RatingService ratingService;

    @Autowired
    public UserController(InsightClient insightClient, BadgeService badgeService,
                          SkillService skillService, RatingService ratingService) {
        this.insightClient = insightClient;
        this.badgeService = badgeService;
        this.skillService = skillService;
        this.ratingService = ratingService;
    }

    @GetMapping("/search")
    public ResponseEntity<UserInfo> getUserInfo(@RequestParam String term) {
        UserInfo result;
        try {
            List<UserInfo> response = this.insightClient.getEmployees(term);
            if (response != null && response.size() > 0) {
                result = response.get(0);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/badges")
    public ResponseEntity<List<Badge>> getBadges(@PathVariable String userId) {
        try {
            List<Badge> response = this.badgeService.getBadges(userId);
            if (response != null && response.size() > 0) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}/skills")
    public ResponseEntity<List<Skill>> getSkills(@PathVariable String userId) {
        try {
            List<Skill> response = this.skillService.getSkills(userId);
            if (response != null && response.size() > 0) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{userId}/rating/{skillId}")
    public ResponseEntity updateSkill(@PathVariable String userId, @PathVariable long skillId,
                                      @RequestBody LevelDto dto) {
        this.ratingService.addRating(userId, skillId, dto.getLevel());
        return ResponseEntity.ok().build();
    }

}