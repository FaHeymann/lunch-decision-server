package actors;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import services.Properties;

import javax.inject.Inject;
import java.util.Map;

public class PushNotificationActor extends UntypedActor {
    @Inject
    WSClient ws;

    @Inject
    Properties properties;

    public void onReceive(Object msg) throws Exception {
        if (msg instanceof PushNotificationActorProtocol.Action) {
            ObjectNode body = this.buildBody();
            body = this.addPayload(body, ((PushNotificationActorProtocol.Action) msg).payload);
            this.postRequest(body);
        }

        if (msg instanceof  PushNotificationActorProtocol.Notification) {
            ObjectNode body = this.buildBody();
            body = this.addNotification(
                body,
                ((PushNotificationActorProtocol.Notification) msg).title,
                ((PushNotificationActorProtocol.Notification) msg).message
            );
            this.postRequest(body);
        }
    }

    private ObjectNode buildBody() {
        ArrayNode tokens = Json.newArray();
        tokens.add(this.properties.getConfig().getString("deviceToken"));

        ObjectNode body = Json.newObject();
        body.set("tokens", tokens);
        body.set("profile", Json.toJson(properties.getConfig().getString("securityProfile")));

        return body;
    }

    private void postRequest(ObjectNode body) {
        WSRequest request = ws.url("https://api.ionic.io/push/notifications")
            .setContentType("application/json")
            .setHeader("Authorization", "Bearer " + properties.getConfig().getString("apiKey"));

        request.post(body).thenApply(wsResponse -> {
            Logger.info(wsResponse.asJson().toString());
            return wsResponse;
        });
    }

    private ObjectNode addNotification(ObjectNode body, String title, String message) {
        ObjectNode notification = Json.newObject();
        notification.set("title", Json.toJson(title));
        notification.set("message", Json.toJson(message));

        body.set("notification", notification);

        return body;
    }

    private ObjectNode addPayload(ObjectNode body, Map<String, String> payload) {
        ObjectNode notification = Json.newObject();
        notification.set("payload", Json.toJson(payload));

        ObjectNode android = Json.newObject();
        android.set("content_available", Json.toJson(1));

        ObjectNode ios = Json.newObject();
        ios.set("content_available", Json.toJson(1));

        notification.set("android", android);
        notification.set("ios", ios);

        body.set("notification", notification);

        return body;
    }
}
