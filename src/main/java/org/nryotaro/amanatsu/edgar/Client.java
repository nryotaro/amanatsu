package org.nryotaro.amanatsu.edgar;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Client {

    public Client(@Value("${edgar.url.host}") String host) {

    }
}
