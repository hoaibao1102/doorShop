/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.AdminDAO;
import dto.Admin;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import utils.PasswordUtils;

/**
 *
 * @author MSI PC
 */
@WebServlet(name = "AdminController", urlPatterns = {"/AdminController"})
public class AdminController extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();
    private static final String ADMIN_LIST_PAGE = "admin/admin-list.jsp";
    private static final String ADMIN_FORM_PAGE = "admin/admin-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllAdmins(request, response);
            } else {
                switch (action) {
                    case "viewAllAdmins":
                        url = handleViewAllAdmins(request, response);
                        break;
                    case "addAdmin":
                        url = handleAddAdmin(request, response);
                        break;
                    case "editAdmin":
                        url = handleEditAdmin(request, response);
                        break;
                    case "deleteAdmin":
                        url = handleDeleteAdmin(request, response);
                        break;
                    case "searchAdmin":
                        url = handleSearchAdmin(request, response);
                        break;
                    case "changePassword":
                        url = handleChangePassword(request, response);
                        break;
                    default:
                        request.setAttribute("error", "Invalid action: " + action);
                        url = ERROR_PAGE;
                }
            }
        } catch (Exception e) {
            request.setAttribute("error", "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    private String handleViewAllAdmins(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Admin> admins = adminDAO.getAll();
            request.setAttribute("admins", admins);
            return ADMIN_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading admins: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddAdmin(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                if (username == null || username.trim().isEmpty() || 
                    password == null || password.trim().isEmpty()) {
                    request.setAttribute("error", "Username and password are required");
                    return ADMIN_FORM_PAGE;
                }

                // Hash password
                String hashedPassword = PasswordUtils.encryptSHA256(password);

                Admin admin = new Admin();
                admin.setUsername(username.trim());
                admin.setPassword_hash(hashedPassword);
                admin.setEmail(email != null ? email.trim() : "");
                admin.setPhone(phone != null ? phone.trim() : "");

                boolean success = adminDAO.create(admin);
                if (success) {
                    request.setAttribute("success", "Admin created successfully");
                    return handleViewAllAdmins(request, response);
                } else {
                    request.setAttribute("error", "Failed to create admin");
                    return ADMIN_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error creating admin: " + e.getMessage());
                return ADMIN_FORM_PAGE;
            }
        } else {
            // Show form
            request.setAttribute("action", "add");
            return ADMIN_FORM_PAGE;
        }
    }

    private String handleEditAdmin(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int adminId = Integer.parseInt(request.getParameter("adminId"));
                String username = request.getParameter("username");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");

                Admin admin = adminDAO.getById(adminId);
                if (admin == null) {
                    request.setAttribute("error", "Admin not found");
                    return ERROR_PAGE;
                }

                admin.setUsername(username != null ? username.trim() : admin.getUsername());
                admin.setEmail(email != null ? email.trim() : admin.getEmail());
                admin.setPhone(phone != null ? phone.trim() : admin.getPhone());

                // Note: For security, password update should be handled separately
                
                request.setAttribute("success", "Admin updated successfully");
                return handleViewAllAdmins(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error updating admin: " + e.getMessage());
                return ADMIN_FORM_PAGE;
            }
        } else {
            // Show form with existing data
            try {
                int adminId = Integer.parseInt(request.getParameter("id"));
                Admin admin = adminDAO.getById(adminId);
                if (admin == null) {
                    request.setAttribute("error", "Admin not found");
                    return ERROR_PAGE;
                }
                request.setAttribute("admin", admin);
                request.setAttribute("action", "edit");
                return ADMIN_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading admin: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleDeleteAdmin(HttpServletRequest request, HttpServletResponse response) {
        try {
            int adminId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllAdmins(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting admin: " + e.getMessage());
            return handleViewAllAdmins(request, response);
        }
    }

    private String handleSearchAdmin(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<Admin> admins;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                admins = adminDAO.getByName(searchTerm.trim());
            } else {
                admins = adminDAO.getAll();
            }
            
            request.setAttribute("admins", admins);
            request.setAttribute("searchTerm", searchTerm);
            return ADMIN_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching admins: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleChangePassword(HttpServletRequest request, HttpServletResponse response) {
        try {
            String username = request.getParameter("username");
            String oldPassword = request.getParameter("oldPassword");
            String newPassword = request.getParameter("newPassword");

            if (username == null || oldPassword == null || newPassword == null ||
                username.trim().isEmpty() || oldPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
                request.setAttribute("error", "All fields are required");
                return ADMIN_FORM_PAGE;
            }

            // Verify old password
            if (!adminDAO.login(username, oldPassword)) {
                request.setAttribute("error", "Current password is incorrect");
                return ADMIN_FORM_PAGE;
            }

            // Update password
            String hashedNewPassword = PasswordUtils.encryptSHA256(newPassword);
            boolean success = adminDAO.updatePassword(username, hashedNewPassword);
            
            if (success) {
                request.setAttribute("success", "Password updated successfully");
            } else {
                request.setAttribute("error", "Failed to update password");
            }
            
            return handleViewAllAdmins(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error changing password: " + e.getMessage());
            return ADMIN_FORM_PAGE;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Admin Controller for managing admin users";
    }
}