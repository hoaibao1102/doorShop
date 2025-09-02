<%-- 
    Document   : welcome
    Created on : Sep 2, 2025, 12:36:54 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.Admin" %>
<%@page import="utils.AuthUtils" %>
<%@page import="java.util.List" %>
<%@page import="dto.Products" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Welcome Page</title>
    </head>
    <body>
        <%
            Admin admin = AuthUtils.getCurrentUser(request);
            String keyword = (String) request.getAttribute("keyword");
            String checkError = (String) request.getAttribute("checkError");
            List<Products> list = (List<Products>) request.getAttribute("list");
        %>
        <div class="container">
            <div class="header-section">
                <% if (admin != null) { %>
                <h1>Welcome <%= admin.getFull_name() %>!</h1>
                <div class="header-actions">
                    <a href="MainController?action=logout" class="logout-btn">Logout</a>
                </div>
                <% } else { %>
                <div class="header-actions">
                    <a href="login.jsp" class="login-btn">Login</a>
                </div>
                <% } %>
            </div>

            <div class="content">
                <div class="search-section">
                    <form action="MainController" method="post" class="search-form">
                        <input type="hidden" name="action" value="searchProduct"/>
                        <label>Search product by name:</label>
                        <input type="text" name="keyword" 
                               value="<%= (keyword != null) ? keyword : "" %>" 
                               placeholder="Enter product name..."/>
                        <input type="submit" value="Search"/>
                    </form>
                </div>

                <% if (list != null && !list.isEmpty()) { %>
                <table class="products-table">
                    <thead>
                        <tr>
                            <th>Product ID</th>
                            <th>Product Name</th>
                            <th>Price</th>
                            <th>Description</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Products p : list) { %>
                        <%
                            double price = p.getPrice(); // giả sử cột price lấy từ DB
                            java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
                            String formatted = df.format(price) + " VND";
                        %>
                        <tr>
                            <td><%= p.getProduct_id() %></td>
                            <td><%= p.getName() %></td>
                            <td><%= formatted %></td>
                            <td><%= p.getSpec_html() %></td>
                            <td><%= p.getStatus() %></td>
                            <td>
                                <form action="MainController" method="post" style="display:inline;">
                                    <input type="hidden" name="action" value="editProduct"/>
                                    <input type="hidden" name="product_id" value="<%= p.getProduct_id() %>"/>
                                    <input type="hidden" name="keyword" value="<%= (keyword != null) ? keyword : "" %>" />
                                    <input type="submit" value="Edit" class="edit-btn" />
                                </form>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
                <% } else if (checkError != null && !checkError.isEmpty()) { %>
                <div class="error-message"><%= checkError %></div>
                <% } %>
            </div>
        </div>
    </body>
</html>
