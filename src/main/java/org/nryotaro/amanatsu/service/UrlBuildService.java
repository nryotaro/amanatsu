package org.nryotaro.amanatsu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UrlBuildService {

    @Value("${edgar.url.context}")
    private String edgarUrl;

    public URI buildIndexLink(LocalDate date) {
        throw new UnsupportedOperationException();
    }
}
