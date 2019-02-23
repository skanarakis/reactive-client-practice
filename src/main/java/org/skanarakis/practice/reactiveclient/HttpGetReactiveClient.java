package org.skanarakis.practice.reactiveclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

class HttpGetReactiveClient<T> {

    private Logger logger = LoggerFactory.getLogger(HttpGetReactiveClient.class);

    private WebClient webClient;

    private Class<T> returnType;

    HttpGetReactiveClient(Class<T> returnType) {
        this.webClient = WebClient.builder().build();
        this.returnType = returnType;
    }

    Mono<T> sendRequestTo(String url) {
        logger.debug("Will request for class {}", returnType.getName());
        return Mono.defer(() -> webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(returnType)));
    }
}
