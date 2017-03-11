package org.nryotaro.amanatsu.actor;


import akka.actor.ActorSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.nryotaro.amanatsu.SpringExtension.SpringExtProvider;

@Component
public class AmanatsuSystem {


    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem(ApplicationContext context) {
        ActorSystem system = ActorSystem.create("Amanatsu");
        SpringExtProvider.get(system).initialize(context);
        return system;
    }
}
