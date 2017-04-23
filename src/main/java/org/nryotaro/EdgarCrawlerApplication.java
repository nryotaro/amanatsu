package org.nryotaro;

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

@SpringBootApplication
public class EdgarCrawlerApplication {

	public static void main(String[] args) {



		SpringApplication.run(EdgarCrawlerApplication.class, args);
	}
}
