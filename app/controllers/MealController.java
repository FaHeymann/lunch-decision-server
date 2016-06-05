package controllers;

import com.avaje.ebean.Ebean;
import com.google.inject.Inject;
import models.Meal;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.data.validation.Constraints;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.meals.*;
import java.util.List;

import static play.data.Form.form;

public class MealController extends Controller {

    @Inject
    FormFactory formFactory;

    @Security.Authenticated(Secured.class)
    public Result list() {
        List<Meal> meals = Meal.find.all();

        return ok(list.render(meals));
    }

    @Security.Authenticated(Secured.class)
    public Result create() {
        return ok(create.render(formFactory.form(MealData.class)));
    }

    @Security.Authenticated(Secured.class)
    public Result save() {

        Form<MealData> mealForm = formFactory.form(MealData.class).bindFromRequest();
        if (mealForm.hasErrors()) {
            return badRequest(create.render(mealForm));
        } else {
            Meal meal = new Meal();
            meal.setName(mealForm.get().getName());
            meal.setDescription(mealForm.get().getDescription());
            meal.setImage(mealForm.get().getImage());
            meal.setCurrentPriority(Integer.parseInt(mealForm.get().getCurrentPriority()));
            meal.setPriorityGain(Integer.parseInt(mealForm.get().getPriorityGain()));
            Ebean.save(meal);

            return redirect(routes.MealController.list());
        }
    }

    @Security.Authenticated(Secured.class)
    public Result edit(int mealId) {
        Meal meal = Meal.find.byId(mealId);
        Form<MealData> mealForm = formFactory.form(MealData.class);
        mealForm.data().put("name", meal.getName());
        mealForm.data().put("description", meal.getDescription());
        mealForm.data().put("image", meal.getImage());
        mealForm.data().put("currentPriority", "" + meal.getCurrentPriority());
        mealForm.data().put("priorityGain", "" + meal.getPriorityGain());
        mealForm.data().put("id", "" + meal.getId());
        return ok(edit.render(mealForm));
    }

    @Security.Authenticated(Secured.class)
    public Result saveEdit() {
        Form<MealData> mealForm = formFactory.form(MealData.class).bindFromRequest();
        if (mealForm.hasErrors()) {
            return badRequest(edit.render(mealForm));
        } else {
            Meal meal = Meal.find.byId(Integer.parseInt(mealForm.data().get("id")));
            meal.setName(mealForm.get().getName());
            meal.setDescription(mealForm.get().getDescription());
            meal.setImage(mealForm.get().getImage());
            meal.setCurrentPriority(Integer.parseInt(mealForm.get().getCurrentPriority()));
            meal.setPriorityGain(Integer.parseInt(mealForm.get().getPriorityGain()));
            Ebean.update(meal);

            return redirect(routes.MealController.list());
        }
    }

    @Security.Authenticated(Secured.class)
    public Result detail(int mealId) {
        Meal meal = Meal.find.byId(mealId);

        return ok(detail.render(meal));
    }

    public static class MealData {

        protected String name;
        protected String description;
        protected String image;
        protected String currentPriority;
        protected String priorityGain;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCurrentPriority() {
            return currentPriority;
        }

        public void setCurrentPriority(String currentPriority) {
            this.currentPriority = currentPriority;
        }

        public String getPriorityGain() {
            return priorityGain;
        }

        public void setPriorityGain(String priorityGain) {
            this.priorityGain = priorityGain;
        }

        public String validate() {

            if(name == null || name.equals("")) {
                return "Name must not be empty";
            }
            if(description == null || description.equals("")) {
                return "Description must not be empty";
            }
            if(image == null || image.equals("")) {
                return "Image must not be empty";
            }
            if(currentPriority == null || currentPriority.equals("")) {
                return "Current Priority must not be empty";
            }
            if(priorityGain == null || priorityGain.equals("")) {
                return "Priority Gain must not be empty";
            }
            return null;
        }
    }
}
