package services;

import javax.inject.*;

import actors.CleanUpActorProtocol;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import play.Logger;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

@Singleton
public class AkkaScheduler {

//    ActorRef pushNotificationActor;

    ActorRef cleanUpActor;

    @Inject
    public AkkaScheduler(ActorSystem system, @Named("clean-up-actor") ActorRef cleanUpActor) {
        Logger.info("Started cron");

        this.cleanUpActor = cleanUpActor;

        system.scheduler().schedule(
            Duration.create(this.nextExecutionInSeconds(16, 30), TimeUnit.SECONDS),
            Duration.create(24, TimeUnit.HOURS),
            this.cleanUpActor,
            new CleanUpActorProtocol.CleanUp(),
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