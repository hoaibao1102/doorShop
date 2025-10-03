
<%-- 
    Document   : welcome
    Created on : Sep 2, 2025, 12:36:54 PM
    Author     : ADMIN
--%>

<%@page import="dto.Products"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Trang chủ - Hòa Phát Door</title>

        <!-- Bootstrap 5 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

        <style>
            /* Style phụ nhỏ để đồng bộ với palette */
            :root{
                --primary:#232E76;
                --accent:#78ADDB;
                --sale:#e11d48;
            }
            .section-title{
                display:inline-block;
                padding: .5rem 1.5rem;
                border:2px solid var(--accent);
                border-radius:50rem;
                background:#fff;
                font-weight:700;
                box-shadow:0 4px 12px rgba(120,173,219,.2);
            }
            .sb-thumb{
                width:80px;
                height:100px;
                object-fit:cover;
                border-radius:.5rem;
            }
            .sb-price{
                color:var(--sale);
                font-weight:600;
            }
            .prod-card img{
                height:180px;
                object-fit:cover;
            }
        </style>
    </head>
    <body>

        <%@ include file="header.jsp" %>

        <div class="container my-4">
        <div class="row g-3">
            <section class="col-lg-10 col-md-9 col-12 mx-auto">

                <!-- Hero banner -->
                <div class="mb-4">
                    <img src="assets/demo/hero.jpg" class="img-fluid rounded shadow-sm" alt="Banner">
                </div>

                <!-- Section Cửa nhựa -->
                <div class="text-center mb-3">
                    <h3 class="section-title">Danh sách sản phẩm</h3>
                </div>

                <!-- Hiển thị message nếu có -->
                <%
                    String message = (String) request.getAttribute("message");
                    String checkError = (String) request.getAttribute("checkError");
                    if (message != null) {
                %>
                    <div class="alert alert-success alert-dismissible fade show">
                        <%= message %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>
                
                <% if (checkError != null) { %>
                    <div class="alert alert-danger alert-dismissible fade show">
                        <%= checkError %>
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                <% } %>

                <!-- Danh sách sản phẩm -->
                <div class="row g-3">
                    <%
                        List<Products> list = (List<Products>) request.getAttribute("list");
                        if (list != null && !list.isEmpty()) {
                            for (Products p : list) {
                    %>
                        <div class="col-6 col-md-4 col-lg-3 col-xl-2">
                            <div class="card prod-card h-100">
                                <img src="<%= p.getMain_image() != null ? p.getMain_image() : "assets/demo/doorA.jpg" %>" 
                                     class="card-img-top" 
                                     alt="<%= p.getName() %>">
                                <div class="card-body text-center p-2">
                                    <p class="card-text small mb-1 fw-semibold"><%= p.getName() %></p>
                                    <p class="text-muted small mb-1">SKU: <%= p.getSku() %></p>
                                    <p class="mb-0">Giá: 
                                        <span class="fw-bold text-danger">
                                            <%= String.format("%,.0f đ", p.getPrice()) %>
                                        </span>
                                    </p>
                                </div>
                            </div>
                        </div>
                    <%
                            }
                        } else {
                    %>
                        <div class="col-12 text-center py-5">
                            <p class="text-muted fs-5">Chưa có sản phẩm nào</p>
                        </div>
                    <%
                        }
                    %>
                </div>

            </section>
        </div>
    </div

        <%@ include file="footer.jsp" %>

        <!-- Bootstrap 5 JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

