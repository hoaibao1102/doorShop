<%-- 
    Document   : welcome
    Created on : Sep 2, 2025, 12:36:54 PM
    Author     : ADMIN
--%>

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

                <!-- Sidebar Sản phẩm nổi bật -->
                <aside class="col-lg-2 col-md-3 col-12">
                    <div class="border rounded p-2 h-100">
                        <h5 class="text-center fw-bold text-uppercase text-primary mb-3">Sản phẩm nổi bật</h5>

                        <div class="list-group list-group-flush small mb-3" style="max-height:70vh; overflow:auto;">
                            <!-- Item 1 -->
                            <div class="list-group-item d-flex gap-2 align-items-center">
                                <img src="assets/demo/door1.jpg" class="sb-thumb" alt="">
                                <div>
                                    <div>Cửa composite VD-C211</div>
                                    <div class="sb-price">4.650.000 đ</div>
                                </div>
                            </div>
                            <!-- Item 2 -->
                            <div class="list-group-item d-flex gap-2 align-items-center">
                                <img src="assets/demo/door2.jpg" class="sb-thumb" alt="">
                                <div>
                                    <div>Cửa Gỗ CarbonVD2710-7</div>
                                    <div class="sb-price">3.580.000 đ</div>
                                </div>
                            </div>
                            <!-- Item 3 -->
                            <div class="list-group-item d-flex gap-2 align-items-center">
                                <img src="assets/demo/door3.jpg" class="sb-thumb" alt="">
                                <div>
                                    <div>Cửa nhựa ABS Hàn Quốc</div>
                                    <div class="sb-price">3.150.000 đ</div>
                                </div>
                            </div>
                            <!-- Thêm nhiều sản phẩm tùy ý -->
                        </div>

                        <a href="#" class="btn btn-outline-primary w-100">Xem thêm</a>
                    </div>
                </aside>

                <!-- Content chính -->
                <section class="col-lg-10 col-md-9 col-12">

                    <!-- Hero banner -->
                    <div class="mb-4">
                        <img src="assets/demo/hero.jpg" class="img-fluid rounded shadow-sm" alt="Banner">
                    </div>

                    <!-- Section Cửa nhựa -->
                    <div class="text-center mb-3">
                        <h3 class="section-title">Cửa nhựa</h3>
                    </div>

                    <div class="row g-3">
                        <!-- Card sản phẩm -->
                        <div class="col-6 col-md-4 col-lg-3 col-xl-2">
                            <div class="card prod-card h-100">
                                <img src="assets/demo/doorA.jpg" class="card-img-top" alt="">
                                <div class="card-body text-center p-2">
                                    <p class="card-text small mb-1 fw-semibold">COMPOSITE VPB06 – CNC THAN TRE</p>
                                    <p class="mb-0">Giá: <span class="fw-bold text-danger">3.150.000 đ</span></p>
                                </div>
                            </div>
                        </div>

                        <div class="col-6 col-md-4 col-lg-3 col-xl-2">
                            <div class="card prod-card h-100">
                                <img src="assets/demo/doorB.jpg" class="card-img-top" alt="">
                                <div class="card-body text-center p-2">
                                    <p class="card-text small mb-1 fw-semibold">COMPOSITE VPB02 – CHẠY CHỈ NHÔM</p>
                                    <p class="mb-0">Giá: <span class="fw-bold text-danger">3.150.000 đ</span></p>
                                </div>
                            </div>
                        </div>

                        <!-- … thêm nhiều sản phẩm … -->
                    </div>

                    <div class="text-center mt-3">
                        <a href="#" class="btn btn-success rounded-pill px-4">Xem thêm sản phẩm »</a>
                    </div>

                </section>
            </div>
        </div>

        <%@ include file="footer.jsp" %>

        <!-- Bootstrap 5 JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>

