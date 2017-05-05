package org.nryotaro.edgar;

import org.junit.Test;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TempTest {

    public void temp() throws Exception {

        WebClient.create("https://www.sec.gov")
                .get().uri("Archives/edgar/data/740629/9999999997-17-002782-index.htm").exchange()
                .onErrorResume((i) -> Mono.empty())
                .map((i) -> i.body(BodyExtractors.toMono(String.class)                )).block();
    }
}
