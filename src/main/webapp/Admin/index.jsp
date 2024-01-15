<%-- Created by IntelliJ IDEA. User: nguyenhung1903 Date: 12/7/2023 Time: 9:34 PM To change this template use File |
    Settings | File Templates. --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard</title>

    <link rel="stylesheet" href="./assets/css/index.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="./assets/js/utils.js"></script>
</head>

<body>
<table>
    <tr>
        <td style="width:100px;  vertical-align: top;">
            <nav
                    style="position: fixed; display: flex; flex-direction: column; align-items: self-start; justify-content: space-between; padding: 48px 16px 16px 16px; background: white; min-height: 100vh;">
                <section style="width: 100%;">
                    <h1
                            style="text-align: center; width: 100%; margin-bottom: 32px; font-size: 36px; color: #f29a51;">
                        Dashboard
                    </h1>
                    <ul
                            style="list-style: none; gap: 16px; display: flex; flex-direction: column; align-items: self-start; width: 100%;">
                        <li class="list-item">
                            <a href="?page=product" id="product"
                               style="text-decoration: none;  width: 100%; height: 100%; display: block;">Manage Product</a>
                        </li>
                        <li class="list-item">
                            <a href="?page=order" id="order"
                               style="text-decoration: none;  width: 100%; height: 100%; display: block;">Manage Customer's Order</a>
                        </li>
                    </ul>
                </section>
                <section style="display: grid; place-items: center; padding : 16px; width:100%;">
                    <a href="../logout" style="text-decoration: none; color: #f29a51;">Logout</a>
                </section>
            </nav>
        </td>
        <td style="width:auto; vertical-align: top;">
            <div id="content" style="margin-left:200px;">
                <h1 style="text-align: center; width: 100%;margin-left:100px;">ADMIN Homepage</h1>
            </div>
        </td>
    </tr>
</table>
</body>
<script>

    <%
        String role = (String)session.getAttribute("role");
        if ( role == null || !role.equals("admin")) {%>
    alert("You are not admin, please login again");
    window.location.href = "..";
    <%}%>

    $(document).ready(function () {
        var q = getUrlVars();
        if (q["page"] == "product") {
            $(document).prop('title', 'Manage Product');
            $(".list-item").removeClass("active");
            $("#product").addClass("active");

            var keyword = q["keyword"];
            var classId = q["class"];
            var pageLoad = "./Product";
            if (keyword != null && keyword !== "") {
                pageLoad += "?keyword=" + keyword + "&class=" + classId;
            }

            console.log(pageLoad);

            $("#content").load(pageLoad, () => {
                $("#searchBar").on("keydown", function (e) {
                    if (e.which == 13) {
                        console.log($("#searchClass").val());
                        window.location.href = ".?page=product&keyword=" + $(this).val() + "&class=" + $("#searchClass").val();
                    }
                });
            });
        } else if (q["page"] == "order") {
            $(document).prop('title', 'Manage Customer\'s Order');
            $(".list-item").removeClass("active");
            $("#order").addClass("active");
            $("#content").load("./Order");
        }

    });
</script>

</html>