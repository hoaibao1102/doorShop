<%-- 
    Document   : footer
    Created on : Sep 2, 2025, 10:12:16 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>footer</title>
        <style>
            /* ===== Footer – polished alignment & modern look (text color #e0d9d9) ===== */
            .site-footer{
                --primary:#232E76;
                --accent:#78ADDB;
                color:#e0d9d9; /* đổi màu chữ */
                background:
                    radial-gradient(90% 140% at 100% 0%, rgba(120, 173, 219, .15), transparent 42%), linear-gradient(180deg, #19245c 0%, #06070c 100%);
                padding: 20px 0;
                border-top: 1px solid rgba(255,255,255,.06);
            }

            .footer-inner{
                width:min(1200px, 94vw);
                margin-inline:auto;
                display:grid;
                gap: 16px;
                grid-template-columns: 1fr;
                align-items: center;
            }

            .footer-col{
                font-size:14.5px;
                line-height:1.65;
                color:#e0d9d9;
            }
            .footer-col p{
                margin:0;
            }
            .footer-col .label{
                margin:0;
                color:#e0d9d9;
                font-weight:700;
            }
            .footer-col .value{
                margin:0;
                color:#e0d9d9;
                opacity:.9;
            }

            /* Contact panel */
            .footer-col.contact{
                background: linear-gradient(180deg, rgba(255,255,255,.08), rgba(0,0,0,.12));
                border: 1px solid rgba(255,255,255,.12);
                border-radius: 12px;
                padding: 12px;
                display:flex;
                flex-direction:column;
                gap:10px;
                backdrop-filter: saturate(140%) blur(4px);
                box-shadow: inset 0 1px 0 rgba(255,255,255,.06), 0 8px 24px rgba(0,0,0,.18);
            }

            .contact-item{
                display:flex;
                align-items:center;
                gap:10px;
                color:#e0d9d9; /* chữ nhạt hơn */
                text-decoration:none;
                padding:10px 12px;
                border-radius:10px;
                transition: background .2s ease, transform .06s ease, color .2s ease;
            }
            .contact-item:hover{
                background: rgba(120,173,219,.18);
                color:#fff;
            }
            .contact-item:active{
                transform: translateY(1px);
            }
            .contact-item span:last-child{
                font-weight:600;
                letter-spacing:.15px;
            }

            /* Icons */
            .contact-item .ico{
                width:22px;
                height:22px;
                flex:0 0 22px;
                background-size:22px 22px;
                background-repeat:no-repeat;
                filter: drop-shadow(0 1px 3px rgba(0,0,0,.25));
            }
            .contact-item.phone .ico{
                background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ffffff'><path d='M6.62 10.79a15.05 15.05 0 0 0 6.59 6.59l1.87-1.87a1 1 0 0 1 1.02-.24 11.36 11.36 0 0 0 3.56.57 1 1 0 0 1 1 1V20a1 1 0 0 1-1 1A16 16 0 0 1 3 9a1 1 0 0 1 1-1h2.16a1 1 0 0 1 1 1.02c-.02 1.23.18 2.43.46 2.77z'/></svg>");
            }
            .contact-item.mail .ico{
                background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ffffff'><path d='M20 4H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2zm0 4-8 5-8-5V6l8 5 8-5v2z'/></svg>");
            }
            .contact-item.zalo .ico{
                background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ffffff'><path d='M20 2H4a2 2 0 0 0-2 2v14l4-3h14a2 2 0 0 0 2-2V4a2 2 0 0 0-2-2z'/></svg>");
            }

            /* --- Hover accent cho link liên hệ --- */
            .contact-item:hover span:last-child{
                color:#eaf3ff;
            }

            /* ====== Responsive ====== */
            @media (min-width:576px){
                .footer-inner{
                    grid-template-columns: 1.2fr 1fr;
                }
                .footer-col.contact{
                    grid-column: 1 / -1;
                }
            }

            @media (min-width:992px){
                .footer-inner{
                    grid-template-columns: 1.25fr 1.1fr .95fr; /* trái, giữa, phải */
                    gap:18px;
                }
                .footer-col.contact{
                    grid-column: auto;
                }
                /* đảm bảo 3 cột thẳng hàng theo trục giữa */
                .footer-col.about,
                .footer-col.address,
                .footer-col.contact{
                    align-self:center;
                }
            }

        </style>
    </head>
    <body>

        <footer class="site-footer">
            <div class="footer-inner">
                <div class="footer-col about">
                    <p><strong>CỬA GỖ VIỆT PHÁT</strong> chuyên sản xuất, thiết kế thi công nội thất và cửa gỗ công nghiệp, cửa nhựa vân gỗ, cửa chống cháy theo tiêu chuẩn Việt Nam.</p>
                </div>

                <div class="footer-col address">
                    <div class="label">Trụ sở:</div>
                    <div class="value">130 Đại Lộ Bình Dương, phường Phú Lợi, Thành phố Hồ Chí Minh</div>
                </div>

                <aside class="footer-col contact">
                    <a href="tel:0888223779" class="contact-item phone">
                        <span class="ico"></span><span>0888.223.779</span>
                    </a>
                    <a href="mailto:info@vietduchome.vn" class="contact-item mail">
                        <span class="ico"></span><span>Info@vietduchome.vn</span>
                    </a>
                    <a href="https://zalo.me/0888223779" class="contact-item zalo">
                        <span class="ico"></span><span>zalo: 0888 223 779</span>
                    </a>
                </aside>
            </div>
        </footer>

    </body>
</html>
