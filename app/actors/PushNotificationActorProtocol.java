package actors;

import java.util.Map;

public class PushNotificationActorProtocol {
    public static class Send {
        public final String message;

        public Send(String message) {
            this.message = message;
        }
    }

    public static class Notification {
        public String title;
        public String message;

        public Notification(String title, String message) {
            this.title = title;
            this.message = message;
        }
    }

    public static class Action {
        public Map<String, String> payload;

        public Action(Map<String, String> payload) {
            this.payload = payload;
        }
    }
}
