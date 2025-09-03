<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="dto.Admin" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Login Page</title>
        <style>
            /* ====== Modern Login – Responsive, No HTML changes ====== */
            :root{
                --bg:#FFFFFF;
                --accent:#78ADDB;
                --primary:#232E76;
                --text:#0f172a;
                --muted:#6b7280;
                --border:#e5e7eb;
            }

            /* Reset nhỏ & fix overflow */
            *,
            *::before,
            *::after{
                box-sizing:border-box;
            }
            html, body{
                height:100%;
            }
            body{
                margin:0;
                color:var(--text);
                font-family:'Segoe UI', Arial, sans-serif;
                /* nền có chiều sâu nhẹ nhưng KHÔNG gây scroll ngang */
                background:
                    radial-gradient(900px 450px at 10% 10%, rgba(120,173,219,.12), transparent 60%),
                    radial-gradient(800px 400px at 90% 90%, rgba(35,46,118,.10), transparent 55%),
                    #f3f6fb;
                display:flex;
                align-items:center;
                justify-content:center;
                overflow-x:hidden; /* ngăn tràn ngang */
                padding: clamp(12px, 4vw, 28px); /* chừa viền an toàn trên mobile */
                padding-left: max(clamp(12px, 4vw, 28px), env(safe-area-inset-left));
                padding-right:max(clamp(12px, 4vw, 28px), env(safe-area-inset-right));
            }

            /* Card */
            .login-container{
                width:100%;
                max-width: 420px;             /* chống tràn trên mobile */
                background: rgba(255,255,255,.96);
                backdrop-filter: saturate(160%) blur(6px);
                border:1px solid rgba(15,23,42,.06);
                border-radius: 16px;
                padding: 22px 18px;           /* base: mobile */
                text-align:center;
                box-shadow: 0 18px 54px rgba(2,8,23,.12);
                margin-inline:auto;
                animation: cardIn .45s ease-out both;
            }
            @keyframes cardIn{
                from{
                    opacity:0;
                    transform:translateY(10px) scale(.98)
                }
                to{
                    opacity:1;
                    transform:translateY(0) scale(1)
                }
            }

            .login-container h2{
                margin: 0 0 16px;
                color: var(--primary);
                font-size: clamp(22px, 3.5vw, 28px); /* scale theo màn hình */
                font-weight: 700;
                letter-spacing:.3px;
                position:relative;
            }
            .login-container h2::after{
                content:"";
                display:block;
                width:62px;
                height:3px;
                margin:10px auto 0;
                border-radius:999px;
                background: linear-gradient(90deg, var(--accent), var(--primary));
                opacity:.65;
            }

            /* Inputs */
            .login-container input[type="text"],
            .login-container input[type="password"]{
                width:100%;
                padding: 12px 14px;
                margin: 10px 0;
                border:1px solid var(--border);
                border-radius: 12px;
                background:#eef4ff;
                transition:border-color .2s, box-shadow .25s, background .25s, transform .05s;
                font-size: 15px;
            }
            .login-container input[type="text"]:focus,
            .login-container input[type="password"]:focus{
                outline:none;
                border-color: var(--accent);
                background:#fff;
                box-shadow: 0 0 0 4px rgba(120,173,219,.25);
            }

            /* Button */
            .login-container input[type="submit"]{
                width:100%;
                margin-top: 8px;
                padding: 12px 14px;
                border:0;
                border-radius: 12px;
                color:#fff;
                font-size: clamp(15px, 2.8vw, 16px);
                font-weight:600;
                background: linear-gradient(180deg, #2c3690, var(--primary));
                box-shadow: 0 10px 20px rgba(35,46,118,.25);
                cursor:pointer;
                position:relative;
                overflow:hidden;
                transition: transform .05s ease, box-shadow .25s ease, filter .25s ease;
            }
            .login-container input[type="submit"]:hover{
                box-shadow: 0 14px 28px rgba(35,46,118,.32);
                filter:saturate(1.05);
            }
            .login-container input[type="submit"]:active{
                transform: translateY(1px);
            }

            /* Error */
            .error-message{
                margin-top: 12px;
                font-size: 14px;
                color:#d61f1f;
                display:inline-block;
                background:#fff5f5;
                border:1px solid #f7caca;
                padding:8px 12px;
                border-radius:10px;
            }

            /* ========= Breakpoints ========= */
            /* >= 576px: điện thoại to / tablet dọc */
            @media (min-width:576px){
                .login-container{
                    padding: 26px 22px;
                }
            }

            /* >= 768px: tablet ngang */
            @media (min-width:768px){
                body{
                    padding: clamp(16px, 5vw, 40px);
                }
                .login-container{
                    padding: 28px 26px;
                    border-radius:18px;
                }
                .login-container input[type="text"],
                .login-container input[type="password"]{
                    padding: 14px 16px;
                }
                .login-container input[type="submit"]{
                    padding: 14px 16px;
                }
            }

            /* >= 992px: desktop */
            @media (min-width:992px){
                .login-container{
                    max-width: 520px;          /* đẹp hơn trên màn to, vẫn không tràn */
                    padding: 32px 28px;
                }
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
