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
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    private List<User> users = new ArrayList<>();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String username = request.getParameter("name");
            String password = request.getParameter("pass");
            readFromFile();
            switch (checkUser(username,password)){
                case 0: request.getRequestDispatcher("/invalidLogin.jsp").forward(request, response);
                        break;
                case 1: request.getSession().setAttribute("user", username);
                        request.getRequestDispatcher("/newPost.jsp").forward(request, response);
                        break;
                case 2: User user = new User (username, password);
                        users.add(user);
                        writeToFile();
                        request.getSession().setAttribute("user", username);
                        request.getRequestDispatcher("/newPost.jsp").forward(request, response);
            }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void writeToFile() throws IOException {
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
        try{
            fout = new FileOutputStream("users.txt", false);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(users);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(oos  != null){
                oos.close();
            }
        }
    }

    private int checkUser(String name, String pass) throws IOException {
            for (User user : users) {
                if (name.equals(user.getUsername()) && pass.equals(user.getPassword())) {
                    return 1;
                } else if (name.equals(user.getUsername()) && !pass.equals(user.getPassword())) {
                    return 0;
                }
            }
        return 2;
    }

    private void readFromFile() throws IOException {
        ObjectInputStream objectInputStream = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("users.txt");
            ObjectInputStream objectinputstream = new ObjectInputStream(fileInputStream);
            List<User> readUser = (ArrayList<User>) objectinputstream.readObject();
            users = readUser;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(objectInputStream != null){
                objectInputStream.close();
            }
        }

    }
}
