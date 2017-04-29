package org.nryotaro.edgar;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.junit.Test;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TempTest {

    public void temp() throws Exception {

        WebClient.create("").get().uri("").exchange().flatMapMany(i ->
                       i.body(BodyExtractors.toDataBuffers())
                ).reduce((a, b) -> {
            return a.write(b);});
    }
}
