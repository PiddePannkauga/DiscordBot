package Bot.YoutubeApi;

import com.google.gson.*;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * @author Petter Månsson 2018-03-20
 */
public class YoutubeApi {

    private final String baseUrl = "https://www.googleapis.com/youtube/v3/";
    private final String searchURL = "search?q=";
    private final String searchParts = "&part=snippet";
    private String API_KEY = "";
    private Client client;


    public YoutubeApi()  {
        client  = Client.create();
        try {
            API_KEY = "&key="+URLEncoder.encode(System.getenv("YOUTUBE_API_KEY"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String youtubeURLfromSearch(String search){
        WebResource resource = client.resource(baseUrl+searchURL+search+API_KEY+searchParts);
        JsonObject response = jsonArrayBuilder(resource.get(String.class));

        JsonArray array = response.getAsJsonArray("items");
        JsonElement element = array.get(0);
        JsonObject videoID = (JsonObject)element.getAsJsonObject().get("id");

        String toReturn = videoID.get("videoId").toString();
        toReturn=toReturn.substring(1,toReturn.length()-1);
        return toReturn;
    }

    private JsonObject jsonArrayBuilder(String jsonString){
        JsonParser parser = new JsonParser();
        JsonObject jsonObj = parser.parse(jsonString).getAsJsonObject();
        return jsonObj;

    }



}
