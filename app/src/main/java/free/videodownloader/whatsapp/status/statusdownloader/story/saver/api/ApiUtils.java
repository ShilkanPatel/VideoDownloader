package free.videodownloader.whatsapp.status.statusdownloader.story.saver.api;

public class ApiUtils {
    private ApiUtils() {
    }

    public static Appservice getAPIService(String str) {
        return (Appservice) ApiClient.getClient(str).create(Appservice.class);
    }
}
