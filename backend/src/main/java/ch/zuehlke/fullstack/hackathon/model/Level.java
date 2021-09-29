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

    public static long getPoints(Level level) {
        if (level == null) return 0;
        
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