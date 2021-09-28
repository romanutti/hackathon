package ch.zuehlke.fullstack.hackathon.model;

public enum Level {
    BEGINNER("BEGINNER"), PROFICIENT("PROFICIENT"), EXPERT("EXPERT");

    private final String levelCode;

    Level(String levelCode) {
        this.levelCode = levelCode;
    }

    public static Level fromString(String string) {
        if (string == null) {
            return null;
        }
        for (Level level : values()) {
            if (level.levelCode.equalsIgnoreCase(string)) {
                return level;
            }
        }
        return null;
    }
}