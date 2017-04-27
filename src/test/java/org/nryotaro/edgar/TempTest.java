package org.nryotaro.edgar;

import org.junit.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Created by nryotaro on 2017/04/27.
 */
public class TempTest {

    public void temp() throws Exception {

        Mono<String> str = WebClient.create("").get().uri("").exchange().flatMap(i ->
                i.bodyToMono(String.class));
    }
}
