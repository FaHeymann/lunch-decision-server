package actors;

import akka.actor.UntypedActor;
import com.avaje.ebean.Ebean;
import models.Meal;
import models.User;

public class CleanUpActor extends UntypedActor {
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof CleanUpActorProtocol.CleanUp) {
            for(User user : User.find.where().eq("vetod", true).findList()) {
                user.setVetod(false);
                Ebean.save(user);
            }

            for(Meal meal : Meal.find.all()) {
                meal.setVetod(false);
                meal.setCurrentPriority(meal.getCurrentPriority() + meal.getPriorityGain());
                Ebean.save(meal);
            }
        }
    }
}
