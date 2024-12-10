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
import java.util.Date;
import java.util.Calendar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

public class RootServlet extends HttpServlet {


        User admin = new User(1, "admin", "1112223344", "admin@oasisLMS.io", UserDAO.hashPassword("password_admin"), true);
        User user1 = new User(2, "bob", "4443332211", "user@gmail.com", UserDAO.hashPassword("password_user"), false);
        UserDAO tmp_dao;
        BookDAO bd = new BookDAO();
        TransactionDAO td = new TransactionDAO();
        Gson gson = new Gson(); // Gson for JSON handling

    public void init() throws ServletException{
        tmp_dao = new UserDAO();
        bd = new BookDAO();
        td = new TransactionDAO();
        
        // for(User u : tmp_dao.getAll()){
        //     tmp_dao.delete(u.id);
        // }
        // tmp_dao.save(admin);
        // tmp_dao.save(user1);

        Book b1 = new Book(1, "The Art of Thinking Clearly", "Rolf Dobelli", "9781444759563");
        Book b2 = new Book(2, "Sapiens: A Brief History of Humankind", "Yuval Noah Harari", "9780062316097");
        Book b3 = new Book(3, "Atomic Habits", "James Clear", "9780735211292");
        Book b4 = new Book(4, "To Kill a Mockingbird", "Harper Lee", "9780060935467");
        Book b5 = new Book(5, "1984", "George Orwell", "9780451524935");
        // Save the books using the BookDAO
        // bd.save(b1);
        // bd.save(b2);
        // bd.save(b3);
        // bd.save(b4);
        // bd.save(b5);

        // Get the current date
        Date checkoutDate = new Date();

        // Create a calendar instance and add 7 days for the return date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkoutDate);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date returnDate = calendar.getTime();


        // Create a Transaction object
        Transaction transaction = new Transaction(1, user1, b4, checkoutDate, returnDate, false);
        //td.save(transaction);
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
            default:
                response.sendRedirect("/");
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
            case "/dashboard":
                handleDashboard(request, response, session);
                break;
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
                response.sendRedirect("/");
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
        User user = tmp_dao.get((Integer) session.getAttribute("user_id"));        
        String method = request.getMethod();


        if (method.equals("GET")) {
            request.setAttribute("user", user);
            request.setAttribute("transactions", td.getAllPending(user.id));
            request.setAttribute("libraryName", "SJSU");

            request.setAttribute("checked_count", td.getAllPending(user.id).size());
            request.setAttribute("total_count", td.getAll(user.id).size());
            request.setAttribute("due_count", td.getDueTransactions(user.id, 3).size());

            if (user.isAdmin()){
                request.setAttribute("admin_total_books", bd.getAll().size());
                request.setAttribute("admin_total_checked", td.getAllPending().size());
                request.setAttribute("admin_active_users", tmp_dao.getAll().size());
                request.setAttribute("users", tmp_dao.getAll());
            }

            request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
        }else if (method.equals("POST")){
    try {
                 // Parse the JSON payload
                JsonObject requestData = gson.fromJson(request.getReader(), JsonObject.class);
                String action = requestData.has("action") ? requestData.get("action").getAsString() : null;

                JsonObject jsonResponse = new JsonObject();

                if (action == null) {
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Action is missing or invalid.");
                    response.getWriter().write(gson.toJson(jsonResponse));
                    return;
                }

                User tmp_user = (User) request.getSession().getAttribute("user");

                switch (action) {
                    case "return":
                        handleReturnAction(requestData, jsonResponse, tmp_user);
                        break;

                    case "add_book":
                        handleAddBookAction(requestData, jsonResponse);
                        break;

                    case "add_user":
                        handleAddUserAction(requestData, jsonResponse, tmp_user);
                        break;

                    case "check_out_book":
                        handleCheckOutBookAction(requestData, jsonResponse);
                        break;

                    case "search":
                        handleSearchAction(requestData, jsonResponse);
                        break;

                    case "db_dump":
                        handleDatabaseDumpAction(jsonResponse);
                        break;

                    default:
                        jsonResponse.addProperty("status", "error");
                        jsonResponse.addProperty("message", "Unknown action: " + action);
                }

                // Respond with JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(jsonResponse));
            } catch (JsonSyntaxException e) {
                // Handle invalid JSON input
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                JsonObject errorResponse = new JsonObject();
                errorResponse.addProperty("status", "error");
                errorResponse.addProperty("message", "Invalid JSON format.");
                response.getWriter().write(gson.toJson(errorResponse));
            }
        }
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
            request.getSession().setAttribute("user", user);
            response.sendRedirect("/dashboard");
            return;
        }

        response.sendRedirect("/");
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        if (UserDAO.isLoggedIn(request.getSession())){
            
            logger.info("=== Logout Request ===");
            
            request.getSession(false).invalidate();
            response.sendRedirect("/");
            return;
        }

    }

    private void handleNotFound(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("404 - Page not found");
    }

    // Handle Return Action
    private void handleReturnAction(JsonObject requestData, JsonObject jsonResponse, User user) {
        Transaction tmp_transaction;
        JsonObject data = requestData.getAsJsonObject("data");

        Integer book_id = data.has("id") ? data.get("id").getAsInt() : null;

        if (book_id == null){
            jsonResponse.addProperty("status", "fail");
            jsonResponse.addProperty("message", "id returned a null object. book does not exit.");
            return;
        }

        tmp_transaction = td.get(user.id, book_id);
        if (tmp_transaction == null){
            jsonResponse.addProperty("status", "fail");
            jsonResponse.addProperty("message", "Could not find database record, please contact admin.");
            return;
        }
        tmp_transaction.setCompleted();
        td.update(tmp_transaction);

        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("data", user.toString());
        // Add database logic for handling book return here
    }

    // Handle Add Book Action
    private void handleAddBookAction(JsonObject requestData, JsonObject jsonResponse) {
        if (!requestData.has("data")) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing data payload.");
            return;
        }

        JsonObject data = requestData.getAsJsonObject("data");
        String title = data.has("title") ? data.get("title").getAsString() : null;
        String author = data.has("author") ? data.get("author").getAsString() : null;
        String isbn = data.has("isbn") ? data.get("isbn").getAsString() : null;

        if (title == null || author == null || isbn == null) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing required fields: title, author, or ISBN.");
            return;
        }

        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", "Book added successfully.");
        jsonResponse.addProperty("title", title);
        jsonResponse.addProperty("author", author);
        jsonResponse.addProperty("isbn", isbn);
        // Add database logic for adding a book here
    }

    // Handle Add User Action
    private void handleAddUserAction(JsonObject requestData, JsonObject jsonResponse, User tmp_user) {
        if(!tmp_user.isAdmin()){
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Not an admin user.");
            return;
        }

        if (!requestData.has("data")) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing data payload.");
            return;
        }

        JsonObject data = requestData.getAsJsonObject("data");
        String name = data.has("name") ? data.get("name").getAsString() : null;
        String email = data.has("email") ? data.get("email").getAsString() : null;
        String password = data.has("password") ? data.get("password").getAsString() : null;

        if (name == null || email == null || password == null) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing required fields: name, email, or password.");
            return;
        }

        User non_admin_user_to_add = new User(-1, name, "NA", email, UserDAO.hashPassword(password), false);
        if (tmp_dao.save_status(non_admin_user_to_add)){
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "User added successfully.");
            jsonResponse.addProperty("name", name);
            jsonResponse.addProperty("email", email);
        }else{
            jsonResponse.addProperty("status", "failed");
            jsonResponse.addProperty("message", "Failed to add user.");
        }
        
        // Add database logic for adding a user here
    }

    // Handle Check Out Book Action
    private void handleCheckOutBookAction(JsonObject requestData, JsonObject jsonResponse) {
        if (!requestData.has("data")) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing data payload.");
            return;
        }

        JsonObject data = requestData.getAsJsonObject("data");
        Integer bookId = data.has("bookId") ? data.get("bookId").getAsInt() : null;

        if (bookId == null) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing required field: bookId.");
            return;
        }

        jsonResponse.addProperty("status", "success");
        jsonResponse.addProperty("message", "Book checked out successfully.");
        jsonResponse.addProperty("bookId", bookId);
        // Add database logic for checking out a book here
    }

    // Handle Search Action
    private void handleSearchAction(JsonObject requestData, JsonObject jsonResponse) {
        if (!requestData.has("data")) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Missing data payload.");
            return;
        }

        JsonObject data = requestData.getAsJsonObject("data");
        String query = data.has("query") ? data.get("query").getAsString() : null;

        if (query == null || query.isEmpty()) {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Query string is missing or empty.");
            return;
        }

        // Simulate a search result
        JsonArray results = new JsonArray();
        JsonObject result1 = new JsonObject();
        result1.addProperty("title", "Example Book 1");
        result1.addProperty("author", "Author 1");
        result1.addProperty("isbn", "1234567890");
        results.add(result1);

        JsonObject result2 = new JsonObject();
        result2.addProperty("title", "Example Book 2");
        result2.addProperty("author", "Author 2");
        result2.addProperty("isbn", "0987654321");
        results.add(result2);

        jsonResponse.addProperty("status", "success");
        jsonResponse.add("results", results);
    }

    // Handle Database Dump Action
    private void handleDatabaseDumpAction(JsonObject jsonResponse) {
        // Simulate a database dump operation
        boolean success = true; // Replace with actual DB dump logic

        if (success) {
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Database dump completed successfully.");
        } else {
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Failed to complete database dump.");
        }
    }
}