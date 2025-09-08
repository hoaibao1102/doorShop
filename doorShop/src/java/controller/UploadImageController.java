package controller;

import java.io.*;
import java.nio.file.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

@WebServlet("/UploadImageController")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 20 // 20MB
)
public class UploadImageController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        Part filePart = request.getPart("file"); // TinyMCE gửi field "file"
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        // Thư mục lưu file (trong webapp/uploads)
        String uploadDir = getServletContext().getRealPath("/uploads/images");
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        // Đường dẫn tuyệt đối file
        File file = new File(uploadDir, fileName);
        filePart.write(file.getAbsolutePath());

        // Trả JSON cho TinyMCE
        response.setContentType("application/json");
        String fileUrl = request.getContextPath() + "/uploads/images/" + fileName;
        response.getWriter().write("{\"location\": \"" + fileUrl + "\"}");
    }
}
