package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, LinkedBlockingQueue<String>> topics =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> topicById =
            new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> topicSubscribers =
            new ConcurrentHashMap<>() {{
                put("moscow", new CopyOnWriteArrayList<>() {{
                    add(1);
                    add(2);
                }});
            }};

    @Override
    public Resp process(Req req) {
        String method = req.method();
        String text = req.text();
        String[] buffText = text.split(" ");
        String name = buffText[0];
        String message = buffText[1];
        topics.putIfAbsent(name, new LinkedBlockingQueue<>());
        LinkedBlockingQueue<String> innerQueue = topics.get(name);
        Resp responseResult = null;
        if ("POST".equals(method)) {
            innerQueue.add(message);
            responseResult = new Resp(message, 200);
        } else if ("GET".equals(method)) {
            try {
                String idSubscriber = buffText[2];
                topicSubscribers.putIfAbsent(name, new CopyOnWriteArrayList<>());
                CopyOnWriteArrayList<Integer> buff = topicSubscribers.get(name);
                for (Integer elem : buff) {
                    topicById.putIfAbsent(elem, new LinkedBlockingQueue<>(innerQueue));
                }
                responseResult = new Resp(topicById.get(Integer.parseInt(idSubscriber)).poll(), 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }
}
