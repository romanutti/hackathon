package ch.zuehlke.fullstack.hackathon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SkillTest {

    @Test
    void constructor_setsLevelAccordingToExperienceText() {
        Skill beginnerSkill = new Skill(0, "Test skill", "Test", "BEGINNER");
        assertEquals(Level.BEGINNER, beginnerSkill.getLevel());

        Skill proficientSkill = new Skill(0, "Test skill", "Test", "proFiCient");
        assertEquals(Level.PROFICIENT, proficientSkill.getLevel());

        Skill expertSkill = new Skill(0, "Test skill", "Test", "expert");
        assertEquals(Level.EXPERT, expertSkill.getLevel());

    }

    @Test
    void constructor_setsPointsAccordingToLevelFromExperienceText() {
        Skill result = new Skill(0, "Test skill", "Test", "BEGINNER");
        assertEquals(Level.getPoints(Level.BEGINNER), result.getPoints());
    }
}