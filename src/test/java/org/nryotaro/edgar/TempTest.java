package org.nryotaro.edgar;

import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;

public class TempTest {

    public void temp() throws Exception {

        WebClient.create("").get().uri("").exchange().flatMapMany(i ->
                       i.body(BodyExtractors.toDataBuffers())
                ).reduce((a, b) -> {
            return a.write(b);});
    }
}
