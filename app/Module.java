import actors.PushNotificationActor;
import com.google.inject.AbstractModule;

import play.libs.akka.AkkaGuiceSupport;
import services.AkkaScheduler;
import services.Properties;

public class Module extends AbstractModule implements AkkaGuiceSupport {

    @Override
    public void configure() {
        bind(AkkaScheduler.class).asEagerSingleton();
        bind(Properties.class);
        bindActor(PushNotificationActor.class, "push-notification-actor");
    }

}