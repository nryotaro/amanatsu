package org.nryotaro.amanatsu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import static java.time.Month.*;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Service
public class UrlBuildService {

    @Value("${edgar.url.host}")
    private String edgarHost;

    @Value("${edgar.url.daily-index}")
    private String edgarDailyIndex;


    public URI buildIndexLink(LocalDate date) throws URISyntaxException {
        return new URI("https://"+edgarHost +"/" + edgarDailyIndex + date.getYear() + "/" + buildQTR(date.getMonth()) + "/crawler."
                + date.format(DateTimeFormatter.BASIC_ISO_DATE) + ".idx");
    }


    private static String buildQTR(Month month) {
        if(Arrays.asList(JANUARY, FEBRUARY, MARCH).contains(month)) {
            return "QTR1";
        }
        if(Arrays.asList(APRIL, MAY, JUNE ).contains(month)) {
            return "QTR2";
        }
        if(Arrays.asList(JULY, AUGUST, SEPTEMBER).contains(month)) {
            return "QTR3";
        }
        return "QTR4";

    }
}
