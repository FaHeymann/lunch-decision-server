package controllers;


import models.User;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

public class ApiSecured extends Security.Authenticator {

    @Override
    public String getUsername(Http.Context ctx) {
        String token = ctx.request().getHeader("X-Auth");
        if(token == null) {
            return null;
        }
        User user = User.find.where().eq("api_token", token).findUnique();
        return user == null ? null : user.getEmail();
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized(Json.toJson("Unauthorized"));
    }
}
