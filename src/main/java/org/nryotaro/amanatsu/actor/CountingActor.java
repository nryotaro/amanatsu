package org.nryotaro.amanatsu.actor;

import akka.actor.AbstractActor;
import akka.actor.UntypedActor;
import akka.japi.pf.ReceiveBuilder;
import akka.japi.pf.UnitPFBuilder;
import org.nryotaro.amanatsu.service.CountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


/**
 * An actor that can count using an injected CountingService.
 *
 * @note The scope here is prototype since we want to create a new actor
 * instance for use of this bean.
 */
@Component("CountingActor")
@Scope("prototype")
public class CountingActor extends AbstractActor {

  private int count = 0;

  // the org.nryotaro.amanatsu.service that will be automatically injected
  @Autowired
  CountingService countingService;


  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .match(Count.class, s -> count = countingService.increment(count))
            .match(Get.class, g -> getSender().tell(count, getSelf()))
            .matchAny(this::unhandled)
            .build();
  }


  public static class Count {}
  public static class Get {}


  /*
  @Autowired
  public CountingActor(CountingService countingService) {
    this.countingService = countingService;
  }


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
  */
}
