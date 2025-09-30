/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/UploadVideoController")
@MultipartConfig
public class UploadVideoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Thư mục upload (chỉnh theo project của bạn)
        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads/videos";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Lấy file từ request
        Part filePart = request.getPart("file");
        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();

        // Chỉ cho phép .mp4
        if (!fileName.toLowerCase().endsWith(".mp4")) {
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Only .mp4 allowed\"}");
            return;
        }

        // Lưu file
        filePart.write(uploadPath + File.separator + fileName);

        // URL truy cập file (ví dụ: http://localhost:8080/YourApp/uploads/videos/xxxx.mp4)
        String fileUrl = request.getContextPath() + "/uploads/videos/" + fileName;

        // TinyMCE cần JSON trả về { "location": "url" }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write("{\"location\": \"" + fileUrl + "\"}");
        out.flush();
    }
}
