package org.nryotaro.edgar;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import org.nryotaro.edgar.actor.CountingActor;
import org.nryotaro.edgar.edgar.Client;
import org.nryotaro.edgar.service.EdgarDailyIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.CommandLineRunner;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static org.nryotaro.edgar.actor.SpringExtension.SpringExtProvider;

@RestController
@SpringBootApplication
public class Edgar {


    @Autowired
    ApplicationContext ctx;

    @Autowired
    Client client;

    @Autowired
    EdgarDailyIndexService service;

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello, Spring Boot!";
    }

    public static void main(String[] args) {

        SpringApplication application = new SpringApplication(Edgar.class);
        application.setWebEnvironment(false);
        application.run(args);
        //SpringApplication.run(Edgar.class, args);
    }

    @Bean
    public CommandLineRunner runner() {
        return new EdgarRunner();
    }

    class EdgarRunner implements CommandLineRunner {

        @Override
        public void run(String... strings) throws Exception {



            client.connect();

            client.request(service.buildIndexLink(LocalDate.parse("2017-01-10")));
            client.sync();

            client.close();
        }
    }


    class Runner implements CommandLineRunner {

        @Override
        public void run(String... strings) throws Exception {
            // get hold of the actor system

            ActorSystem system = ctx.getBean(ActorSystem.class);
            // use the Spring Extension to create props for a named actor bean
            ActorRef counter = system.actorOf(
                    SpringExtProvider.get(system).props("CountingActor"), "counter");


            // tell it to count three times
            counter.tell(new CountingActor.Count(), null);
            counter.tell(new CountingActor.Count(), null);
            counter.tell(new CountingActor.Count(), null);

            // print the result
            FiniteDuration duration = FiniteDuration.create(3, TimeUnit.SECONDS);
            Future<Object> result = ask(counter, new CountingActor.Get(),
                    Timeout.durationToTimeout(duration));
            try {
                System.out.println("Got back " + Await.result(result, duration));
            } catch (Exception e) {
                System.err.println("Failed getting result: " + e.getMessage());
                throw e;
            } finally {
                system.terminate();

            }
        }
    }
}


