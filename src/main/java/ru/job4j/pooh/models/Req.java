package ru.job4j.pooh.models;

/**
 * Model of user's request
 */
public class Req {
    /**
     * Method of request (e.g. POST or GET)
     */
    private final String method;
    /**
     * Mode of request (e.g. topic or queue)
     */
    private final String mode;
    /**
     * Text of request - message
     */
    private final String text;

    private Req(String method, String mode, String text) {
        this.method = method;
        this.mode = mode;
        this.text = text;
    }

    /**
     * Fabric for creating new request object
     * @param content String of request
     * @return Req object
     */
    public static Req of(String content) {
        String[] contentArray = content.split(" ");
        String modeAndName = contentArray[1];
        String[] modeAndNameArray = modeAndName.split("/");
        String method = contentArray[0];
        String mode = modeAndNameArray[1];
        String name = modeAndNameArray[2];
        String idSubscriber = "";
        if ("GET".equals(method) && "topic".equals(mode)) {
            idSubscriber = modeAndNameArray[3];
        }
        String message = contentArray[contentArray.length - 1];
        String text = name + " " + message + " " + idSubscriber;
        return new Req(method, mode, text);
    }

    public String getMethod() {
        return method;
    }

    public String getMode() {
        return mode;
    }

    public String getText() {
        return text;
    }
}
