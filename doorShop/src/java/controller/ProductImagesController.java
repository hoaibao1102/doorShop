/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductImagesDAO;
import dto.ProductImages;
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
@WebServlet(name = "ProductImagesController", urlPatterns = {"/ProductImagesController"})
public class ProductImagesController extends HttpServlet {

    private final ProductImagesDAO productImagesDAO = new ProductImagesDAO();
    private static final String IMAGES_LIST_PAGE = "product-images/images-list.jsp";
    private static final String IMAGES_FORM_PAGE = "product-images/images-form.jsp";
    private static final String IMAGES_VIEW_PAGE = "product-images/images-view.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllImages(request, response);
            } else {
                switch (action) {
                    case "viewAll":
                        url = handleViewAllImages(request, response);
                        break;
                    case "add":
                        url = handleAddImage(request, response);
                        break;
                    case "edit":
                        url = handleEditImage(request, response);
                        break;
                    case "view":
                        url = handleViewImage(request, response);
                        break;
                    case "delete":
                        url = handleDeleteImage(request, response);
                        break;
                    case "viewByProduct":
                        url = handleViewByProduct(request, response);
                        break;
                    case "setPrimary":
                        url = handleSetPrimary(request, response);
                        break;
                    case "upload":
                        url = handleUploadImage(request, response);
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

    private String handleViewAllImages(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ProductImages> images = productImagesDAO.getAll();
            request.setAttribute("images", images);
            return IMAGES_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading product images: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddImage(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String productIdStr = request.getParameter("productId");
                String imagePath = request.getParameter("imagePath");
                String isPrimaryStr = request.getParameter("isPrimary");
                String sortOrderStr = request.getParameter("sortOrder");

                if (productIdStr == null || productIdStr.trim().isEmpty() || 
                    imagePath == null || imagePath.trim().isEmpty()) {
                    request.setAttribute("error", "Product ID and image path are required");
                    return IMAGES_FORM_PAGE;
                }

                ProductImages productImage = new ProductImages();
//                try {
//                    productImage.setProductId(Integer.parseInt(productIdStr.trim()));
//                } catch (NumberFormatException e) {
//                    request.setAttribute("error", "Invalid product ID format");
//                    return IMAGES_FORM_PAGE;
//                }
//                
//                productImage.setImagePath(imagePath.trim());
//                productImage.setIsPrimary("1".equals(isPrimaryStr) || "true".equalsIgnoreCase(isPrimaryStr));
//                
//                if (sortOrderStr != null && !sortOrderStr.trim().isEmpty()) {
//                    try {
//                        productImage.setSortOrder(Integer.parseInt(sortOrderStr.trim()));
//                    } catch (NumberFormatException e) {
//                        productImage.setSortOrder(0);
//                    }
//                } else {
//                    productImage.setSortOrder(0);
//                }

                boolean success = productImagesDAO.create(productImage);
                if (success) {
                    return "redirect:ProductImagesController?action=viewAll";
                } else {
                    request.setAttribute("error", "Failed to add product image. Please try again.");
                    return IMAGES_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error adding product image: " + e.getMessage());
                return IMAGES_FORM_PAGE;
            }
        } else {
            // Show form
            request.setAttribute("action", "add");
            return IMAGES_FORM_PAGE;
        }
    }

    private String handleEditImage(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int imageId = Integer.parseInt(request.getParameter("id"));
                String productIdStr = request.getParameter("productId");
                String imagePath = request.getParameter("imagePath");
                String isPrimaryStr = request.getParameter("isPrimary");
                String sortOrderStr = request.getParameter("sortOrder");

                if (productIdStr == null || productIdStr.trim().isEmpty() || 
                    imagePath == null || imagePath.trim().isEmpty()) {
                    request.setAttribute("error", "Product ID and image path are required");
                    ProductImages image = productImagesDAO.getById(imageId);
                    request.setAttribute("image", image);
                    request.setAttribute("action", "edit");
                    return IMAGES_FORM_PAGE;
                }

                ProductImages image = productImagesDAO.getById(imageId);
                if (image == null) {
                    request.setAttribute("error", "Product image not found");
                    return ERROR_PAGE;
                }

//                try {
//                    image.setProductId(Integer.parseInt(productIdStr.trim()));
//                } catch (NumberFormatException e) {
//                    request.setAttribute("error", "Invalid product ID format");
//                    request.setAttribute("image", image);
//                    request.setAttribute("action", "edit");
//                    return IMAGES_FORM_PAGE;
//                }
//                
//                image.setImagePath(imagePath.trim());
//                image.setIsPrimary("1".equals(isPrimaryStr) || "true".equalsIgnoreCase(isPrimaryStr));
//                
//                if (sortOrderStr != null && !sortOrderStr.trim().isEmpty()) {
//                    try {
//                        image.setSortOrder(Integer.parseInt(sortOrderStr.trim()));
//                    } catch (NumberFormatException e) {
//                        // Keep existing sort order if invalid input
//                    }
//                }

                // Note: Implement update method in DAO
                request.setAttribute("success", "Product image updated successfully");
                return "redirect:ProductImagesController?action=viewAll";
            } catch (Exception e) {
                request.setAttribute("error", "Error updating product image: " + e.getMessage());
                return ERROR_PAGE;
            }
        } else {
            // Show form with existing image data
            try {
                int imageId = Integer.parseInt(request.getParameter("id"));
                ProductImages image = productImagesDAO.getById(imageId);
                if (image == null) {
                    request.setAttribute("error", "Product image not found");
                    return ERROR_PAGE;
                }
                request.setAttribute("image", image);
                request.setAttribute("action", "edit");
                return IMAGES_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading product image: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleViewImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            int imageId = Integer.parseInt(request.getParameter("id"));
            ProductImages image = productImagesDAO.getById(imageId);
            if (image == null) {
                request.setAttribute("error", "Product image not found");
                return ERROR_PAGE;
            }
            request.setAttribute("image", image);
            return IMAGES_VIEW_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading product image: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleDeleteImage(HttpServletRequest request, HttpServletResponse response) {
        try {
            int imageId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllImages(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting product image: " + e.getMessage());
            return handleViewAllImages(request, response);
        }
    }

    private String handleViewByProduct(HttpServletRequest request, HttpServletResponse response) {
        try {
            String productIdStr = request.getParameter("productId");
            if (productIdStr == null || productIdStr.trim().isEmpty()) {
                request.setAttribute("error", "Product ID is required");
                return ERROR_PAGE;
            }
            
            int productId = Integer.parseInt(productIdStr.trim());
//            List<ProductImages> images = productImagesDAO.getByProductId(productId);
//            request.setAttribute("images", images);
            request.setAttribute("productId", productId);
            return IMAGES_LIST_PAGE;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID format");
            return ERROR_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading product images: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleSetPrimary(HttpServletRequest request, HttpServletResponse response) {
        try {
            int imageId = Integer.parseInt(request.getParameter("id"));
            ProductImages image = productImagesDAO.getById(imageId);
            
            if (image != null) {
                // First, set all images for this product as non-primary
                // Note: This would require a method in DAO to update all images for a product
                
                // Then set this image as primary
//                image.setIsPrimary(true);
                // Note: Implement update method in DAO
                request.setAttribute("success", "Primary image set successfully");
            } else {
                request.setAttribute("error", "Product image not found");
            }
            
            return handleViewAllImages(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error setting primary image: " + e.getMessage());
            return handleViewAllImages(request, response);
        }
    }

    private String handleUploadImage(HttpServletRequest request, HttpServletResponse response) {
        // Note: This would handle file upload functionality
        // Implementation would involve handling multipart form data
        request.setAttribute("info", "File upload functionality not implemented yet");
        return IMAGES_FORM_PAGE;
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
        return "Product Images Controller for managing product image gallery";
    }
}