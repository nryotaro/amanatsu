package org.nryotaro.amanatsu;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.util.Timeout;
import org.nryotaro.amanatsu.actor.CountingActor;
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

import java.util.concurrent.TimeUnit;

import static akka.pattern.Patterns.ask;
import static org.nryotaro.amanatsu.actor.SpringExtension.SpringExtProvider;

@RestController
@SpringBootApplication
public class Edgar {


    @Autowired
    ApplicationContext ctx;

    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello, Spring Boot!";
    }

    public static void main(String[] args) {

        
        SpringApplication.run(Edgar.class, args);
    }

    @Bean
    public Runner runner() {
        return new Runner();
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


