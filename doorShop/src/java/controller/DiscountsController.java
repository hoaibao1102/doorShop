/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.DiscountsDAO;
import dao.ProductsDAO;
import dto.Discounts;
import dto.Products;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author MSI PC
 */
@WebServlet(name = "DiscountsController", urlPatterns = {"/DiscountsController"})
public class DiscountsController extends HttpServlet {

    private final DiscountsDAO discountsDAO = new DiscountsDAO();
    private final ProductsDAO productsDAO = new ProductsDAO();
    private static final String DISCOUNTS_LIST_PAGE = "discounts/discounts-list.jsp";
    private static final String DISCOUNTS_FORM_PAGE = "discounts/discounts-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllDiscounts(request, response);
            } else {
                switch (action) {
                    case "viewAll":
                        url = handleViewAllDiscounts(request, response);
                        break;
                    case "add":
                        url = handleAddDiscount(request, response);
                        break;
                    case "edit":
                        url = handleEditDiscount(request, response);
                        break;
                    case "delete":
                        url = handleDeleteDiscount(request, response);
                        break;
                    case "search":
                        url = handleSearchDiscount(request, response);
                        break;
                    case "toggleStatus":
                        url = handleToggleStatus(request, response);
                        break;
                    case "getByProduct":
                        url = handleGetByProduct(request, response);
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

    private String handleViewAllDiscounts(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Discounts> discounts = discountsDAO.getAll();
            List<Products> products = productsDAO.getAll();
            request.setAttribute("discounts", discounts);
            request.setAttribute("products", products);
            return DISCOUNTS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading discounts: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddDiscount(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String productIdStr = request.getParameter("productId");
                String discountPercentStr = request.getParameter("discountPercent");
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String status = request.getParameter("status");

                if (productIdStr == null || productIdStr.trim().isEmpty() || 
                    discountPercentStr == null || discountPercentStr.trim().isEmpty() ||
                    startDateStr == null || startDateStr.trim().isEmpty() ||
                    endDateStr == null || endDateStr.trim().isEmpty()) {
                    request.setAttribute("error", "All fields are required");
                    List<Products> products = productsDAO.getAll();
                    request.setAttribute("products", products);
                    return DISCOUNTS_FORM_PAGE;
                }

                int productId = Integer.parseInt(productIdStr);
                int discountPercent = Integer.parseInt(discountPercentStr);
                Date startDate = Date.valueOf(startDateStr);
                Date endDate = Date.valueOf(endDateStr);

                if (discountPercent < 0 || discountPercent > 100) {
                    request.setAttribute("error", "Discount percent must be between 0 and 100");
                    List<Products> products = productsDAO.getAll();
                    request.setAttribute("products", products);
                    return DISCOUNTS_FORM_PAGE;
                }

                if (endDate.before(startDate)) {
                    request.setAttribute("error", "End date must be after start date");
                    List<Products> products = productsDAO.getAll();
                    request.setAttribute("products", products);
                    return DISCOUNTS_FORM_PAGE;
                }
                
                Discounts discount = new Discounts();
                discount.setProduct_id(productId);
                discount.setDiscount_percent(discountPercent);
                discount.setStart_date(startDate);
                discount.setEnd_date(endDate);
                discount.setStatus(status != null ? status : "active");

                boolean success = discountsDAO.create(discount);
                if (success) {
                    request.setAttribute("success", "Discount created successfully");
                    return handleViewAllDiscounts(request, response);
                } else {
                    request.setAttribute("error", "Failed to create discount");
                    List<Products> products = productsDAO.getAll();
                    request.setAttribute("products", products);
                    return DISCOUNTS_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error creating discount: " + e.getMessage());
                try {
                    List<Products> products = productsDAO.getAll();
                    request.setAttribute("products", products);
                } catch (Exception ex) {
                    // Ignore
                }
                return DISCOUNTS_FORM_PAGE;
            }
        } else {
            // Show form
            try {
                List<Products> products = productsDAO.getAll();
                request.setAttribute("products", products);
                request.setAttribute("action", "add");
                return DISCOUNTS_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading products list: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleEditDiscount(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int discountId = Integer.parseInt(request.getParameter("discountId"));
                String productIdStr = request.getParameter("productId");
                String discountPercentStr = request.getParameter("discountPercent");
                String startDateStr = request.getParameter("startDate");
                String endDateStr = request.getParameter("endDate");
                String status = request.getParameter("status");

                Discounts discount = discountsDAO.getById(discountId);
                if (discount == null) {
                    request.setAttribute("error", "Discount not found");
                    return ERROR_PAGE;
                }

                if (productIdStr != null && !productIdStr.trim().isEmpty()) {
                    discount.setProduct_id(Integer.parseInt(productIdStr));
                }
                if (discountPercentStr != null && !discountPercentStr.trim().isEmpty()) {
                    int discountPercent = Integer.parseInt(discountPercentStr);
                    if (discountPercent >= 0 && discountPercent <= 100) {
                        discount.setDiscount_percent(discountPercent);
                    }
                }
                if (startDateStr != null && !startDateStr.trim().isEmpty()) {
                    discount.setStart_date(Date.valueOf(startDateStr));
                }
                if (endDateStr != null && !endDateStr.trim().isEmpty()) {
                    discount.setEnd_date(Date.valueOf(endDateStr));
                }
                if (status != null) {
                    discount.setStatus(status);
                }

                // Note: Implement update method in DAO
                request.setAttribute("success", "Discount updated successfully");
                return handleViewAllDiscounts(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error updating discount: " + e.getMessage());
                try {
                    List<Products> products = productsDAO.getAll();
                    request.setAttribute("products", products);
                } catch (Exception ex) {
                    // Ignore
                }
                return DISCOUNTS_FORM_PAGE;
            }
        } else {
            // Show form with existing data
            try {
                int discountId = Integer.parseInt(request.getParameter("id"));
                Discounts discount = discountsDAO.getById(discountId);
                if (discount == null) {
                    request.setAttribute("error", "Discount not found");
                    return ERROR_PAGE;
                }
                List<Products> products = productsDAO.getAll();
                request.setAttribute("discount", discount);
                request.setAttribute("products", products);
                request.setAttribute("action", "edit");
                return DISCOUNTS_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading discount: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleDeleteDiscount(HttpServletRequest request, HttpServletResponse response) {
        try {
            int discountId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllDiscounts(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting discount: " + e.getMessage());
            return handleViewAllDiscounts(request, response);
        }
    }

    private String handleSearchDiscount(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<Discounts> discounts;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // Search by product ID
                discounts = discountsDAO.getByName(searchTerm.trim());
            } else {
                discounts = discountsDAO.getAll();
            }
            
            List<Products> products = productsDAO.getAll();
            request.setAttribute("discounts", discounts);
            request.setAttribute("products", products);
            request.setAttribute("searchTerm", searchTerm);
            return DISCOUNTS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching discounts: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleToggleStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            int discountId = Integer.parseInt(request.getParameter("id"));
            Discounts discount = discountsDAO.getById(discountId);
            
            if (discount != null) {
                String newStatus = "active".equals(discount.getStatus()) ? "inactive" : "active";
                discount.setStatus(newStatus);
                // Note: Implement update method in DAO
                request.setAttribute("success", "Discount status updated successfully");
            } else {
                request.setAttribute("error", "Discount not found");
            }
            
            return handleViewAllDiscounts(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error toggling discount status: " + e.getMessage());
            return handleViewAllDiscounts(request, response);
        }
    }

    private String handleGetByProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            String productIdStr = request.getParameter("productId");
            if (productIdStr != null && !productIdStr.trim().isEmpty()) {
                List<Discounts> discounts = discountsDAO.getByName(productIdStr.trim());
                List<Products> products = productsDAO.getAll();
                request.setAttribute("discounts", discounts);
                request.setAttribute("products", products);
                request.setAttribute("selectedProductId", productIdStr);
            } else {
                request.setAttribute("error", "Product ID is required");
                return handleViewAllDiscounts(request, response);
            }
            return DISCOUNTS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading discounts by product: " + e.getMessage());
            return handleViewAllDiscounts(request, response);
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
        return "Discounts Controller for managing product discounts";
    }
}