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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "welcome.jsp";
        try {
            String action = request.getParameter("action");

            if (action == null || action.isEmpty()) {
                url = handleViewAllProducts(request, response);
            } else if (action.equals("searchProduct")) {
                url = handleProductSearching(request, response);
            } else if (action.equals("showAddProductForm")) {
                url = handleShowAddProductForm(request, response);
            } else if (action.equals("addProduct")) {
                url = handleProductAdding(request, response);
            } else if (action.equals("editProduct")) {
                url = handleProductEditing(request, response);
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

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = productsdao.getByName(keyword.trim());
            if (list == null || list.isEmpty()) {
                checkError = "No products found with name: " + keyword;
            } else {
                // Lấy danh sách ảnh cho từng sản phẩm
                for (Products p : list) {
                    List<ProductImages> imgs = productImagesDAO.getListByProductId(p.getProduct_id());
                    p.setImages(imgs); // Products giờ có field List<ProductImages> images + setter
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
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            String specHtml = request.getParameter("spec_html");
            String status = request.getParameter("status"); // active, inactive, Prominent

            // ===== Tạo sản phẩm trước =====
            Products newProduct = new Products();
            newProduct.setBrand_id(brandId);
            newProduct.setCategory_id(categoryId);
            newProduct.setName(name);
            newProduct.setPrice(price);
            newProduct.setSpec_html(specHtml);
            newProduct.setStatus(status);

            ProductsDAO productsdao = new ProductsDAO();
            int generatedId = productsdao.createNewProduct(newProduct); // lấy id tự tăng vừa tạo

            if (generatedId > 0) {
                newProduct.setProduct_id(generatedId); // fix id
                // ===== Upload ảnh =====
                List<Part> imageParts = new ArrayList<>();
                Part img1 = request.getPart("imageFile1");
                Part img2 = request.getPart("imageFile2");
                Part img3 = request.getPart("imageFile3");
                Part img4 = request.getPart("imageFile4");

                if (img1 != null && img1.getSize() > 0) {
                    imageParts.add(img1);
                }
                if (img2 != null && img2.getSize() > 0) {
                    imageParts.add(img2);
                }
                if (img3 != null && img3.getSize() > 0) {
                    imageParts.add(img3);
                }
                if (img4 != null && img4.getSize() > 0) {
                    imageParts.add(img4);
                }

                String uploadDir = getServletContext().getRealPath("/assets/img/products/");
                new File(uploadDir).mkdirs();

                List<ProductImages> imageList = new ArrayList<>();
                int index = 1;
                for (Part imagePart : imageParts) {
                    String originalFileName = Paths.get(imagePart.getSubmittedFileName()).getFileName().toString();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                    String storedFileName = generatedId + "_" + (index++) + fileExtension;
                    String imagePath = uploadDir + File.separator + storedFileName;

                    // Lưu file vào thư mục
                    imagePart.write(imagePath);

                    // Tạo record ảnh
                    ProductImages img = new ProductImages();
                    img.setProduct_id(generatedId);
                    img.setImage_url("assets/img/products/" + storedFileName);
                    img.setCaption("");
                    img.setStatus(1); // default
                    imageList.add(img);
                }

                // ===== Lưu ảnh vào DB =====
                ProductImagesDAO imageDao = new ProductImagesDAO();
                for (ProductImages img : imageList) {
                    imageDao.create(img);
                }

                // ===== Cập nhật main_image_id bằng ảnh đầu tiên =====
                if (!imageList.isEmpty()) {
                    ProductImages firstImg = imageList.get(0);
                    productsdao.updateMainImage(generatedId, firstImg.getImage_id());
                    newProduct.setMain_image_id(firstImg.getImage_id());
                }

                // ===== Success =====
                HttpSession session = request.getSession();
                session.removeAttribute("cachedProductListEdit");
                request.setAttribute("messageAddProduct", "New product and images added successfully.");
                request.setAttribute("product", newProduct);
                request.setAttribute("productImages", imageList); // fix
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
