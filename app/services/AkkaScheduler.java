package services;

import javax.inject.*;

import actors.PushNotificationActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import play.Logger;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

@Singleton
public class AkkaScheduler {

    @Inject
    @Named("push-notification-actor")
    ActorRef pushNotificationActor;

    @Inject
    public AkkaScheduler(ActorSystem system) {
        Logger.info("Started cron");
        system.scheduler().schedule(
            Duration.create(this.nextExecutionInSeconds(1, 0), TimeUnit.SECONDS),
            Duration.create(24, TimeUnit.HOURS),
            this.pushNotificationActor,
            new PushNotificationActorProtocol.Notification("Test", "test"),
            system.dispatcher(),
            null
        );
    }

    private int nextExecutionInSeconds(int hour, int minute){
        return Seconds.secondsBetween(
            new DateTime(),
            this.nextExecution(hour, minute)
        ).getSeconds();
    }

    private DateTime nextExecution(int hour, int minute){
        DateTime next = new DateTime()
            .withHourOfDay(hour)
            .withMinuteOfHour(minute)
            .withSecondOfMinute(0)
            .withMillisOfSecond(0);

        return (next.isBeforeNow()) ? next.plusHours(24) : next;
    }

}