package ru.job4j.pooh;

public class Req {
    private final String method;
    private final String mode;
    private final String text;

    private Req(String method, String mode, String text) {
        this.method = method;
        this.mode = mode;
        this.text = text;
    }

    public static Req of(String content) {
        String[] buffAll = content.split(" ");
        String modeAndName = buffAll[1];
        String[] buffModeAndName = modeAndName.split("/");
        String method = buffAll[0];
        String mode = buffModeAndName[1];
        String name = buffModeAndName[2];
        String idSubscriber = "";
        if ("GET".equals(method) && "topic".equals(mode)) {
            idSubscriber = buffModeAndName[3];
        }
        String message = buffAll[buffAll.length - 1];
        String text = name + " " + message + " " + idSubscriber;
        return new Req(method, mode, text);
    }

    public String method() {
        return method;
    }

    public String mode() {
        return mode;
    }

    public String text() {
        return text;
    }
}
