package org.nryotaro.amanatsu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URL;
import java.time.LocalDateTime;


@Service
public class LinkBuildService {

    @Value("${edgar.url}")
    private String edgarUrl;

    public URI buildIndexLink(LocalDateTime s) {
        return null;
    }
}
