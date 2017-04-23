package org.nryotaro.edgar.cmdparser;

import org.junit.Test;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.web.reactive.function.*;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class FooTest {

    //@Test
    public void foo() {
        		WebClient client =  WebClient.create("https://www.sec.gov");

		String a = client.get().uri("Archiveis/edgar/daily-index/2017/QTR1/crawler.20170331.idx")
				.exchange().flatMap(e ->
						e.bodyToMono(String.class)).block();

		System.out.println(a);
		/*

		Mono<Integer> integerMono = client.get().uri("Archives/edgar/daily-index/2017/QTR1/crawler.20170331.idx")
				.exchange().flatMap((ClientResponse e) ->
						e.body((ClientHttpResponse a, BodyExtractor.Context b) -> {
							return Mono.just(1);
						}));
						*/
    }
}
