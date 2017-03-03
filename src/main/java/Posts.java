import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by austingolding on 3/2/17.
 */
@WebServlet(name = "Posts", urlPatterns = {"/Posts"})
public class Posts extends HttpServlet {

    private List<SinglePost> posts = new ArrayList<>();
    private String timeComp = "";
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String timestamp = request.getParameter("timestamp");
        readFromFile();
        if (posts.size() > 1) {
            timeComp = posts.get((posts.size() - 1)).getTimestamp();
        }
        if (timeComp.equals(timestamp)) {
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("viewPosts.jsp").forward(request,response);
        } else {
            String post = request.getParameter("post");
            String name = request.getSession().getAttribute("user").toString();
            SinglePost singlePost = new SinglePost(name, post, timestamp);
            timeComp = timestamp;
            posts.add(singlePost);
            writeToFile();
            request.setAttribute("posts", posts);
            request.getRequestDispatcher("viewPosts.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        readFromFile();
        request.setAttribute("posts", posts);
        request.getRequestDispatcher("viewPosts.jsp").forward(request,response);
    }

    private void writeToFile() throws IOException {
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try{
            fout = new FileOutputStream("posts.txt", false);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(posts);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(oos  != null){
                oos.close();
            }
        }
    }

    private void readFromFile() throws IOException {
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("posts.txt");
            ObjectInputStream objectinputstream = new ObjectInputStream(fileInputStream);
            List<SinglePost> readPosts = (ArrayList<SinglePost>) objectinputstream.readObject();
            posts = readPosts;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(objectInputStream != null){
                objectInputStream.close();
            }
        }
    }

}
