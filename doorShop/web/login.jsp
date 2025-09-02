<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.Admin" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login Page</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #f2f2f2;
                margin: 0;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }
            .login-container {
                background-color: #fff;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 10px rgba(0,0,0,0.1);
                width: 320px;
                text-align: center;
            }
            .login-container h2 {
                margin-bottom: 20px;
                color: #333;
            }
            .login-container input[type="text"],
            .login-container input[type="password"] {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
                border: 1px solid #ccc;
                border-radius: 6px;
                box-sizing: border-box;
            }
            .login-container input[type="submit"] {
                width: 100%;
                padding: 10px;
                background-color: #4CAF50;
                border: none;
                color: #fff;
                border-radius: 6px;
                cursor: pointer;
                font-size: 16px;
            }
            .login-container input[type="submit"]:hover {
                background-color: #45a049;
            }
            .error-message {
                color: red;
                margin-top: 15px;
                font-size: 14px;
            }
        </style>
    </head>
    <body>
        <%
            // Kiểm tra nếu đã login thì redirect sang welcome.jsp
            if (session.getAttribute("user") != null) {
                response.sendRedirect("welcome.jsp");
                return;
            }

            // Lấy message từ request (nếu có)
            String msg = (String) request.getAttribute("message");
            if (msg == null) msg = "";
        %>
        <div class="login-container">
            <h2>Login</h2>
            <form action="MainController" method="post">
                <input type="hidden" name="action" value="login"/>
                <input type="text" name="strUserName" placeholder="Username" required />
                <input type="password" name="strPassword" placeholder="Password" required />
                <input type="submit" value="Login"/>
            </form>

            <% if (!msg.isEmpty()) { %>
            <div class="error-message"><%= msg %></div>
            <% } %>
        </div>
    </body>
</html>
