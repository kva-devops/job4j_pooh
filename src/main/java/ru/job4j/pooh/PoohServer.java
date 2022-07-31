package ru.job4j.pooh;

import ru.job4j.pooh.models.Req;
import ru.job4j.pooh.services.QueueService;
import ru.job4j.pooh.services.Service;
import ru.job4j.pooh.services.TopicService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main server class
 */
public class PoohServer {
    /**
     * Map of operating modes
     */
    private final HashMap<String, Service> availableServicesMap = new HashMap<>();

    /**
     * Method for starting server:
     * - add operating modes,
     * - create pool of connections
     * - starting server socket
     * - request processing and response forming
     */
    public void start() {
        availableServicesMap.put("queue", new QueueService());
        availableServicesMap.put("topic", new TopicService());
        ExecutorService pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(() -> {
                    try (OutputStream out = socket.getOutputStream();
                         InputStream input = socket.getInputStream()) {
                        byte[] buff = new byte[1_000_000];
                        var total = input.read(buff);
                        var text = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                        var req = Req.of(text);
                        var resp = availableServicesMap.get(req.getMode()).process(req);
                        out.write(("HTTP/1.1 " + resp.getStatus() + " OK\r\n").getBytes());
                        out.write((resp.getText() + "\n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main thread of application
     */
    public static void main(String[] args) {
        new PoohServer().start();
    }
}
