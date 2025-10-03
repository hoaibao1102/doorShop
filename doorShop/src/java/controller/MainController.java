package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"", "/MainController", "/mc"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class MainController extends HttpServlet {

    private static final String WELCOME = "login.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = WELCOME;
        try {
            String action = request.getParameter("action");
            System.out.println(">>> doPost, action = " + action);
            if (isUserAction(action)) {
                url = "/UserController";
            } else if (isProductAction(action)) {
                url = "/ProductController";
            } else if (isAdminAction(action)){
                url = "/AdminController";
            } else if (isBannersAction(action)){
                url = "/BannersController";
            } else if (isCategoriesAction(action)){
                url = "/CategoriesController";
            } else if (isContactMessagesAction(action)){
                url = "/ContactMessagesController";
            } else if (isDiscountsAction(action)){
                url = "/DiscountsController";
            } else if (isMediaAction(action)){
                url = "/MediaController";
            } else if (isPostsAction(action)){
                url = "/PostsController";
            } else if (isProductImagesAction(action)){
                url = "/ProductImagesController";
            } else if (isUploadImageAction(action)){
                url = "/UploadImageController";
            } else if (isUploadVideoAction(action)){
                url = "/UploadVideoController";
            }
        } catch (Exception e) {
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
        //
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

    private boolean isUserAction(String action) {
        return "login".equals(action)
                || "logout".equals(action);
    }

    private boolean isProductAction(String action) {
        return "viewAllProduct".equals(action)
                || "searchProduct".equals(action)
                || "showAddProductForm".equals(action)
                || "addProduct".equals(action)
                || "editProduct".equals(action);
    }

    private boolean isAdminAction(String action) {
        return "viewAllAdmins".equals(action)
                || "addAdmin".equals(action)
                || "editAdmin".equals(action)
                || "deleteAdmin".equals(action)
                || "searchAdmin".equals(action)
                || "changePassword".equals(action);
    }

    private boolean isBannersAction(String action) {
        return "viewAllBanners".equals(action)
                || "addBanner".equals(action)
                || "editBanner".equals(action)
                || "deleteBanner".equals(action)
                || "searchBanner".equals(action)
                || "toggleStatusBanner".equals(action);
    }

    private boolean isCategoriesAction(String action) {
        return "viewAllCategories".equals(action)
                || "addCategory".equals(action)
                || "editCategory".equals(action)
                || "deleteCategory".equals(action)
                || "searchCategory".equals(action)
                || "toggleStatusCategory".equals(action);
    }

    private boolean isContactMessagesAction(String action) {
        return "viewAllMessages".equals(action)
                || "addMessage".equals(action)
                || "viewMessage".equals(action)
                || "replyMessage".equals(action)
                || "deleteMessage".equals(action)
                || "searchMessage".equals(action)
                || "markAsRead".equals(action)
                || "markAsReplied".equals(action);
    }

    private boolean isDiscountsAction(String action) {
        return "viewAllDiscounts".equals(action)
                || "addDiscount".equals(action)
                || "editDiscount".equals(action)
                || "deleteDiscount".equals(action)
                || "searchDiscount".equals(action)
                || "toggleStatusDiscount".equals(action)
                || "getByProduct".equals(action);
    }

    private boolean isMediaAction(String action) {
        return "viewAllMedia".equals(action)
                || "addMedia".equals(action)
                || "editMedia".equals(action)
                || "viewMedia".equals(action)
                || "deleteMedia".equals(action)
                || "searchMedia".equals(action)
                || "filterByType".equals(action)
                || "uploadMedia".equals(action);
    }

    private boolean isPostsAction(String action) {
        return "viewAllPosts".equals(action)
                || "addPost".equals(action)
                || "editPost".equals(action)
                || "deletePost".equals(action)
                || "searchPost".equals(action)
                || "toggleStatusPost".equals(action)
                || "publishPost".equals(action);
    }

    private boolean isProductImagesAction(String action) {
        return "viewAllImages".equals(action)
                || "addImage".equals(action)
                || "editImage".equals(action)
                || "viewImage".equals(action)
                || "deleteImage".equals(action)
                || "viewByProduct".equals(action)
                || "setPrimary".equals(action)
                || "uploadImage".equals(action);
    }

    private boolean isUploadImageAction(String action) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private boolean isUploadVideoAction(String action) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
