import twitter4j.Place;

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

    private String name;
    private String post;
    private String timestamp;
    private Place location;

    public SinglePost(String name, String post, String timestamp) {
        this.name = name;
        this.post = post;
        this.timestamp = timestamp;
    }

    public SinglePost(String name, String post, String timestamp, Place location) {
        this.name = name;
        this.post = post;
        this.timestamp = timestamp;
        this.location = location;
    }

    @Override
    public String toString() {
        return "SinglePost{" +
                "name='" + name + '\'' +
                ", post='" + post + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
