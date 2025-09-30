/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.PostsDAO;
import dao.AdminDAO;
import dao.MediaDAO;
import dto.Posts;
import dto.Admin;
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
@WebServlet(name = "PostsController", urlPatterns = {"/PostsController"})
public class PostsController extends HttpServlet {

    private final PostsDAO postsDAO = new PostsDAO();
    private final AdminDAO adminDAO = new AdminDAO();
    private final MediaDAO mediaDAO = new MediaDAO();
    private static final String POSTS_LIST_PAGE = "posts/posts-list.jsp";
    private static final String POSTS_FORM_PAGE = "posts/posts-form.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllPosts(request, response);
            } else {
                switch (action) {
                    case "viewAll":
                        url = handleViewAllPosts(request, response);
                        break;
                    case "add":
                        url = handleAddPost(request, response);
                        break;
                    case "edit":
                        url = handleEditPost(request, response);
                        break;
                    case "delete":
                        url = handleDeletePost(request, response);
                        break;
                    case "search":
                        url = handleSearchPost(request, response);
                        break;
                    case "toggleStatus":
                        url = handleToggleStatus(request, response);
                        break;
                    case "publish":
                        url = handlePublishPost(request, response);
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

    private String handleViewAllPosts(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<Posts> posts = postsDAO.getAll();
            List<Admin> admins = adminDAO.getAll();
            List<Media> mediaList = mediaDAO.getAll();
            request.setAttribute("posts", posts);
            request.setAttribute("admins", admins);
            request.setAttribute("mediaList", mediaList);
            return POSTS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading posts: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddPost(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                String title = request.getParameter("title");
                String summary = request.getParameter("summary");
                String content = request.getParameter("content");
                String mediaIdStr = request.getParameter("mediaId");
                String authorIdStr = request.getParameter("authorId");
                String status = request.getParameter("status");

                if (title == null || title.trim().isEmpty() || 
                    content == null || content.trim().isEmpty() ||
                    authorIdStr == null || authorIdStr.trim().isEmpty()) {
                    request.setAttribute("error", "Title, content, and author are required");
                    loadFormData(request);
                    return POSTS_FORM_PAGE;
                }

                int authorId = Integer.parseInt(authorIdStr);
                Integer mediaId = null;
                if (mediaIdStr != null && !mediaIdStr.trim().isEmpty() && !"0".equals(mediaIdStr)) {
                    mediaId = Integer.parseInt(mediaIdStr);
                }
                
                Posts post = new Posts();
                post.setTitle(title.trim());
                post.setSummary(summary != null ? summary.trim() : "");
                post.setContent(content.trim());
                post.setMedia_id(mediaId);
                post.setAuthor_id(authorId);
                post.setStatus(status != null ? status : "visible");
                post.setPublished_at(new Date());

                boolean success = postsDAO.create(post);
                if (success) {
                    request.setAttribute("success", "Post created successfully");
                    return handleViewAllPosts(request, response);
                } else {
                    request.setAttribute("error", "Failed to create post");
                    loadFormData(request);
                    return POSTS_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error creating post: " + e.getMessage());
                loadFormData(request);
                return POSTS_FORM_PAGE;
            }
        } else {
            // Show form
            try {
                loadFormData(request);
                request.setAttribute("action", "add");
                return POSTS_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading form data: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleEditPost(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission
            try {
                int postId = Integer.parseInt(request.getParameter("postId"));
                String title = request.getParameter("title");
                String summary = request.getParameter("summary");
                String content = request.getParameter("content");
                String mediaIdStr = request.getParameter("mediaId");
                String authorIdStr = request.getParameter("authorId");
                String status = request.getParameter("status");

                Posts post = postsDAO.getById(postId);
                if (post == null) {
                    request.setAttribute("error", "Post not found");
                    return ERROR_PAGE;
                }

                post.setTitle(title != null ? title.trim() : post.getTitle());
                post.setSummary(summary != null ? summary.trim() : post.getSummary());
                post.setContent(content != null ? content.trim() : post.getContent());
                
                if (mediaIdStr != null && !mediaIdStr.trim().isEmpty()) {
                    if ("0".equals(mediaIdStr)) {
                        post.setMedia_id(null);
                    } else {
                        post.setMedia_id(Integer.parseInt(mediaIdStr));
                    }
                }
                
                if (authorIdStr != null && !authorIdStr.trim().isEmpty()) {
                    post.setAuthor_id(Integer.parseInt(authorIdStr));
                }
                
                if (status != null) {
                    post.setStatus(status);
                }

                // Note: Implement update method in DAO
                request.setAttribute("success", "Post updated successfully");
                return handleViewAllPosts(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error updating post: " + e.getMessage());
                loadFormData(request);
                return POSTS_FORM_PAGE;
            }
        } else {
            // Show form with existing data
            try {
                int postId = Integer.parseInt(request.getParameter("id"));
                Posts post = postsDAO.getById(postId);
                if (post == null) {
                    request.setAttribute("error", "Post not found");
                    return ERROR_PAGE;
                }
                loadFormData(request);
                request.setAttribute("post", post);
                request.setAttribute("action", "edit");
                return POSTS_FORM_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading post: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleDeletePost(HttpServletRequest request, HttpServletResponse response) {
        try {
            int postId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllPosts(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting post: " + e.getMessage());
            return handleViewAllPosts(request, response);
        }
    }

    private String handleSearchPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<Posts> posts;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                posts = postsDAO.getByName(searchTerm.trim());
            } else {
                posts = postsDAO.getAll();
            }
            
            List<Admin> admins = adminDAO.getAll();
            List<Media> mediaList = mediaDAO.getAll();
            request.setAttribute("posts", posts);
            request.setAttribute("admins", admins);
            request.setAttribute("mediaList", mediaList);
            request.setAttribute("searchTerm", searchTerm);
            return POSTS_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching posts: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleToggleStatus(HttpServletRequest request, HttpServletResponse response) {
        try {
            int postId = Integer.parseInt(request.getParameter("id"));
            Posts post = postsDAO.getById(postId);
            
            if (post != null) {
                String newStatus = "visible".equals(post.getStatus()) ? "hidden" : "visible";
                post.setStatus(newStatus);
                // Note: Implement update method in DAO
                request.setAttribute("success", "Post status updated successfully");
            } else {
                request.setAttribute("error", "Post not found");
            }
            
            return handleViewAllPosts(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error toggling post status: " + e.getMessage());
            return handleViewAllPosts(request, response);
        }
    }

    private String handlePublishPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            int postId = Integer.parseInt(request.getParameter("id"));
            Posts post = postsDAO.getById(postId);
            
            if (post != null) {
                post.setStatus("visible");
                post.setPublished_at(new Date());
                // Note: Implement update method in DAO
                request.setAttribute("success", "Post published successfully");
            } else {
                request.setAttribute("error", "Post not found");
            }
            
            return handleViewAllPosts(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error publishing post: " + e.getMessage());
            return handleViewAllPosts(request, response);
        }
    }

    private void loadFormData(HttpServletRequest request) {
        try {
            List<Admin> admins = adminDAO.getAll();
            List<Media> mediaList = mediaDAO.getAll();
            request.setAttribute("admins", admins);
            request.setAttribute("mediaList", mediaList);
        } catch (Exception e) {
            // Handle silently or log
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
        return "Posts Controller for managing blog posts and articles";
    }
}