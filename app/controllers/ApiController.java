package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Meal;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

public class ApiController extends Controller {

    @Security.Authenticated(ApiSecured.class)
    public Result current() {
        User user = User.find.where().eq("email", request().username()).findUnique();

        try {
            Meal meal = Meal.find.where().eq("vetod", false).orderBy("currentPriority desc").findList().get(0);
            ObjectNode container = Json.newObject();
            container.set("meal", Json.toJson(meal));
            container.set("canVeto", Json.toJson(!user.hasVetod()));
            return ok(Json.toJson(container));
        } catch(IndexOutOfBoundsException e) {
            ObjectNode container = Json.newObject();
            container.set("meal", Json.newObject());
            container.set("canVeto", Json.toJson(!user.hasVetod()));
            return ok(Json.toJson(container));
        }
    }

    @Security.Authenticated(ApiSecured.class)
    public Result all() {
        List<Meal> meals = Meal.find.all();

        return ok(Json.toJson(meals));
    }

    @Security.Authenticated(ApiSecured.class)
    public Result veto(int mealId) {
        User user = User.find.where().eq("email", request().username()).findUnique();

        if(user.hasVetod()) {
            return badRequest(Json.toJson("user vetod already"));
        }

        try {
            Meal meal = Meal.find.byId(mealId);
            meal.setVetod(true);
            Ebean.save(meal);

            user.setVetod(true);
            Ebean.save(user);

            return ok();
        } catch(Exception e) {
            return notFound();
        }
    }
}
