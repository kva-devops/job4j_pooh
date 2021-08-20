package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class QueueService implements Service {
    private final ConcurrentHashMap<String, LinkedBlockingQueue<String>> queues =
            new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        String method = req.method();
        String text = req.text();
        String[] buffText = text.split(" ");
        String name = buffText[0];
        String message = buffText[1];
        queues.putIfAbsent(name, new LinkedBlockingQueue<>());
        LinkedBlockingQueue<String> innerQueue = queues.get(name);
        Resp responseResult = null;
        if ("POST".equals(method)) {
            innerQueue.add(message);
            responseResult = new Resp(message, 200);
        } else if ("GET".equals(method)) {
            try {
                responseResult = new Resp(queues.get(name).poll(), 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }
}
