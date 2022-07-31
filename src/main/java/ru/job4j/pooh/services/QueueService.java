package ru.job4j.pooh.services;

import ru.job4j.pooh.models.Req;
import ru.job4j.pooh.models.Resp;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Mode of "queue"
 */
public class QueueService implements Service {
    /**
     * Queues storage where the key is the name of the queue
     */
    private final ConcurrentHashMap<String, LinkedBlockingQueue<String>> queuesStorageWithNameOfQueueKey =
            new ConcurrentHashMap<>();

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
        String nameOfQueue = textOfRequestArray[0];
        String textOfMessage = textOfRequestArray[1];
        queuesStorageWithNameOfQueueKey.putIfAbsent(nameOfQueue, new LinkedBlockingQueue<>());
        LinkedBlockingQueue<String> innerQueue = queuesStorageWithNameOfQueueKey.get(nameOfQueue);
        Resp responseResult = null;
        if ("POST".equals(methodOfRequest)) {
            innerQueue.add(textOfMessage);
            responseResult = new Resp(textOfMessage, 200);
        } else if ("GET".equals(methodOfRequest)) {
            try {
                responseResult = new Resp(queuesStorageWithNameOfQueueKey.get(nameOfQueue).poll(), 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseResult;
    }
}
