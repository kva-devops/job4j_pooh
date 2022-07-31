package ru.job4j.pooh.services;

import ru.job4j.pooh.models.Req;
import ru.job4j.pooh.models.Resp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
/**
 * Mode of "topic"
 */
public class TopicService implements Service {

    /**
     * Queue storage where the key is the name of the topic
     */
    private final ConcurrentHashMap<String, LinkedBlockingQueue<String>> queueStorageWithTopicNameKey =
            new ConcurrentHashMap<>();

    /**
     * Queue storage where the key is the subscriber ID
     */
    private final ConcurrentHashMap<Integer, LinkedBlockingQueue<String>> queueStorageWithSubscriberIdKey =
            new ConcurrentHashMap<>();

    /**
     * Topic storage where the key is the name the topic and value is the list of the subscribers IDs
     */
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Integer>> topicStorageWithSubscribersIds =
            new ConcurrentHashMap<>() {{
                put("moscow", new CopyOnWriteArrayList<>() {{
                    add(1);
                    add(2);
                }});
            }};

    /**
     * Implementation for request processing and response forming
     * @param req Req object (request)
     * @return Resp object (response)
     */
    @Override
    public Resp process(Req req) {
        String methodOfRequest = req.getMethod();
        String textOfRequest = req.getText();
        String[] textOfRequestArray = textOfRequest.split(" ");
        String nameOfTopic = textOfRequestArray[0];
        String textOfMessage = textOfRequestArray[1];
        queueStorageWithTopicNameKey.putIfAbsent(nameOfTopic, new LinkedBlockingQueue<>());
        LinkedBlockingQueue<String> innerQueue = queueStorageWithTopicNameKey.get(nameOfTopic);
        Resp responseResult = null;
        if ("POST".equals(methodOfRequest)) {
            innerQueue.add(textOfMessage);
            responseResult = new Resp(textOfMessage, 200);
        } else if ("GET".equals(methodOfRequest)) {
            try {
                String idSubscriber = textOfRequestArray[2];
                topicStorageWithSubscribersIds.putIfAbsent(nameOfTopic, new CopyOnWriteArrayList<>());
                CopyOnWriteArrayList<Integer> listOfSubscribersIds = topicStorageWithSubscribersIds.get(nameOfTopic);
                for (Integer subscriberId : listOfSubscribersIds) {
                    queueStorageWithSubscriberIdKey.putIfAbsent(subscriberId, new LinkedBlockingQueue<>(innerQueue));
                }
                responseResult = new Resp(queueStorageWithSubscriberIdKey.get(Integer.parseInt(idSubscriber)).poll(), 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }
}
