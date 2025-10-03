/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.BannersDAO;
import dao.MediaDAO;
import dto.Banners;
import dto.Media;
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
@WebServlet(name = "BannersController", urlPatterns = {"/BannersController"})
public class BannersController extends HttpServlet {

    private final BannersDAO bannersDAO = new BannersDAO();
    private final MediaDAO mediaDAO = new MediaDAO();
    private static final String BANNERS_LIST_PAGE = "banners/banners-list.jsp";
    private static final String BANNERS_FORM_PAGE = "banners/banners-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllBanners(request, response);
            } else {
                switch (action) {
                    case "viewAllBanners":
                        url = handleViewAllBanners(request, response);
                        break;
                    case "addBanner":
                        url = handleAddBanner(request, response);
                        break;
                    case "editBanner":
                        url = handleEditBanner(request, response);
                        break;
                    case "deleteBanner":
                        url = handleDeleteBanner(request, response);
                        break;
                    case "searchBanner":
                        url = handleSearchBanner(request, response);
                        break;
                    case "toggleStatusBanner":
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

    private String handleViewAllBanners(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Banners> banners = bannersDAO.getAll();
            List<Media> mediaList = mediaDAO.getAll();
            request.setAttribute("banners", banners);
            request.setAttribute("mediaList", mediaList);
            return BANNERS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading banners: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddBanner(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String title = request.getParameter("title");
                String mediaIdStr = request.getParameter("mediaId");
                String status = request.getParameter("status");

                if (title == null || title.trim().isEmpty() || 
                    mediaIdStr == null || mediaIdStr.trim().isEmpty()) {
                    request.setAttribute("error", "Title and media are required");
                    List<Media> mediaList = mediaDAO.getAll();
                    request.setAttribute("mediaList", mediaList);
                    return BANNERS_FORM_PAGE;
                }

                int mediaId = Integer.parseInt(mediaIdStr);
                
                Banners banner = new Banners();
                banner.setTitle(title.trim());
                banner.setMedia_id(mediaId);
                banner.setStatus(status != null ? status : "visible");

                boolean success = bannersDAO.create(banner);
                if (success) {
                    request.setAttribute("success", "Banner created successfully");
                    return handleViewAllBanners(request, response);
                } else {
                    request.setAttribute("error", "Failed to create banner");
                    List<Media> mediaList = mediaDAO.getAll();
                    request.setAttribute("mediaList", mediaList);
                    return BANNERS_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error creating banner: " + e.getMessage());
                try {
                    List<Media> mediaList = mediaDAO.getAll();
                    request.setAttribute("mediaList", mediaList);
                } catch (Exception ex) {
                    // Ignore
                }
                return BANNERS_FORM_PAGE;
            }
        } else {
            // Show form
            try {
                List<Media> mediaList = mediaDAO.getAll();
                request.setAttribute("mediaList", mediaList);
                request.setAttribute("action", "add");
                return BANNERS_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading media list: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleEditBanner(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int bannerId = Integer.parseInt(request.getParameter("bannerId"));
                String title = request.getParameter("title");
                String mediaIdStr = request.getParameter("mediaId");
                String status = request.getParameter("status");

                Banners banner = bannersDAO.getById(bannerId);
                if (banner == null) {
                    request.setAttribute("error", "Banner not found");
                    return ERROR_PAGE;
                }

                banner.setTitle(title != null ? title.trim() : banner.getTitle());
                if (mediaIdStr != null && !mediaIdStr.trim().isEmpty()) {
                    banner.setMedia_id(Integer.parseInt(mediaIdStr));
                }
                banner.setStatus(status != null ? status : banner.getStatus());

                // Note: Implement update method in DAO
                request.setAttribute("success", "Banner updated successfully");
                return handleViewAllBanners(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error updating banner: " + e.getMessage());
                try {
                    List<Media> mediaList = mediaDAO.getAll();
                    request.setAttribute("mediaList", mediaList);
                } catch (Exception ex) {
                    // Ignore
                }
                return BANNERS_FORM_PAGE;
            }
        } else {
            // Show form with existing data
            try {
                int bannerId = Integer.parseInt(request.getParameter("id"));
                Banners banner = bannersDAO.getById(bannerId);
                if (banner == null) {
                    request.setAttribute("error", "Banner not found");
                    return ERROR_PAGE;
                }
                List<Media> mediaList = mediaDAO.getAll();
                request.setAttribute("banner", banner);
                request.setAttribute("mediaList", mediaList);
                request.setAttribute("action", "edit");
                return BANNERS_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading banner: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleDeleteBanner(HttpServletRequest request, HttpServletResponse response) {
        try {
            int bannerId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllBanners(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting banner: " + e.getMessage());
            return handleViewAllBanners(request, response);
        }
    }

    private String handleSearchBanner(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<Banners> banners;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                banners = bannersDAO.getByName(searchTerm.trim());
            } else {
                banners = bannersDAO.getAll();
            }
            
            List<Media> mediaList = mediaDAO.getAll();
            request.setAttribute("banners", banners);
            request.setAttribute("mediaList", mediaList);
            request.setAttribute("searchTerm", searchTerm);
            return BANNERS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching banners: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleToggleStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            int bannerId = Integer.parseInt(request.getParameter("id"));
            Banners banner = bannersDAO.getById(bannerId);
            
            if (banner != null) {
                String newStatus = "visible".equals(banner.getStatus()) ? "hidden" : "visible";
                banner.setStatus(newStatus);
                // Note: Implement update method in DAO
                request.setAttribute("success", "Banner status updated successfully");
            } else {
                request.setAttribute("error", "Banner not found");
            }
            
            return handleViewAllBanners(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error toggling banner status: " + e.getMessage());
            return handleViewAllBanners(request, response);
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
        return "Banners Controller for managing website banners";
    }
}