package utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.regex.Pattern;
import dto.Admin;

public class AuthUtils {

    private static String EMAIL_REGEX = "^[A-Za-z0-9_+&*-]+@[A-Za-z0-9]+\\.[a-zA-Z]{2,4}$";
    private static String PHONE_REGEX = "^(09|08|07|05|03)\\d{8}$";

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        return Pattern.matches(PHONE_REGEX, phone);
    }

    public static Admin getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            return (Admin) session.getAttribute("user");
        }
        return null;
    }

    public static boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }

    public static String getAccessDeniedMessage(String action) {
        return "You can not access to this page. Please contact administrator!";
    }

    public static String getLoginURL() {
        return "login.jsp";
    }
}
