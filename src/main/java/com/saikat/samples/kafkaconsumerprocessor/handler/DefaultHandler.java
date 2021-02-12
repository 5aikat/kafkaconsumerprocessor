package com.saikat.samples.kafkaconsumerprocessor.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Saikat
 */
public interface DefaultHandler {

    /**
     * The interface to handle specific request
     * The default Mono Handler
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> handle(ServerRequest serverRequest);
}
