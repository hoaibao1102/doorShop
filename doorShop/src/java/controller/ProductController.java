/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ProductImagesDAO;
import dao.ProductsDAO;
import dto.ProductImages;
import dto.Products;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "ProductController", urlPatterns = {"/ProductController"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50 // 50MB
)
public class ProductController extends HttpServlet {

    private final ProductsDAO productsdao = new ProductsDAO();
    private final ProductImagesDAO productImagesDAO = new ProductImagesDAO();

    private static final String ERROR_PAGE = "error.jsp"; 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        try {
            String action = request.getParameter("action");

            if (action == null || action.isEmpty()) {
                url = handleViewAllProducts(request, response);
            } else {
                switch (action) {
                    case "viewAllProduct":
                        url = handleViewAllProducts(request, response);
                        break;
                    case "searchProduct":
                        url = handleProductSearching(request, response);
                        break;
                    case "showAddProductForm":
                        url = handleShowAddProductForm(request, response);
                        break;
                    case "addProduct":
                        url = handleProductAdding(request, response);
                        break;
                    case "editProduct":
                        url = handleProductEditing(request, response);
                        break;
                    default:
                        request.setAttribute("error", "Invalid action: " + action);
                        url = ERROR_PAGE;
                }
            }
        } catch (Exception e) {
            request.setAttribute("checkError", "Unexpected error: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
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

    private String handleViewAllProducts(HttpServletRequest request, HttpServletResponse response) {
        List<Products> list = productsdao.getAll();
        request.setAttribute("list", list);
        return "welcome.jsp";
    }

    private String handleProductSearching(HttpServletRequest request, HttpServletResponse response) {
        String checkError = "";
        String keyword = request.getParameter("keyword");
        List<Products> list;

        System.out.println(">>> Keyword nhận từ request: " + keyword);  // debug

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = productsdao.getByName(keyword.trim());
            if (list == null || list.isEmpty()) {
                checkError = "No products found with name: " + keyword;
            } else {
                // Lấy danh sách ảnh cho từng sản phẩm
                for (Products p : list) {
                    List<ProductImages> imgs = productImagesDAO.getListByProductId(p.getProduct_id());
                    p.setImages(imgs); // Products có field List<ProductImages> images + setter
                }
            }
        } else {
            list = productsdao.getAll();
            // cũng lấy danh sách ảnh cho tất cả sản phẩm
            for (Products p : list) {
                List<ProductImages> imgs = productImagesDAO.getListByProductId(p.getProduct_id());
                p.setImages(imgs);
            }
        }

        request.setAttribute("keyword", keyword);
        request.setAttribute("list", list);
        request.setAttribute("checkError", checkError);
        return "welcome.jsp";
    }

    private String handleShowAddProductForm(HttpServletRequest request, HttpServletResponse response) {
        // Chỉ forward ra form rỗng, không có dữ liệu sản phẩm
        request.setAttribute("product", null);
        return "productsUpdate.jsp";
    }

    private String handleProductAdding(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // ===== Lấy dữ liệu từ form =====
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String name = request.getParameter("name");
            String sku = request.getParameter("sku");
            double price = Double.parseDouble(request.getParameter("price"));
            String shortDesc = request.getParameter("short_desc");
            String specHtml = request.getParameter("spec_html");
            String mainImage = request.getParameter("main_image");
            String status = request.getParameter("status"); // visible, hidden

            // ===== Tạo sản phẩm trước =====
            Products newProduct = new Products();
            newProduct.setCategory_id(categoryId);
            newProduct.setName(name);
            newProduct.setSku(sku);
            newProduct.setPrice(price);
            newProduct.setShort_desc(shortDesc);
            newProduct.setSpec_html(specHtml);
            newProduct.setMain_image(mainImage);
            newProduct.setStatus(status);

            boolean success = productsdao.create(newProduct); // sử dụng method create chuẩn

            if (success) {
                // ===== Success =====
                HttpSession session = request.getSession();
                session.removeAttribute("cachedProductListEdit");
                request.setAttribute("messageAddProduct", "New product added successfully.");
                request.setAttribute("product", newProduct);
                return "productsUpdate.jsp";
            } else {
                request.setAttribute("checkErrorAddProduct", "Failed to add product.");
                return "productsUpdate.jsp";
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("checkError", "Error while adding product: " + e.getMessage());
            return "error.jsp";
        }
    }

    private String handleProductEditing(HttpServletRequest request, HttpServletResponse response) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
