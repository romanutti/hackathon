package ch.zuehlke.fullstack.hackathon.service;

import ch.zuehlke.fullstack.hackathon.model.Badge;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {

    public List<Badge> getBadges(String userId) {
        return List.of(
                new Badge(1, String.format("%s's first badge for accessing our awesome app!", userId)),
                new Badge(2, String.format("%s's second badge for accessing our awesome app!", userId)),
                new Badge(3, String.format("%s's third badge for accessing our awesome app!", userId)),
                new Badge(4, String.format("%s's fift badge for accessing our awesome app!", userId)),
                new Badge(5, String.format("%s's sixt badge for accessing our awesome app!", userId)));
    }

}