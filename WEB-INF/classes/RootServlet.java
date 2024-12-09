import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;
import java.io.IOException;
import java.sql.Connection;
import java.util.stream.Collectors;


public class RootServlet extends HttpServlet {


        User admin = new User(1, "admin", "1112223344", "admin@oasisLMS.io", UserDAO.hashPassword("password_admin"), true);
        User user1 = new User(2, "user", "4443332211", "user@gmail.com", UserDAO.hashPassword("password_user"), false);
        UserDAO tmp_dao;

    public void init() throws ServletException{
        tmp_dao = new UserDAO();


        tmp_dao.delete(1);
        tmp_dao.delete(2);

        tmp_dao.save(admin);
        tmp_dao.save(user1);
    }

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

        switch (path) {
            case "/":
                handleHome(request, response);
                break;
            case "/dashboard":
                handleDashboard(request, response, session);
                break;
            case "/login":
                handleDashboard(request, response, session);
                break;
            case "/logout":
                handleDashboard(request, response, session);
                break;
            // case "/profile":
            //     handleProfile(request, response);
            //     break;
            // case "/settings":
            //     handleSettings(request, response);
            //     break;
            default:
                handleNotFound(request, response);
        }
    }

        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Determine the requested route
        String path = request.getServletPath();

        //Get Session
        HttpSession session = request.getSession();


        // Log session details
        logger.info("=== Session Details ===");
        logger.info("Session ID: " + session.getId());
        logger.info("Creation Time: " + new java.util.Date(session.getCreationTime()));
        logger.info("Last Accessed Time: " + new java.util.Date(session.getLastAccessedTime()));

        switch (path) {
            // case "/":
            //     handleHome(request, response);
            //     break;
            // case "/dashboard":
            //     handleDashboard(request, response, session);
            //     break;
            // case "/profile":
            //     handleProfile(request, response);
            //     break;
            // case "/settings":
            //     handleSettings(request, response);
            //     break;
            case "/login":
                handleLogin(request, response, session);
                break;
            case "/logout":
                handleLogout(request, response, session);
                break;
            default:
                handleNotFound(request, response);
        }
    }

    private void handleHome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	request.setAttribute("libraryName", "MLK Library");
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }

    private void handleDashboard(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        if (!UserDAO.isLoggedIn(session)){
            response.sendRedirect("/");
            return;
        }
        

        request.setAttribute("message", "Welcome to your Dashboard!");
        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        if (UserDAO.isLoggedIn(request.getSession())){
            response.sendRedirect("/dashboard");
            return;
        }

        logger.info("=== Login Request ===");
        // Parse form parameters
        String email = request.getParameter("user");
        String password = request.getParameter("password");

        User user = tmp_dao.get(email, UserDAO.hashPassword(password));
        if (user != null){
            logger.info("=== Login Successfull ===");
            logger.info("User: " + user.getEmail());
            logger.info("isAdmin: " + user.isAdmin());
            request.getSession().setAttribute("user_id", user.id);
            request.setAttribute("isAdmin", user.isAdmin());                
            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("/");
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        if (UserDAO.isLoggedIn(request.getSession())){
            
            logger.info("=== Logout Request ===");
            
            request.getSession().removeAttribute("user_id");
            response.sendRedirect("/");
            return;
        }

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
