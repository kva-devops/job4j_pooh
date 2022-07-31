package ru.job4j.pooh.services;

import ru.job4j.pooh.models.Req;
import ru.job4j.pooh.models.Resp;

/**
 * Interface for request processing and response forming
 */

public interface Service {
    Resp process(Req req);
}
