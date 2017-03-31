import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.restfb.*;
import com.restfb.Version;
import com.restfb.types.Place;
import com.restfb.types.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by austingolding on 3/9/17.
 */
@WebServlet(name = "MapServlet", urlPatterns = {"/Map"})
public class MapServlet extends HttpServlet {
    private List<SinglePost> posts = new ArrayList<>();
    private List<twitter4j.Status> tweets;
    private FacebookClient facebookClient23;
    private GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAdsjlZu63hLWiK0D02VRC1cj2JdlDsgNU");
    private static YouTube youtube;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        posts.clear();
        String search = request.getParameter("search");
        String lat = request.getParameter("centerLat");
        String lng = request.getParameter("centerLong");
        String radius = request.getParameter("radius");
        if (!search.isEmpty()) {
            getTwitterPosts(search);
            getYoutubeVideos(search,"41.462937", "-107.968523", "1000000");
        } else {
            getFacebookPosts(lat, lng, radius);
            getYoutubeVideos(search,lat, lng, radius);
        }
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/map.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("/map.jsp").forward(request, response);
    }

    private twitter4j.Twitter setTwitter() {
        twitter4j.conf.ConfigurationBuilder cb = new twitter4j.conf.ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(Constants.TWITTER_API)
                .setOAuthConsumerSecret(Constants.TWITTER_API_SECRET)
                .setOAuthAccessToken(Constants.TWITTER_AUTH)
                .setOAuthAccessTokenSecret(Constants.TWITTER_AUTH_TOKEN);
        twitter4j.TwitterFactory tf = new twitter4j.TwitterFactory(cb.build());
        twitter4j.Twitter twitter = tf.getInstance();
        return twitter;
    }

    private Coordinates getCoordinates(String location) {
        GeocodingResult[] results = new GeocodingResult[0];
        try {
            results = GeocodingApi.geocode(context, location).await();
            return new Coordinates(results[0].geometry.location.lat, results[0].geometry.location.lng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getYoutubeVideos(String query, String lat, String lng, String radius) {
        try {
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("PostMap").build();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            double tempRadius = Double.parseDouble(radius) * .001;
            String apiKey = "AIzaSyAdsjlZu63hLWiK0D02VRC1cj2JdlDsgNU";
            search.setKey(apiKey);
            if (!query.isEmpty()) {
                search.setQ(query);
            }
            search.setLocation(lat + "," + lng);
            search.setLocationRadius(String.valueOf(tempRadius) + "km");

            search.setType("video");

            search.setMaxResults(Long.valueOf(10));

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            List<String> videoIds = new ArrayList<String>();

            if (searchResultList != null) {

                for (SearchResult searchResult : searchResultList) {
                    videoIds.add(searchResult.getId().getVideoId());
                }
                Joiner stringJoiner = Joiner.on(',');
                String videoId = stringJoiner.join(videoIds);


                YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet, recordingDetails").setId(videoId);
                listVideosRequest.setKey(apiKey);
                VideoListResponse listResponse = listVideosRequest.execute();

                List<Video> videoList = listResponse.getItems();

                for (Video video : videoList) {
                    Coordinates coordinates = new Coordinates(video.getRecordingDetails().getLocation().getLatitude(), video.getRecordingDetails().getLocation().getLongitude());
                    String url = "https://www.youtube.com/watch?v=" + video.getId();
                    SinglePost singlePost = new SinglePost("Youtube", video.getSnippet().getChannelTitle(), video.getSnippet().getTitle(), video.getSnippet().getPublishedAt().toString(), coordinates, url);
                    posts.add(singlePost);
                }
            }
        }catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        }
    }

    private void getTwitterPosts(String search) {
        twitter4j.Twitter twitter = setTwitter();
        try {
            twitter4j.Query query = new twitter4j.Query(search);
            twitter4j.GeoLocation geoLocation = new twitter4j.GeoLocation(0.0, 0.0);
            query.setGeoCode(geoLocation, 6371, twitter4j.Query.KILOMETERS);
            query.setCount(15);
            query.setLang("en");
            twitter4j.QueryResult result;
            do {
                result = twitter.search(query);
                tweets = result.getTweets();
            } while ((query = result.nextQuery()) != null);
            for (twitter4j.Status tweet : tweets) {
                twitter4j.GeoLocation geoLocation1 = tweet.getGeoLocation();
                String geoLocation2 = tweet.getUser().getLocation();
                SinglePost singlePost;
                if (geoLocation1 != null) {
                    Coordinates coordinates = getCoordinates(geoLocation1.toString());
                    String url = "https://twitter.com/" + tweet.getUser().getScreenName() + "/status/" + tweet.getId();
                    singlePost = new SinglePost("Twitter", tweet.getUser().getName(), tweet.getText(), tweet.getCreatedAt().toString(), coordinates, url);
                    posts.add(singlePost);
                } else if (!geoLocation2.isEmpty()) {
                    Coordinates coordinates = getCoordinates(geoLocation2);
                    if (coordinates != null) {
                        String url = "https://twitter.com/" + tweet.getUser().getScreenName() + "/status/" + tweet.getId();
                        singlePost = new SinglePost("Twitter", tweet.getUser().getName(), tweet.getText(), tweet.getCreatedAt().toString(), coordinates, url);
                        posts.add(singlePost);
                    }
                }
            }
        } catch (twitter4j.TwitterException e) {
            e.printStackTrace();
        }
    }


    private void getFacebookPosts(String lat, String lng, String radius) {
        FacebookClient.AccessToken accessToken =
                new DefaultFacebookClient().obtainAppAccessToken(Constants.MY_APP_ID, Constants.MY_APP_SECRET);
        facebookClient23 = new DefaultFacebookClient(accessToken.getAccessToken(), Version.VERSION_2_3);

        Connection<Place> publicSearch = facebookClient23.fetchConnection("search", Place.class, Parameter.with("center", lat + ", " + lng),
                Parameter.with("distance", radius), Parameter.with("type", "place"));

        List<Place> fbPosts = publicSearch.getData();

        for (Place place : fbPosts) {
            String id = place.getId();
            Connection<Post> commentConnection
                    = facebookClient23.fetchConnection(id + "/tagged",
                    Post.class, Parameter.with("limit", 3));
            List<Post> pgPostList = commentConnection.getData();
            for (Post pgPost : pgPostList) {
                System.out.println(pgPost.toString());
                String url = "https://www.facebook.com/search/posts/?q=" + place.getName();
                Coordinates coordinates = getCoordinates(place.getName());
                SinglePost singlePost = new SinglePost("Facebook",
                        place.getName(),
                        pgPost.getMessage(),
                        "Unavailable",
                        coordinates,
                        url);
                posts.add(singlePost);
            }
        }
    }
}
