package ru.job4j.pooh.models;

/**
 * Model of user's response
 */
public class Resp {
    /**
     * Text of message
     */
    private final String text;
    /**
     * Status of sending (e.g. 200)
     */
    private final int status;

    public Resp(String text, int status) {
        this.text = text;
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public int getStatus() {
        return status;
    }
}
