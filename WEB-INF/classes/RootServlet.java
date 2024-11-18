import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/")
public class RootServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Example: Set dynamic content
        String libraryName = "MLK Library";
//        String libraryLogo = "logo.png"; // Path to the logo image

        // Pass the data to the JSP
        request.setAttribute("libraryName", libraryName);
//        request.setAttribute("libraryLogo", libraryLogo);

        // Forward to the JSP page
        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}
