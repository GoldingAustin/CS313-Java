import facebook4j.*;
import twitter4j.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by austingolding on 3/9/17.
 */
@WebServlet(name = "MapServlet", urlPatterns = {"/Map"})
public class MapServlet extends HttpServlet {
    Twitter twitter = TwitterFactory.getSingleton();
    private List<SinglePost> posts = new ArrayList<>();
    private List<Status> tweets;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Query query = new Query("Star Wars");
            QueryResult result;
            do {

                result = twitter.search(query);
                tweets = result.getTweets();


            } while ((query = result.nextQuery()) != null);
            for (Status tweet : tweets) {
                posts.add(new SinglePost(tweet.getUser().getName(), tweet.getText(), tweet.getCreatedAt().toString(), tweet.getPlace()));
            }
        }
        catch (TwitterException e) {
            e.printStackTrace();
        }

        request.setAttribute("posts", posts);
        request.setAttribute("apiKey", "AIzaSyAdsjlZu63hLWiK0D02VRC1cj2JdlDsgNU");
        request.getRequestDispatcher("/map.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("apiKey", "AIzaSyAdsjlZu63hLWiK0D02VRC1cj2JdlDsgNU");
        request.getRequestDispatcher("/map.jsp").forward(request, response);
    }
}
