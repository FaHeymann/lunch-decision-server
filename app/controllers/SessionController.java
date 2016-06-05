package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.session.login;
import views.html.session.register;

import static play.data.Form.form;

public class SessionController extends Controller {

    public Result login() {
        return ok(login.render(form(Login.class)));
    }

    public Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(routes.MealController.list());
        }
    }

    public Result logout() {
        session().clear();
        return redirect(routes.SessionController.login());
    }

    public Result registrationForm() {
        return ok(register.render(form(Registration.class)));
    }

    public Result register() {
        Form<Registration> registrationForm = form(Registration.class).bindFromRequest();
        if(registrationForm.hasErrors()) {
            return badRequest(register.render(registrationForm));
        } else {
            User user = new User(
                registrationForm.data().get("email"),
                registrationForm.data().get("name"),
                registrationForm.data().get("password1")
            );
            user.save();
            return redirect(routes.SessionController.login());
        }
    }

    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static class Registration {
        public String email;
        public String name;
        public String password1;
        public String password2;

        public String validate() {
            if("".equals(email)) {
                return "email.empty";
            }
            if("".equals(name)) {
                return "name.empty";
            }
            if("".equals(password1)) {
                return "password.empty";
            }
            if(null != User.find.where().eq("email", email).findUnique()) {
                return "email.used";
            }
            if(!password1.equals(password2)) {
                return "password.match";
            }
            return null;
        }
    }

}
