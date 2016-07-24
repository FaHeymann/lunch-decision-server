package controllers;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class AdminSecured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        if (ctx.session().get("admin") != null && ctx.session().get("admin").equals("true")) {
            return ctx.session().get("email");
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.SessionController.login());
    }
}
