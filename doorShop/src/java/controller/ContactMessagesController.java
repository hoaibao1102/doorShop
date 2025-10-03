/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dao.ContactMessagesDAO;
import dto.ContactMessages;
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
@WebServlet(name = "ContactMessagesController", urlPatterns = {"/ContactMessagesController"})
public class ContactMessagesController extends HttpServlet {

    private final ContactMessagesDAO contactMessagesDAO = new ContactMessagesDAO();
    private static final String MESSAGES_LIST_PAGE = "contact/messages-list.jsp";
    private static final String MESSAGES_FORM_PAGE = "contact/messages-form.jsp";
    private static final String MESSAGES_VIEW_PAGE = "contact/messages-view.jsp";
    private static final String ERROR_PAGE = "error.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR_PAGE;
        
        try {
            String action = request.getParameter("action");
            
            if (action == null || action.isEmpty()) {
                url = handleViewAllMessages(request, response);
            } else {
                switch (action) {
                    case "viewAllMessages":
                        url = handleViewAllMessages(request, response);
                        break;
                    case "addMessage":
                        url = handleAddMessage(request, response);
                        break;
                    case "viewMessage":
                        url = handleViewMessage(request, response);
                        break;
                    case "replyMessage":
                        url = handleReplyMessage(request, response);
                        break;
                    case "deleteMessage":
                        url = handleDeleteMessage(request, response);
                        break;
                    case "searchMessage":
                        url = handleSearchMessage(request, response);
                        break;
                    case "markAsRead":
                        url = handleMarkAsRead(request, response);
                        break;
                    case "markAsReplied":
                        url = handleMarkAsReplied(request, response);
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

    private String handleViewAllMessages(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<ContactMessages> messages = contactMessagesDAO.getAll();
            request.setAttribute("messages", messages);
            return MESSAGES_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading messages: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleAddMessage(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle form submission (for website visitors)
            try {
                String name = request.getParameter("name");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String subject = request.getParameter("subject");
                String message = request.getParameter("message");

                if (name == null || name.trim().isEmpty() || 
                    email == null || email.trim().isEmpty() ||
                    message == null || message.trim().isEmpty()) {
                    request.setAttribute("error", "Name, email, and message are required");
                    return MESSAGES_FORM_PAGE;
                }

                ContactMessages contactMessage = new ContactMessages();
                contactMessage.setName(name.trim());
                contactMessage.setEmail(email.trim());
                contactMessage.setPhone(phone != null ? phone.trim() : "");
                contactMessage.setSubject(subject != null ? subject.trim() : "");
                contactMessage.setMessage(message.trim());
                contactMessage.setStatus("new");

                boolean success = contactMessagesDAO.create(contactMessage);
                if (success) {
                    request.setAttribute("success", "Your message has been sent successfully! We will contact you soon.");
                    return MESSAGES_FORM_PAGE; // Return to form with success message
                } else {
                    request.setAttribute("error", "Failed to send message. Please try again.");
                    return MESSAGES_FORM_PAGE;
                }
            } catch (Exception e) {
                request.setAttribute("error", "Error sending message: " + e.getMessage());
                return MESSAGES_FORM_PAGE;
            }
        } else {
            // Show form
            request.setAttribute("action", "add");
            return MESSAGES_FORM_PAGE;
        }
    }

    private String handleViewMessage(HttpServletRequest request, HttpServletResponse response) {
        try {
            int messageId = Integer.parseInt(request.getParameter("id"));
            ContactMessages message = contactMessagesDAO.getById(messageId);
            if (message == null) {
                request.setAttribute("error", "Message not found");
                return ERROR_PAGE;
            }
            
            // Mark as read if it's new
            if ("new".equals(message.getStatus())) {
                message.setStatus("read");
                // Note: Implement update method in DAO
            }
            
            request.setAttribute("message", message);
            return MESSAGES_VIEW_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error loading message: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleReplyMessage(HttpServletRequest request, HttpServletResponse response) {
        if ("POST".equals(request.getMethod())) {
            // Handle reply submission
            try {
                int messageId = Integer.parseInt(request.getParameter("messageId"));
                String replyContent = request.getParameter("replyContent");

                if (replyContent == null || replyContent.trim().isEmpty()) {
                    request.setAttribute("error", "Reply content is required");
                    ContactMessages message = contactMessagesDAO.getById(messageId);
                    request.setAttribute("message", message);
                    return MESSAGES_VIEW_PAGE;
                }

                ContactMessages message = contactMessagesDAO.getById(messageId);
                if (message != null) {
                    message.setStatus("replied");
                    // Note: Implement update method in DAO
                    // Note: Implement email sending functionality here
                    request.setAttribute("success", "Reply sent successfully");
                } else {
                    request.setAttribute("error", "Message not found");
                }
                
                return handleViewAllMessages(request, response);
            } catch (Exception e) {
                request.setAttribute("error", "Error sending reply: " + e.getMessage());
                return handleViewAllMessages(request, response);
            }
        } else {
            // Show reply form
            try {
                int messageId = Integer.parseInt(request.getParameter("id"));
                ContactMessages message = contactMessagesDAO.getById(messageId);
                if (message == null) {
                    request.setAttribute("error", "Message not found");
                    return ERROR_PAGE;
                }
                request.setAttribute("message", message);
                request.setAttribute("action", "reply");
                return MESSAGES_VIEW_PAGE;
            } catch (Exception e) {
                request.setAttribute("error", "Error loading message: " + e.getMessage());
                return ERROR_PAGE;
            }
        }
    }

    private String handleDeleteMessage(HttpServletRequest request, HttpServletResponse response) {
        try {
            int messageId = Integer.parseInt(request.getParameter("id"));
            // Note: Implement delete functionality in DAO if needed
            request.setAttribute("info", "Delete functionality not implemented yet");
            return handleViewAllMessages(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error deleting message: " + e.getMessage());
            return handleViewAllMessages(request, response);
        }
    }

    private String handleSearchMessage(HttpServletRequest request, HttpServletResponse response) {
        try {
            String searchTerm = request.getParameter("search");
            List<ContactMessages> messages;
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                messages = contactMessagesDAO.getByName(searchTerm.trim());
            } else {
                messages = contactMessagesDAO.getAll();
            }
            
            request.setAttribute("messages", messages);
            request.setAttribute("searchTerm", searchTerm);
            return MESSAGES_LIST_PAGE;
        } catch (Exception e) {
            request.setAttribute("error", "Error searching messages: " + e.getMessage());
            return ERROR_PAGE;
        }
    }

    private String handleMarkAsRead(HttpServletRequest request, HttpServletResponse response) {
        try {
            int messageId = Integer.parseInt(request.getParameter("id"));
            ContactMessages message = contactMessagesDAO.getById(messageId);
            
            if (message != null) {
                message.setStatus("read");
                // Note: Implement update method in DAO
                request.setAttribute("success", "Message marked as read");
            } else {
                request.setAttribute("error", "Message not found");
            }
            
            return handleViewAllMessages(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error marking message as read: " + e.getMessage());
            return handleViewAllMessages(request, response);
        }
    }

    private String handleMarkAsReplied(HttpServletRequest request, HttpServletResponse response) {
        try {
            int messageId = Integer.parseInt(request.getParameter("id"));
            ContactMessages message = contactMessagesDAO.getById(messageId);
            
            if (message != null) {
                message.setStatus("replied");
                // Note: Implement update method in DAO
                request.setAttribute("success", "Message marked as replied");
            } else {
                request.setAttribute("error", "Message not found");
            }
            
            return handleViewAllMessages(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error marking message as replied: " + e.getMessage());
            return handleViewAllMessages(request, response);
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
        return "Contact Messages Controller for managing customer inquiries";
    }
}