package controllers;

import com.google.inject.Inject;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.session.login;
import views.html.session.register;

import static play.data.Form.form;

public class SessionController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result login() {
        return ok(login.render(formFactory.form(Login.class)));
    }

    public Result authenticate() {
        Form<Login> loginForm = formFactory.form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            User user = User.find.where().eq("email", loginForm.get().getEmail()).findUnique();
            session().clear();
            session("email", loginForm.get().getEmail());
            session("admin", user.isAdmin() ? "true" : "false");
            return redirect(routes.MealController.list());
        }
    }

    public Result logout() {
        session().clear();
        return redirect(routes.SessionController.login());
    }

    public Result registrationForm() {
        return ok(register.render(formFactory.form(Registration.class)));
    }

    public Result register() {
        Form<Registration> registrationForm = formFactory.form(Registration.class).bindFromRequest();
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

        protected String email;
        protected String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String validate() {
            if (User.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }

    public static class Registration {
        protected String email;
        protected String name;
        protected String password1;
        protected String password2;
        protected String code;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword1() {
            return password1;
        }

        public void setPassword1(String password1) {
            this.password1 = password1;
        }

        public String getPassword2() {
            return password2;
        }

        public void setPassword2(String password2) {
            this.password2 = password2;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

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
            if(!code.equals("Taubsi")) {
                return "code.invalid";
            }
            return null;
        }
    }

}
