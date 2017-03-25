import org.jinstagram.entity.common.Location;
import twitter4j.Place;
import org.jinstagram.entity.locations.*;

import java.io.Serializable;

/**
 * Created by austingolding on 3/2/17.
 */
public class SinglePost implements Serializable {

    public String getName() {
        return name;
    }

    public String getPost() {
        return post;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public Coordinates getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public String getLoc() {
        return loc;
    }


    public String getUrl() {
        return url;
    }

    private String type;
    private String name;
    private String post;
    private String timestamp;
    private Coordinates location;
    private String loc;
    private String url;

    public SinglePost(String name, String post, String timestamp) {
        this.name = name;
        this.post = post;
        this.timestamp = timestamp;
    }

    public SinglePost(String type, String name, String post, String timestamp, Coordinates location, String url) {
        this.type = type;
        this.name = name;
        this.post = post;
        this.timestamp = timestamp;
        this.location = location;
        this.url = url;
    }

    public SinglePost(String type, String name, String post, String timestamp, String loc) {
        this.type = type;
        this.name = name;
        this.post = post;
        this.timestamp = timestamp;
        this.loc = loc;
    }

    public SinglePost(String type, String name, String post, String timestamp) {
        this.type = type;
        this.name = name;
        this.post = post;
        this.timestamp = timestamp;
    }


}
