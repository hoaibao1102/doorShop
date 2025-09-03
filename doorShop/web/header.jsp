<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>header</title>
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <style>
    :root{
      --accent:#78ADDB;
      --primary:#232E76;
      --text:#0f172a;
      --line:#d7e2f3;
    }
    *{ box-sizing:border-box }
    img{ max-width:100%; display:block }

    .site-header{
      background:#fff;
      border-bottom:1px solid var(--line);
      box-shadow:0 2px 10px rgba(2,8,23,.04);
      position:sticky; top:0; z-index:50;
    }

    /* ===== TOP BAR ===== */
    .hdr-top{
      width:min(1200px,94vw);
      margin-inline:auto;
      display:grid;
      grid-template-columns:auto auto 1fr auto auto;
      grid-template-areas:"brand contacts search login burger";
      gap:14px; align-items:center; padding:10px 0;
    }
    .area-brand{ grid-area:brand; }
    .area-contacts{ grid-area:contacts; }
    .area-search{ grid-area:search; }
    .area-login{ grid-area:login; }
    .area-burger{ grid-area:burger; justify-self:end; }

    .brand{ display:flex; align-items:center; text-decoration:none; }
    .brand-full{ height:64px; width:auto; display:block; }

    .hdr-contacts{ display:flex; flex-direction:column; align-items:flex-start; gap:4px; margin-left:8px; }
    .c-item{ display:flex; align-items:center; gap:8px; color:#0b2a3a; text-decoration:none; font-weight:600; }
    .c-item .ico{ width:18px; height:18px; flex:0 0 18px; background-size:18px 18px; background-repeat:no-repeat; opacity:.9; }
    .c-item.mail .ico{
      background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%230b2a3a'><path d='M20 4H4a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2zm0 4-8 5-8-5V6l8 5 8-5v2z'/></svg>");
    }
    .c-item.phone .ico{
      background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%230b2a3a'><path d='M6.62 10.79a15.05 15.05 0 0 0 6.59 6.59l1.87-1.87a1 1 0 0 1 1.02-.24 11.36 11.36 0 0 0 3.56.57 1 1 0 0 1 1 1V20a1 1 0 0 1-1 1A16 16 0 0 1 3 9a1 1 0 0 1 1-1h2.16a1 1 0 0 1 1 1.02c-.02 1.23.18 2.43.46 2.77z'/></svg>");
    }

    .hdr-search{
      width:100%; display:flex; align-items:center; gap:0; height:40px;
      border:1px solid #bcd0ec; border-radius:14px; overflow:hidden;
    }
    .hdr-search input{
      border:0; outline:none; padding:0 14px; height:100%;
      width:100%; min-width:0; font-size:16px; color:var(--text);
    }
    .hdr-search input::placeholder{ color:#8aa1bb; }
    .hdr-search button{ width:44px; height:100%; border:0; cursor:pointer; background:#0b3e63; position:relative; }
    .hdr-search button .ico{
      position:absolute; inset:0; margin:auto; width:20px; height:20px; background-size:20px 20px; background-repeat:no-repeat;
      background-image:url("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='%23ffffff'><path d='M10 2a8 8 0 105.293 14.293l4.207 4.207 1.414-1.414-4.207-4.207A8 8 0 0010 2zm0 2a6 6 0 110 12 6 6 0 010-12z'/></svg>");
    }

    .btn-login{
      display:inline-flex; align-items:center; justify-content:center;
      height:40px; padding:0 16px; border-radius:10px;
      background:#eaf2ff; border:1px solid #bcd0ec; color:#0b2a3a; text-decoration:none; font-weight:700;
      transition:background .2s, box-shadow .2s, transform .05s;
    }
    .btn-login:hover{ background:#dfeaff; box-shadow:0 6px 14px rgba(12,46,118,.16); }
    .btn-login:active{ transform:translateY(1px); }

    /* ===== BURGER ===== */
    .nav-toggle{ position:absolute; left:-9999px; }
    .burger{
      width:44px; height:40px; display:none; cursor:pointer;
      border:1px solid #bcd0ec; border-radius:10px; align-items:center; justify-content:center;
      background:#fff;
    }
    .burger span,
    .burger::before,
    .burger::after{
      content:""; display:block; width:22px; height:2px; background:#0b2a3a; transition:transform .2s, opacity .2s;
    }
    .burger::before{ margin-top:0; transform:translateY(-6px); }
    .burger::after{ transform:translateY(6px); }
    /* animation khi mở */
    .nav-toggle:checked ~ .hdr-nav ~ .hdr-top .burger span{ opacity:0; }
    .nav-toggle:checked ~ .hdr-nav ~ .hdr-top .burger::before{ transform:rotate(45deg); }
    .nav-toggle:checked ~ .hdr-nav ~ .hdr-top .burger::after{ transform:rotate(-45deg); }

    /* ===== NAV (menu) ====== */
    .hdr-nav{
      border-top:1px solid var(--line);
      background:linear-gradient(180deg,#fff 0%,#f7fbff 100%);
    }
    .nav-list{
      width:min(1200px,94vw);
      margin:0 auto;
      padding:8px 0;
      list-style:none;
      display:flex;
      gap:24px;
      align-items:center;
      flex-wrap:wrap;
    }
    .nav-list > li{ position:relative; }
    .nav-list > li > a{
      text-decoration:none;
      color:#111;
      font-weight:800;
      letter-spacing:.2px;
      padding:6px 0;
      display:inline-flex; align-items:center; gap:8px;
      position:relative;
    }

    /* underline nhẹ khi hover mục có submenu */
    .has-sub > a::before{
      content:"";
      width:0; height:2px; background:var(--accent);
      position:absolute; left:0; bottom:0; transition:width .2s;
    }
    .has-sub:hover > a::before{ width:100%; }

    /* ICON CHEVRON chỉ cho mục có submenu */
    .has-sub > a::after{
      content:"";
      display:inline-block; width:12px; height:12px; flex:0 0 12px; margin-left:2px;
      background-repeat:no-repeat; background-size:12px 12px;
      background-image:url("data:image/svg+xml;utf8,\
        <svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%230f172a' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'>\
          <path d='M9 5l7 7-7 7'/>\
        </svg>");
      transition:transform .2s ease;
    }
    .has-sub.open > a::after{ transform:rotate(90deg); }

    .sub-menu{
      position:absolute; top:100%; left:0; min-width:260px;
      background:#fff; border:1px solid #e2e8f0; border-radius:8px; padding:8px 0;
      display:none; flex-direction:column; z-index:100;
      box-shadow:0 12px 24px rgba(2,8,23,.12);
      list-style:none; margin:0;
    }
    .has-sub.open > .sub-menu{ display:flex; }
    .sub-menu li a{
      display:block; padding:9px 14px; text-decoration:none;
      color:#232E76; font-weight:600; transition:background .2s, color .2s;
    }
    .sub-menu li a:hover{ background:rgba(120,173,219,.15); color:#0f172a; }

    /* ====== RESPONSIVE ====== */

    /* Desktop (≥1200): căn giữa menu */
    @media (min-width:1200px){
      .nav-list{ justify-content:center; }
    }

    /* Tablet & Mobile */
    @media (max-width:1199.98px){
      /* Sắp xếp lại khu vực top:
         row1: brand | burger
         row2: contacts
         row3: search
         row4: login
      */
      .hdr-top{
        grid-template-columns:1fr auto;
        grid-template-areas:
          "brand burger"
          "contacts contacts"
          "search search"
          "login login";
        gap:10px;
      }
      .hdr-contacts{ margin-left:0 }
      .burger{ display:flex; }

      /* Ẩn nav mặc định, chỉ mở khi toggle */
      .hdr-nav{ display:none; }
      .nav-toggle:checked + .hdr-nav{ display:block; }

      .nav-list{
        flex-direction:column; align-items:stretch; gap:0; padding:8px 0;
      }
      .nav-list > li{ border-top:1px solid #eef2f7; }
      .nav-list > li > a{
        display:flex; justify-content:space-between; padding:12px 16px;
      }
      .has-sub > a::after{ margin-left:12px; }
      .sub-menu{
        position:static; border:0; border-top:1px dashed #e2e8f0;
        box-shadow:none; padding:4px 0; margin:0 4px 8px 16px;
        border-left:3px solid rgba(120,173,219,.35);
      }
      .sub-menu li a{ padding:10px 12px; }
    }

    /* Focus-visible cho accessibility */
    a:focus-visible, button:focus-visible, .burger:focus-visible{
      outline:2px solid var(--accent); outline-offset:2px;
    }
  </style>
</head>
<body>

<header class="site-header">
  <div class="hdr-top">
    <!-- Logo -->
    <a href="welcome.jsp" class="brand area-brand">
      <img src="https://wallpaperbat.com/img/79111-purple-sunset-desktop-background-purple.jpg" alt="Hòa Phát Door" class="brand-full">
    </a>

    <!-- Contacts -->
    <div class="hdr-contacts area-contacts">
      <a href="mailto:info@vietduchome.vn" class="c-item mail"><span class="ico"></span><span>INFO@VIETDUCHOME.VN</span></a>
      <a href="tel:0888223779" class="c-item phone"><span class="ico"></span><span>0888 22 37 79</span></a>
    </div>

    <!-- Search -->
    <form class="hdr-search area-search" action="search" method="get" role="search">
      <input type="text" name="q" placeholder="Tìm kiếm…" aria-label="Tìm kiếm">
      <button type="submit" aria-label="Tìm"><span class="ico"></span></button>
    </form>

    <!-- Login -->
    <a href="login.jsp" class="btn-login area-login">LOGIN</a>

    <!-- Hamburger -->
    <label for="navToggle" class="burger area-burger" aria-label="Mở menu" aria-controls="mainNav"><span></span></label>
  </div>

  <!-- Toggle đặt ngay trước nav để selector hoạt động trên mobile -->
  <input id="navToggle" class="nav-toggle" type="checkbox" aria-hidden="true">

  <!-- NAV -->
  <nav id="mainNav" class="hdr-nav">
    <ul class="nav-list">
      <li><a href="welcome.jsp">HOME</a></li>

      <li class="has-sub">
        <a href="#">CỬA NHỰA</a>
        <ul class="sub-menu">
          <li><a href="#">CỬA NHỰA COMPOSITE THƯỜNG</a></li>
          <li><a href="#">CỬA NHỰA COMPOSITE CAO CẤP</a></li>
          <li><a href="#">CỬA NHỰA ABS HÀN QUỐC</a></li>
          <li><a href="#">CỬA NHỰA ĐÀI LOAN</a></li>
          <li><a href="#">CỬA NHỰA PVC</a></li>
        </ul>
      </li>

      <li class="has-sub">
        <a href="#">CỬA GỖ</a>
        <ul class="sub-menu">
          <li><a href="#">CỬA GỖ TỰ NHIÊN</a></li>
          <li><a href="#">CỬA GỖ CARBON</a></li>
          <li><a href="#">CỬA GỖ HDF</a></li>
          <li><a href="#">CỬA GỖ MDF AN CƯỜNG</a></li>
        </ul>
      </li>

      <li class="has-sub">
        <a href="#">CỬA THÉP CHỐNG CHÁY</a>
        <ul class="sub-menu">
          <li><a href="#">CỬA THÉP PCCC 2 CÁNH</a></li>
          <li><a href="#">CỬA THÉP PCCC 1 CÁNH</a></li>
          <li><a href="#">CỬA THÉP VÂN GỖ</a></li>
        </ul>
      </li>

      <li><a href="#">THIẾT BỊ VỆ SINH</a></li>
      <li><a href="#">PHỤ KIỆN CỬA</a></li>
      <li><a href="#">TIN TỨC</a></li>
    </ul>
  </nav>
</header>

<!-- JS: dropdown toggle bằng click (mọi kích thước) + accordion mobile + đóng khi click ngoài/Esc -->
<script>
  (function () {
    const header = document.querySelector('.site-header');
    const nav = header.querySelector('.nav-list');
    const navToggle = header.querySelector('#navToggle');
    if (!nav) return;

    // Toggle submenu bằng click ở MỌI kích thước
    nav.querySelectorAll('.has-sub > a').forEach(a => {
      a.setAttribute('role','button');
      a.setAttribute('aria-expanded','false');

      a.addEventListener('click', function (e) {
        e.preventDefault();
        const li = this.parentElement;

        // Đóng các submenu khác
        nav.querySelectorAll('.has-sub.open').forEach(openLi => {
          if (openLi !== li) {
            openLi.classList.remove('open');
            const link = openLi.querySelector(':scope > a');
            if (link) link.setAttribute('aria-expanded','false');
          }
        });

        // Toggle submenu hiện tại
        li.classList.toggle('open');
        this.setAttribute('aria-expanded', li.classList.contains('open'));
      });
    });

    // Click ngoài để đóng tất cả
    document.addEventListener('click', function (e) {
      const insideHeader = e.target.closest('.site-header');
      if (!insideHeader) {
        nav.querySelectorAll('.has-sub.open').forEach(li => {
          li.classList.remove('open');
          const link = li.querySelector(':scope > a');
          if (link) link.setAttribute('aria-expanded','false');
        });
        if (navToggle) navToggle.checked = false;
      }
    });

    // Esc để đóng
    document.addEventListener('keydown', function (e) {
      if (e.key === 'Escape') {
        nav.querySelectorAll('.has-sub.open').forEach(li => {
          li.classList.remove('open');
          const link = li.querySelector(':scope > a');
          if (link) link.setAttribute('aria-expanded','false');
        });
        if (navToggle) navToggle.checked = false;
      }
    });

    // Khi chọn link con trên mobile/tablet thì đóng menu hamburger
    const mqSmall = window.matchMedia('(max-width: 1199.98px)');
    nav.querySelectorAll('.sub-menu a, li:not(.has-sub) > a').forEach(link => {
      link.addEventListener('click', () => {
        if (mqSmall.matches && navToggle) navToggle.checked = false;
      });
    });
  })();
</script>

</body>
</html>
