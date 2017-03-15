package org.nryotaro.amanatsu.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest({"edgar.url.host=www.sec.gov", "edgar.url.daily-index=Archives/edgar/daily-index/"})
//@TestPropertySource(locations = "/application-ut.yml")
//@ActiveProfiles("ut")
/*
@ContextConfiguration(classes = Edgar.class,
        initializers = ConfigFileApplicationContextInitializer.class)
        */
public class LinkBuildServiceTest {

    @Autowired
    UrlBuildService service;

    String url = "https://www.sec.gov/";

    @Test
    public void testBuildIndexLink() throws Exception {
        assertThat(service.buildIndexLink(LocalDate.parse("2017-03-10")),
                equalTo(new URI(url + "Archives/edgar/daily-index/2017/QTR1/crawler.20170310.idx")));
    }

}
