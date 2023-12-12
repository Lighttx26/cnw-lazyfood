<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="lazyfood.demo.models.Bean.Order" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Manage Order</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto|Varela+Round">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="./index.css">
    <style>
        .shadow-xl {
            box-shadow: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
        }

        .preview-image {
            width: 50%;
        }

        body {
            color: #566787;
            background: #f5f5f5;
            font-family: 'Varela Round', sans-serif;
            font-size: 13px;
        }

        .table-wrapper {
            background: #fff;
            padding: 20px 25px;
            margin: 30px 0;
            border-radius: 3px;
            box-shadow: 0 1px 1px rgba(0, 0, 0, .05);
        }

        .table-title {
            padding-bottom: 15px;
            background: #f29a51;
            color: #fff;
            padding: 16px 30px;
            margin: -20px -25px 10px;
            border-radius: 3px 3px 0 0;
        }

        .table-title h2 {
            margin: 5px 0 0;
            font-size: 24px;
        }

        .table-title .btn-group {
            float: right;
        }

        .table-title .btn {
            color: #fff;
            float: right;
            font-size: 13px;
            border: none;
            min-width: 50px;
            border-radius: 2px;
            border: none;
            outline: none !important;
            margin-left: 10px;
        }

        .table-title .btn i {
            float: left;
            font-size: 21px;
            margin-right: 5px;
        }

        .table-title .btn span {
            float: left;
            margin-top: 2px;
        }

        table.table tr th,
        table.table tr td {
            border-color: #e9e9e9;
            padding: 12px 15px;
            vertical-align: middle;
        }

        table.table tr th:first-child {
            width: 60px;
        }

        table.table tr th:last-child {
            width: 100px;
        }

        table.table-striped tbody tr:nth-of-type(odd) {
            background-color: #fcfcfc;
        }

        table.table-striped.table-hover tbody tr:hover {
            background: #f5f5f5;
        }

        table.table th i {
            font-size: 13px;
            margin: 0 5px;
            cursor: pointer;
        }

        table.table td:last-child i {
            opacity: 0.9;
            font-size: 22px;
            margin: 0 5px;
        }

        table.table td a {
            font-weight: bold;
            color: #566787;
            display: inline-block;
            text-decoration: none;
            outline: none !important;
        }

        table.table td a:hover {
            color: #2196F3;
        }

        table.table td a.edit {
            color: #FFC107;
        }

        table.table td a.delete {
            color: #F44336;
        }

        table.table td i {
            font-size: 19px;
        }

        table.table .avatar {
            border-radius: 50%;
            vertical-align: middle;
            margin-right: 10px;
        }


        /* Modal styles */
        .modal .modal-dialog {
            max-width: 400px;
        }

        .modal .modal-header,
        .modal .modal-body,
        .modal .modal-footer {
            padding: 20px 30px;
        }

        .modal .modal-content {
            border-radius: 3px;
            width: 200%;
            left: -30%;
        }

        .modal .modal-footer {
            background: #ecf0f1;
            border-radius: 0 0 3px 3px;
        }

        .modal .modal-title {
            display: inline-block;
        }

        .modal .form-control {
            border-radius: 2px;
            box-shadow: none;
            border-color: #dddddd;
        }

        .modal textarea.form-control {
            resize: vertical;
        }

        .modal .btn {
            border-radius: 2px;
            min-width: 100px;
        }

        .modal form label {
            font-weight: normal;
        }
    </style>

<body>
    <section
        style="display: flex; flex-direction: column; gap: 32px; align-items: self-start; flex: 6 1 0; padding : 32px 16px">
        <section
            style="display: flex; flex-direction: row-reverse; align-items: center; justify-content: center; gap: 16px; width: 100%; padding : 16px; border-radius: 24px;"
            class="shadow-xl">
            <section style="width: 60%;">
                <input type="text"
                    style="padding: 12px 16px; border-radius: 8px; border: 1px solid rgb(128,128,128); width: 60%;"
                    placeholder="Search">
                <select id="searchClass" name="searchClass"
                    style="padding: 12px 16px; border-radius: 8px; border: 1px solid rgb(128,128,128);">
                    <option value="" selected="selected">Search By</option>
                    <option value="">Order ID</option>
                    <option value="">Customer's Name</option>
                </select>
            </section>
        </section>
    </section>

    <div class="container">
        <div class="table-wrapper">
            <div class="table-title">
                <div class="row">
                    <div class="col-sm-6">
                        <h2>Manage <b>Order</b></h2>
                    </div>

                </div>
            </div>
            <table class="table table-striped table-hover">
                <colgroup>
                    <col span="1" style="width: 15%;">
                    <col span="1" style="width: 15%;">
                </colgroup>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer's ID</th>
                        <th>Customer's Name</th>
                        <th>Address</th>
                        <th>Phone</th>
                        <th>Time</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody id="orderBox">
                        <%
                            ArrayList<Order> orders = (ArrayList<Order>) request.getAttribute("orders");
                            for (int i = 0; i < orders.size(); i++) {
                                String isdelivered = null;
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                                String formattedDateTime = orders.get(i).getTime().format(formatter);
                                if (orders.get(i).isDelivered()) {
                                    isdelivered = "Delivered";
                                } else {
                                    isdelivered = "Delivering";
                                }
                            %>
                        <tr>
                            <td> <%= orders.get(i).getOrderId() %> </td>
                            <td> <%= orders.get(i).getCustomerId() %> </td>
                            <td> <%= orders.get(i).getCustomerName() %> </td>
                            <td> <%= orders.get(i).getAddress() %> </td>
                            <td> <%= orders.get(i).getPhoneNumber() %> </td>
                            <td> <%= formattedDateTime %> </td>
                            <td> <%= isdelivered %> </td>
                            <td><a href="#viewDetailModal" class="detail" data-toggle="modal" data-product-id=<%= orders.get(i).getOrderId() %>><i
                                    class="material-icons" data-toggle="tooltip" title="Detail ">&#xe8f4;</i></a></td>
                        </tr>
                        <% } %>

                </tbody>
            </table>

        </div>
        <div id="viewDetailModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">

                </div>
            </div>
        </div>
    </div>
</body>

<script>
    const detailButtons = document.querySelectorAll('.detail');

    detailButtons.forEach(function (detailButton) {
        detailButton.addEventListener('click', function (event) {
            event.preventDefault();

            const orderId = this.getAttribute('data-product-id');
            //get orderId to get product logic here
            $('#viewDetailModal .modal-content').load('./Order/view?OrderId=' + orderId);
        });
    });

    const detailButton = document.querySelector('.detail');

</script>

</html>