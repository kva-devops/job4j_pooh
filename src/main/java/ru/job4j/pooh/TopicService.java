package ru.job4j.pooh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String, LinkedBlockingQueue<String>> topics =
            new ConcurrentHashMap<>();

    private final Map<Integer, List<String>> subscribers =
            new HashMap<>() {{
               put(1, new ArrayList<>() {{
                   add("moscow");
               }});
               put(2, new ArrayList<>() {{
                   add("moscow");
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
                responseResult = new Resp(topics.get(name).poll(), 200);
                System.out.println(idSubscriber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }
}
