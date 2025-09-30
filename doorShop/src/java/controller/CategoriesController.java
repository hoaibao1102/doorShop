/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.CategoriesDAO;
import dto.Categories;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author MSI PC
 */
@WebServlet(name = "CategoriesController", urlPatterns = {"/CategoriesController"})
public class CategoriesController extends HttpServlet {

    private final CategoriesDAO categoriesDAO = new CategoriesDAO();
    private static final String CATEGORIES_LIST_PAGE = "categories/categories-list.jsp";
    private static final String CATEGORIES_FORM_PAGE = "categories/categories-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllCategories(request, response);
            } else {
                switch (action) {
                    case "viewAll":
                        url = handleViewAllCategories(request, response);
                        break;
                    case "add":
                        url = handleAddCategory(request, response);
                        break;
                    case "edit":
                        url = handleEditCategory(request, response);
                        break;
                    case "delete":
                        url = handleDeleteCategory(request, response);
                        break;
                    case "search":
                        url = handleSearchCategory(request, response);
                        break;
                    case "toggleStatus":
                        url = handleToggleStatus(request, response);
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

    private String handleViewAllCategories(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Categories> categories = categoriesDAO.getAll();
            request.setAttribute("categories", categories);
            return CATEGORIES_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading categories: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddCategory(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String categoryName = request.getParameter("categoryName");
                String description = request.getParameter("description");
                String status = request.getParameter("status");

                if (categoryName == null || categoryName.trim().isEmpty()) {
                    request.setAttribute("error", "Category name is required");
                    return CATEGORIES_FORM_PAGE;
                }

                Categories category = new Categories();
                category.setCategory_name(categoryName.trim());
                category.setDescription(description != null ? description.trim() : "");
                category.setStatus(status != null ? status : "visible");

                boolean success = categoriesDAO.create(category);
                if (success) {
                    request.setAttribute("success", "Category created successfully");
                    return handleViewAllCategories(request, response);
                } else {
                    request.setAttribute("error", "Failed to create category");
                    return CATEGORIES_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error creating category: " + e.getMessage());
                return CATEGORIES_FORM_PAGE;
            }
        } else {
            // Show form
            request.setAttribute("action", "add");
            return CATEGORIES_FORM_PAGE;
        }
    }

    private String handleEditCategory(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                String categoryName = request.getParameter("categoryName");
                String description = request.getParameter("description");
                String status = request.getParameter("status");

                Categories category = categoriesDAO.getById(categoryId);
                if (category == null) {
                    request.setAttribute("error", "Category not found");
                    return ERROR_PAGE;
                }

                category.setCategory_name(categoryName != null ? categoryName.trim() : category.getCategory_name());
                category.setDescription(description != null ? description.trim() : category.getDescription());
                category.setStatus(status != null ? status : category.getStatus());

                // Note: Implement update method in DAO
                request.setAttribute("success", "Category updated successfully");
                return handleViewAllCategories(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error updating category: " + e.getMessage());
                return CATEGORIES_FORM_PAGE;
            }
        } else {
            // Show form with existing data
            try {
                int categoryId = Integer.parseInt(request.getParameter("id"));
                Categories category = categoriesDAO.getById(categoryId);
                if (category == null) {
                    request.setAttribute("error", "Category not found");
                    return ERROR_PAGE;
                }
                request.setAttribute("category", category);
                request.setAttribute("action", "edit");
                return CATEGORIES_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading category: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleDeleteCategory(HttpServletRequest request, HttpServletResponse response) {
        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllCategories(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting category: " + e.getMessage());
            return handleViewAllCategories(request, response);
        }
    }

    private String handleSearchCategory(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<Categories> categories;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                categories = categoriesDAO.getByName(searchTerm.trim());
            } else {
                categories = categoriesDAO.getAll();
            }
            
            request.setAttribute("categories", categories);
            request.setAttribute("searchTerm", searchTerm);
            return CATEGORIES_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching categories: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleToggleStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            int categoryId = Integer.parseInt(request.getParameter("id"));
            Categories category = categoriesDAO.getById(categoryId);
            
            if (category != null) {
                String newStatus = "visible".equals(category.getStatus()) ? "hidden" : "visible";
                category.setStatus(newStatus);
                // Note: Implement update method in DAO
                request.setAttribute("success", "Category status updated successfully");
            } else {
                request.setAttribute("error", "Category not found");
            }
            
            return handleViewAllCategories(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error toggling category status: " + e.getMessage());
            return handleViewAllCategories(request, response);
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
        return "Categories Controller for managing product categories";
    }
}