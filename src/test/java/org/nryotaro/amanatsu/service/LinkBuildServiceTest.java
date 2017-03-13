package org.nryotaro.amanatsu.service;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nryotaro.amanatsu.Edgar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Edgar.class,
        initializers = ConfigFileApplicationContextInitializer.class)
public class LinkBuildServiceTest {


    @MockBean
    private LinkBuildService service;

    @Test
    public void testBuildIndexLink() throws Exception {
        LocalDateTime.parse("2007-12-03T10:15:30");

        String date = "2016-08-16";

        //default, ISO_LOCAL_DATE
        LocalDate localDate = LocalDate.parse(date);

        //assertThat("", LocalDateTime.parse("2011-12-03", ), equalTo(""));

    }
}
