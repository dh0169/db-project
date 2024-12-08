import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.Connection;

public class RootServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(RootServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Determine the requested route
        String path = request.getServletPath();

        //Get Session
        HttpSession session = request.getSession();


        // Log session details
        logger.info("=== Session Details ===");
        logger.info("Session ID: " + session.getId());
        logger.info("Creation Time: " + new java.util.Date(session.getCreationTime()));
        logger.info("Last Accessed Time: " + new java.util.Date(session.getLastAccessedTime()));


        UserDAO ud = new UserDAO();


        switch (path) {
            case "/":
                handleHome(request, response);
                break;
            case "/dashboard":
                handleDashboard(request, response);
                break;
            case "/profile":
                handleProfile(request, response);
                break;
            case "/settings":
                handleSettings(request, response);
                break;
            default:
                handleNotFound(request, response);
        }
    }

    private void handleHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setAttribute("libraryName", "MLK Library");
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", "Welcome to your Dashboard!");
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void handleProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", "This is your Profile Page.");
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }

    private void handleSettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("message", "Here are your settings.");
        request.getRequestDispatcher("/settings.jsp").forward(request, response);
    }

    private void handleNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("404 - Page not found");
    }
}
