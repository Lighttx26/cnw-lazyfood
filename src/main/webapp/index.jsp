<%-- <%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %> --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
  <!DOCTYPE html>
  <html>

  <head>
    <meta charset="ISO-8859-1">
    <title>Homepage</title>
    <link rel="stylesheet" href="./assets/css/index.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css"
      integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA=="
      crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@300;400;500;600;700&display=swap" rel="stylesheet">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  </head>

  <body>
    <header id="home" style="color:white; width: 100%; position: fixed; transition: all .2s linear; z-index: 100; left: 50%;
    transform: translateX(-50%);">
      <nav id="navbar" style="display: flex; align-items: center; justify-content: space-between; max-width: 1440px; width:100%; margin: auto;
                ">
        <section style="min-width: 480px;">
          <img src="./assets/imgs/logo.png" alt="" width="120px" height="120px" style="cursor: pointer;"
            onclick=" window.location = './';">
        </section>

        <section style="min-width: 480px; display: grid; place-items: center;">
          <ul style="display: flex; align-items: center; gap:32px; list-style: none;">
            <li class="nav-item" onclick=" window.location = './#home';">Home</li>
            <li class="nav-item" onclick=" window.location = './#products';">Product</li>
            <li class="nav-item" onclick=" window.location = './#footer';">Contact</li>
          </ul>
        </section>

        <section style="display: flex; align-items: center; gap:16px; min-width: 440px;">

          <%
            String userId = (String) session.getAttribute("userid");
            String username = (String) session.getAttribute("username");
            String role = (String) session.getAttribute("role");
            if(userId != null){
          %>
            <button class="btn" onclick="window.location = './logout'">( <%= username %>) LOGOUT</button>
            <% if (role.equals("admin")) {%>
              <button class="btn" onclick="window.location = './Admin';">Dashboard</button>
            <% } else {  %>
                <button class="btn" onclick="window.location = './Order';">My orders</button>
           <% } %>
            <span onclick="handleCart()" class="opacity"
                  style="padding: 12px; border: 2px solid white; background: transparent; cursor: pointer; border-radius: 8px; position: relative;">
              <i class="fa fa-cart-shopping"></i>
              <span id="cartCount" style="position: absolute; border-radius: 100%; background: red; color: white; top: 0;
                              right: 4px;
                              font-size: 12px;
                              width: 20px;
                              height: 20px;
                              display: none;
                              place-items: center;">0</span>
            </span>
          <%
            }else{ %>
            <button class="btn" onclick="handlePopupLogin()">LOGIN</button>
            <button class="btn" onclick="handlePopupSignUp()">SIGN UP</button>
          <% } %>
        </section>
      </nav>
    </header>
    <section class="shop-ani"
      style="position: absolute; bottom:10%; left: 50%; transform: translateX(-50%); display: flex; flex-direction: column;">
      <button class="btn" style="background: #f29a51; border: none;" onclick=" window.location = './#products';">Shop
        Now !</button>
      <i class="fa-solid fa-chevron-down" style="color: white; text-align: center;"></i>
    </section>
    <section
      style="min-height: 100vh; display: flex; flex-direction: column; gap:32px; align-items: center; max-width: 1440px; width:100%; margin: 100vh auto 0 auto;">
      <h1 style="text-align: center; color: white;">Products</h1>
      <section
        style="align-self: flex-start; display: flex; justify-content: space-between; align-items: center; width: 100%;">
        <section style="display: flex; gap:32px">
          <section style="display: flex; flex-direction: column; gap: 16px; padding: 16px;">
            <span style="color: white;">Filter By Category</span>
            <select id="categoryField">
              <option value="0">All</option>
              <option value="1">Food</option>
              <option value="2">Drink</option>
            </select>
          </section>
        </section>
        <section style="display: flex; gap:32px; align-items: center;">
          <input type="text" placeholder="Type name product" id="search-input"
            style="padding:8px 16px; color: white; border:2px solid white; border-radius: 8px; background: transparent;">
        </section>
      </section>
      <section id="products" style="padding: 16px; background: #111111; border-radius: 24px;  grid-template-columns: 1fr 1fr 1fr 1fr;" class="parent">

      </section>
    </section>
    <footer id="footer"
      style="display: flex; gap:48px; align-items: start; color: white; max-width: 1440px; width: 100%; margin: auto; padding: 48px 0;">
      <section style="display: flex; flex-direction: column; gap: 8px; align-items: start; flex: 2 1 0%;">
        <h1>ABOUT</h1>
        <p style="font-size: 14px; color: #3f3f3f;">Lorem ipsum dolor sit amet consectetur adipisicing elit.
          Tenetur officia consequuntur, laborum
          accusamus quasi aliquam sequi id impedit totam pariatur neque ipsa sed nobis adipisci nam
          consectetur. Ea, delectus porro.</p>
      </section>
      <section style="display: flex; flex-direction: column; gap: 8px; align-items: start; flex: 1 1 0%;">
        <h1>MEMBERS</h1>
        <ul
          style="display: flex; flex-direction: column; gap: 8px; align-items: start; list-style: none; font-size: 14px;">
          <li>Nguyen An Hung</li>
          <li>Nguyen Cuu Nhat Quang</li>
          <li>Nguyen Tri Hoai Thuong</li>
        </ul>
      </section>
      <section style="display: flex; flex-direction: column; gap: 8px; align-items: start; flex: 1 1 0%;">
        <h1>QUICK LINKS</h1>
        <ul
          style="display: flex; flex-direction: column; gap: 8px; align-items: start; list-style: none; font-size: 14px;">
          <li><a href="#home" style="color: white; text-decoration: none;">Home</a></li>
          <li><a href="#product" style="color: white; text-decoration: none;">Product</a></li>
        </ul>
      </section>
    </footer>

    <section id="login-modal" class="modal-popup login-popup">
      <button class="form-close-btn" onclick="handleClosePopup('login')"><i class="fa-solid fa-xmark"
          style="font-size: 16px; color: white;"></i></button>
      <form method="post" action="${pageContext.request.contextPath}/login"
        style="display: flex; flex-direction: column; gap: 8px; width: 80%; align-items: self-start;">
        <h1 style="line-height: 150%;">LOGIN</h1>
        <p id="loginStatus" style="color:red"></p>
        <input name="username" class="login-inp" type="text" placeholder="Username" onchange="validateLogin()">
        <input name="password" class="login-inp" type="password" placeholder="Password" onchange="validateLogin()">
        <button class="btn" style="background: #f29a51; border: none; width: 100%; margin-top: 16px;">LOGIN</button>
      </form>
    </section>

    <section id="signup-modal" class="modal-popup signup-popup">
      <button class="form-close-btn" onclick="handleClosePopup()"><i class="fa-solid fa-xmark"
          style="font-size: 16px; color: white;"></i></button>
      <form method="post" action="${pageContext.request.contextPath}/register" style="display: flex; flex-direction: column; gap: 8px; width: 80%; align-items: self-start;">
        <h1 style="line-height: 150%;">SIGN UP</h1>
        <input name="name" class="login-inp" type="text" placeholder="Fullname">
        <input name="usernameS" class="login-inp" type="text" placeholder="Username" onchange="validateLogin()">
        <span class="validate-text user-length"><i class="fa-regular fa-circle-check"></i> Username must has at
          least 3
          letters</span>
        <span class="validate-text user-letter"><i class="fa-regular fa-circle-check"></i> Username must not
          start by a
          number</span>
        <input name="passwordS" class="login-inp" type="password" placeholder="Password" onchange="validateLogin()">
        <span class="validate-text pass-length"><i class="fa-regular fa-circle-check"></i> Password must has at
          least 3
          letters</span>
        <span class="validate-text pass-letter"><i class="fa-regular fa-circle-check"></i> Password must
          contains letters
          and numbers</span>
        <input type="tel" name="phone" id="phone" class="login-inp"  pattern="0[0-9]{9}" placeholder="Phone">
        <input name="addr" class="login-inp" type="text" placeholder="Address">
        <button class="btn" style="background: #f29a51; border: none; width: 100%; margin-top: 16px;">SIGN
          UP</button>
      </form>
    </section>

    <section id="cart-modal" class="cart-container">
      <button class="form-close-btn" style=" left: 10px!important;top: 10px!important" onclick="handleCart('close')"><i
          class="fa-solid fa-xmark" style="font-size: 16px; color: white;"></i></button>
      <h1 style="text-align: center; margin-top: 32px; width: 100%;">CART</h1>
      <section id="cartSection"
        style="display: flex; flex-direction: column; gap: 8px; width: 100%; margin-top: 48px; max-height: 70%; overflow: auto;">

      </section>
      <div style="width: 100%; height: 1px; background-color: rgb(128,128,128);"></div>

      <button class="btn" style="background: #f29a51; border: none; width: 100%; margin-top: 16px;"
        onclick="handleShowConfirm()">Purchase</button>
    </section>

    <section id="confirm-modal" class="modal-popup confirm-popup">

    </section>
  </body>
  <script src="./assets/js/login.js"></script>
  <script src="./assets/js/product.js"></script>
  <script src="./assets/js/general.js"></script>
  <script src="./assets/js/cart.js"></script>
  <script src="./assets/js/confirm.js"></script>


  <%
    String error = (String) request.getAttribute("error");
    if(error != null){
  %>
  <script>
    $("#loginStatus").text("<%= error %>");
    handlePopupLogin();
  </script>
  <%}%>

  </html>