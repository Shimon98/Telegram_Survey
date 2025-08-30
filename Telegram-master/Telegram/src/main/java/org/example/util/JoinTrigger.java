package org.example.util;


public enum JoinTrigger {
    START("/start"), HI_EN("hi"), HI_HE("היי");

    private String text;

    JoinTrigger(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static boolean matches(String input) {
        if (input == null) return false;
        String lc = input.trim().toLowerCase();
        for (JoinTrigger t : values()) if (t.text.equals(lc)) return true;
        return false;
    }
}
