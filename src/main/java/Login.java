<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author austingolding
 */
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

=======
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
>>>>>>> origin/master
}
