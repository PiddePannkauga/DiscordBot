package Bot.YoutubeApi;

import Utility.FileReader;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.ArrayMap;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoLocalization;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;;

/**
 * @author Petter Månsson 2018-03-19
 */
public class YoutubeDataFetch {

     String API_TOKEN = FileReader.readFile("apikey.json");
    static String datafetch = "https://www.googleapis.com/youtube/v3/videos?id=7lCDEYXw3mM&key=" +
            "&fields=items(id,snippet(channelId,title,categoryId),statistics)&part=snippet,statistics";
    private static YouTube youtube;



    public static void main(String[] args) throws IOException {
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        try {
            // Authorize the request.
            Credential credential = YoutubeAuth.authorize(scopes, "localizations");

            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(YoutubeAuth.HTTP_TRANSPORT, YoutubeAuth.JSON_FACTORY, credential)
                    .setApplicationName("youtube-cmdline-localizations-sample").build();


            listVideoLocalizations(getId("video"));


        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }

    }

    private static void listVideoLocalizations(String videoId) throws IOException {
        // Call the YouTube Data API's videos.list method to retrieve videos.
        VideoListResponse videoListResponse = youtube.videos().
                list("snippet,localizations").setId(videoId).execute();

        // Since the API request specified a unique video ID, the API
        // response should return exactly one video. If the response does
        // not contain a video, then the specified video ID was not found.
        List<Video> videoList = videoListResponse.getItems();
        if (videoList.isEmpty()) {
            System.out.println("Can't find a video with ID: " + videoId);
            return;
        }
        Video video = videoList.get(0);
        Map<String, VideoLocalization> localizations = video.getLocalizations();
        System.out.println(localizations);
        // Print information from the API response.
        System.out.println("\n================== Video ==================\n");
        System.out.println("  - ID: " + video.getId());
        for (String language : localizations.keySet()) {
            System.out.println(language);
            System.out.println("  - Title(" + language + "): " +
                    localizations.get(language).getTitle());
            System.out.println("  - Description(" + language + "): " +
                    localizations.get(language).getDescription());
        }
        System.out.println("\n-------------------------------------------------------------\n");
    }

    private static String getId(String resource) throws IOException {

        String id = "";

        System.out.print("Please enter a " + resource + " id: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        id = bReader.readLine();

        System.out.println("You chose " + id + " for localizations.");
        return id;
    }

}
