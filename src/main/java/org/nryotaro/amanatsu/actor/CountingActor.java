package org.nryotaro.amanatsu.actor;

import akka.actor.AbstractActor;
import akka.actor.UntypedActor;
import org.nryotaro.amanatsu.service.CountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


/**
 * An actor that can count using an injected CountingService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Component("CountingActor")
@Scope("prototype")
public class CountingActor extends AbstractActor {

  @Override
  public Receive createReceive() {
      
    return null;
  }

  public static class Count {}
  public static class Get {}

  // the service that will be automatically injected
  final CountingService countingService;

  @Autowired
  public CountingActor(CountingService countingService) {
    this.countingService = countingService;
  }

  private int count = 0;

  @Override
  public void onReceive(Object message) throws Exception {
    if (message instanceof Count) {
      count = countingService.increment(count);
    } else if (message instanceof Get) {
      getSender().tell(count, getSelf());
    } else {
      unhandled(message);
    }
  }
}
