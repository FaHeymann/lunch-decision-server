package controllers;

import actors.PushNotificationActorProtocol;
import akka.actor.ActorRef;
import com.avaje.ebean.Ebean;
import models.Meal;
import play.libs.Json;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static akka.pattern.Patterns.ask;
import static play.mvc.Results.notFound;
import static play.mvc.Results.ok;

public class ApiController {

    @Inject
    @Named("push-notification-actor")
    ActorRef pushNotificationActor;

    public Result current() {
        Map<String, String> payload = new HashMap<>();
        payload.put("test", "asd");
        ask(this.pushNotificationActor, new PushNotificationActorProtocol.Action(payload), 5000);

        try {
            Meal meal = Meal.find.where().eq("vetod", false).orderBy("currentPriority desc").findList().get(0);
            return ok(Json.toJson(meal));
        } catch(IndexOutOfBoundsException e) {
            return ok(Json.newObject());
        }
    }

    public Result all() {
        List<Meal> meals = Meal.find.all();

        return ok(Json.toJson(meals));
    }

    public Result veto(int mealId) {
        try {
            Meal meal = Meal.find.byId(mealId);
            meal.setVetod(true);
            Ebean.save(meal);
            return ok();
        } catch(Exception e) {
            return notFound();
        }
    }
}
