package ch.zuehlke.fullstack.hackathon.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LevelTest {

    @Test
    void getPoints_levelNull_return_0() {
        long result = Level.getPoints(null);
        assertEquals(0, result);
    }

    @Test
    void getPoints_levelBeginner_return_1() {
        long result = Level.getPoints(Level.BEGINNER);
        assertEquals(1, result);
    }

    @Test
    void getPoints_levelProficient_return_10() {
        long result = Level.getPoints(Level.PROFICIENT);
        assertEquals(10, result);
    }

    @Test
    void getPoints_levelExpert_return_50() {
        long result = Level.getPoints(Level.EXPERT);
        assertEquals(50, result);
    }

    @Test
    void fromString_null_returnNull() {
        assertNull(Level.fromString(null));
    }

    @Test
    void fromString_noMatchingLevel_returnNull() {
        Level result = Level.fromString("Test");
        assertNull(result);
    }

    @Test
    void fromString_matchesBeginnerWithIgnoredCase_returnBeginner() {
        Level result = Level.fromString("beGinnEr");
        assertEquals(Level.BEGINNER, result);
    }

    @Test
    void fromString_matchesProficientLowCase_returnBeginner() {
        Level result = Level.fromString("proficient");
        assertEquals(Level.PROFICIENT, result);
    }

    @Test
    void fromString_matchesExpertHighCase_returnBeginner() {
        Level result = Level.fromString("EXPERT");
        assertEquals(Level.EXPERT, result);
    }

}