/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.MediaDAO;
import dto.Media;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 *
 * @author MSI PC
 */
@WebServlet(name = "MediaController", urlPatterns = {"/MediaController"})
public class MediaController extends HttpServlet {

    private final MediaDAO mediaDAO = new MediaDAO();
    private static final String MEDIA_LIST_PAGE = "media/media-list.jsp";
    private static final String MEDIA_FORM_PAGE = "media/media-form.jsp";
    private static final String MEDIA_VIEW_PAGE = "media/media-view.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllMedia(request, response);
            } else {
                switch (action) {
                    case "viewAll":
                        url = handleViewAllMedia(request, response);
                        break;
                    case "add":
                        url = handleAddMedia(request, response);
                        break;
                    case "edit":
                        url = handleEditMedia(request, response);
                        break;
                    case "view":
                        url = handleViewMedia(request, response);
                        break;
                    case "delete":
                        url = handleDeleteMedia(request, response);
                        break;
                    case "search":
                        url = handleSearchMedia(request, response);
                        break;
                    case "filterByType":
                        url = handleFilterByType(request, response);
                        break;
                    case "upload":
                        url = handleUploadMedia(request, response);
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

    private String handleViewAllMedia(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Media> mediaList = mediaDAO.getAll();
            request.setAttribute("mediaList", mediaList);
            return MEDIA_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading media: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddMedia(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String fileName = request.getParameter("fileName");
                String originalName = request.getParameter("originalName");
                String filePath = request.getParameter("filePath");
                String mediaType = request.getParameter("mediaType");
                String fileSizeStr = request.getParameter("fileSize");
                String description = request.getParameter("description");

                if (fileName == null || fileName.trim().isEmpty() || 
                    filePath == null || filePath.trim().isEmpty() ||
                    mediaType == null || mediaType.trim().isEmpty()) {
                    request.setAttribute("error", "File name, file path, and media type are required");
                    return MEDIA_FORM_PAGE;
                }

//                Media media = new Media();
//                media.setFileName(fileName.trim());
//                media.setOriginalName(originalName != null ? originalName.trim() : fileName.trim());
//                media.setFilePath(filePath.trim());
//                media.setMediaType(mediaType.trim());
//                media.setDescription(description != null ? description.trim() : "");
//                
//                if (fileSizeStr != null && !fileSizeStr.trim().isEmpty()) {
//                    try {
//                        media.setFileSize(Long.parseLong(fileSizeStr.trim()));
//                    } catch (NumberFormatException e) {
//                        media.setFileSize(0L);
//                    }
//                } else {
//                    media.setFileSize(0L);
//                }

//                boolean success = mediaDAO.create(media);
//                if (success) {
//                    return "redirect:MediaController?action=viewAll";
//                } else {
//                    request.setAttribute("error", "Failed to add media. Please try again.");
//                    return MEDIA_FORM_PAGE;
//                }
            } catch (Exception e) {
                request.setAttribute("error", "Error adding media: " + e.getMessage());
                return MEDIA_FORM_PAGE;
            }
        } else {
            // Show form
            request.setAttribute("action", "add");
            return MEDIA_FORM_PAGE;
        }
        return null;
    }

    private String handleEditMedia(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int mediaId = Integer.parseInt(request.getParameter("id"));
                String fileName = request.getParameter("fileName");
                String originalName = request.getParameter("originalName");
                String filePath = request.getParameter("filePath");
                String mediaType = request.getParameter("mediaType");
                String fileSizeStr = request.getParameter("fileSize");
                String description = request.getParameter("description");

                if (fileName == null || fileName.trim().isEmpty() || 
                    filePath == null || filePath.trim().isEmpty() ||
                    mediaType == null || mediaType.trim().isEmpty()) {
                    request.setAttribute("error", "File name, file path, and media type are required");
                    Media media = mediaDAO.getById(mediaId);
                    request.setAttribute("media", media);
                    request.setAttribute("action", "edit");
                    return MEDIA_FORM_PAGE;
                }

                Media media = mediaDAO.getById(mediaId);
                if (media == null) {
                    request.setAttribute("error", "Media not found");
                    return ERROR_PAGE;
                }

//                media.setFileName(fileName.trim());
//                media.setOriginalName(originalName != null ? originalName.trim() : fileName.trim());
//                media.setFilePath(filePath.trim());
//                media.setMediaType(mediaType.trim());
//                media.setDescription(description != null ? description.trim() : "");
//                
//                if (fileSizeStr != null && !fileSizeStr.trim().isEmpty()) {
//                    try {
//                        media.setFileSize(Long.parseLong(fileSizeStr.trim()));
//                    } catch (NumberFormatException e) {
//                        // Keep existing file size if invalid input
//                    }
//                } else {
//                    media.setFileSize(0L);
//                }

                // Note: Implement update method in DAO
                request.setAttribute("success", "Media updated successfully");
                return "redirect:MediaController?action=viewAll";
            } catch (Exception e) {
                request.setAttribute("error", "Error updating media: " + e.getMessage());
                return ERROR_PAGE;
            }
        } else {
            // Show form with existing media data
            try {
                int mediaId = Integer.parseInt(request.getParameter("id"));
                Media media = mediaDAO.getById(mediaId);
                if (media == null) {
                    request.setAttribute("error", "Media not found");
                    return ERROR_PAGE;
                }
                request.setAttribute("media", media);
                request.setAttribute("action", "edit");
                return MEDIA_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading media: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleViewMedia(HttpServletRequest request, HttpServletResponse response) {
        try {
            int mediaId = Integer.parseInt(request.getParameter("id"));
            Media media = mediaDAO.getById(mediaId);
            if (media == null) {
                request.setAttribute("error", "Media not found");
                return ERROR_PAGE;
            }
            request.setAttribute("media", media);
            return MEDIA_VIEW_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading media: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleDeleteMedia(HttpServletRequest request, HttpServletResponse response) {
        try {
            int mediaId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllMedia(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting media: " + e.getMessage());
            return handleViewAllMedia(request, response);
        }
    }

    private String handleSearchMedia(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<Media> mediaList;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
//                mediaList = mediaDAO.getByFileName(searchTerm.trim());
            } else {
                mediaList = mediaDAO.getAll();
            }
//            
//            request.setAttribute("mediaList", mediaList);
            request.setAttribute("searchTerm", searchTerm);
            return MEDIA_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching media: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleFilterByType(HttpServletRequest request, HttpServletResponse response) {
        try {
            String mediaType = request.getParameter("type");
            List<Media> mediaList;
            
            if (mediaType != null && !mediaType.trim().isEmpty()) {
//                mediaList = mediaDAO.getByType(mediaType.trim());
            } else {
                mediaList = mediaDAO.getAll();
            }
            
//            request.setAttribute("mediaList", mediaList);
            request.setAttribute("selectedType", mediaType);
            return MEDIA_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error filtering media: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleUploadMedia(HttpServletRequest request, HttpServletResponse response) {
        // Note: This would handle file upload functionality
        // Implementation would involve handling multipart form data
        request.setAttribute("info", "File upload functionality not implemented yet");
        return MEDIA_FORM_PAGE;
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
        return "Media Controller for managing uploaded files and media";
    }
}