package org.nryotaro.amanatsu.actor;


import akka.actor.ActorSystem;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static org.nryotaro.amanatsu.actor.SpringExtension.SpringExtProvider;

@Component
public class EdgarSystem {

    /**
     * Actor system singleton for this application.
     */
    @Bean
    public ActorSystem actorSystem(ApplicationContext context) {
        ActorSystem system = ActorSystem.create("edgar");
        SpringExtProvider.get(system).initialize(context);
        return system;
    }
}
