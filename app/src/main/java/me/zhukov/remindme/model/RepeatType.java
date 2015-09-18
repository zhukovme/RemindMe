package me.zhukov.remindme.model;

public enum RepeatType {

    MINUTE("Minute"),
    HOUR("Hour"),
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month");

    private final String text;

    RepeatType(final String text) {
        this.text = text;
    }

    public static String[] names() {
        String[] names = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            names[i] = values()[i].toString();
        }
        return names;
    }

    @Override
    public String toString() {
        return text;
    }
}
