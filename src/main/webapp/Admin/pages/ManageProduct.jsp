<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="lazyfood.demo.models.Entity.Product" %>
<%@ page import="com.google.gson.Gson" %>
<%@ page import="java.util.List" %>
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
                        color: #f29a51;
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
<%--                <section--%>
<%--                    style="display: flex; flex-direction: column; gap: 32px; align-items: self-start; flex: 6 1 0; padding : 32px 16px; margin:auto;">--%>
<%--                    <section--%>
<%--                        style="display: flex; flex-direction: row-reverse; align-items: center; justify-content: space-between; gap: 16px; width: 100%; padding : 16px; border-radius: 24px;"--%>
<%--                        class="shadow-xl">--%>
<%--                        <section style="width: 100%;">--%>
<%--                            <input type="text"--%>
<%--                                style="padding: 12px 16px; border-radius: 8px; border: 1px solid rgb(128,128,128); width: 60%;"--%>
<%--                                placeholder="Search">--%>
<%--                            <select id="searchClass" name="searchClass"--%>
<%--                                style="padding: 12px 16px; border-radius: 8px; border: 1px solid rgb(128,128,128);">--%>
<%--                                <option value="" selected="selected">Search By</option>--%>
<%--                                <option value="">Product ID</option>--%>
<%--                                <option value="">Product Name</option>--%>
<%--                            </select>--%>
<%--                        </section>--%>
<%--                    </section>--%>
<%--                </section>--%>

                <section style="display: flex; flex-direction: column; gap: 32px; align-items: self-start; flex: 6 1 0; padding : 32px 16px">
                    <section style="display: flex; flex-direction: row-reverse; align-items: center; justify-content: center; gap: 16px; width: 100%; padding : 16px; border-radius: 24px;" class="shadow-xl">
                        <section style="width: 60%;">
                            <input name = "keyword"  id = "searchBar" type="text" style="padding: 12px 16px; border-radius: 8px; border: 1px solid rgb(128,128,128); width: 60%;" placeholder="Search">
                            <select name = "searchClass" id="searchClass" style="padding: 12px 16px; border-radius: 8px; border: 1px solid rgb(128,128,128);">
                                <option value="" selected="selected">Search By</option>
                                <option value="ProductId">Product ID</option>
                                <option value="ProductName">Product Name</option>
                            </select>
                        </section>
                    </section>
                </section>

                <div class="container">
                    <div class="table-wrapper">
                        <div class="table-title">
                            <div class="row">
                                <div class="col-sm-6">
                                    <h2>Manage <b>Product</b></h2>
                                </div>
                                <div class="col-sm-6">
                                    <a href="#addProductModal" class="btn btn-success" data-toggle="modal"><i
                                            class="material-icons">&#xE147;</i> <span>Add New Product</span></a>

                                </div>
                            </div>
                        </div>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Image</th>
                                    <th>Product ID</th>
                                    <th>Product Name</th>
                                    <th>Category Type</th>
                                    <th>Price</th>
                                </tr>
                            </thead>
                            <tbody id="productBox">
                                <%
                                        List<Product> products = (List<Product>) request.getAttribute("products");
                                        Gson gson = new Gson();
                                        String jsonProducts = gson.toJson(products);
                                        for (int i = 0; i < products.size(); i++) { %>
                                            <tr>
                                                <td style="width: 100px"><img
                                                        src="data:image/jpeg;base64,<%=products.get(i).getImage()%>"
                                                        width="80%" /></td>
                                                <td>
                                                    <%= products.get(i).getProductId()%>
                                                </td>
                                                <td>
                                                    <%= products.get(i).getProductName()%>
                                                </td>
                                                <td>
                                                    <%= products.get(i).getCategory().getCategoryName()%>
                                                </td>
                                                <td>
                                                    $<%= String.format("%.2f", products.get(i).getPrice()) %>
                                                </td>
                                                <td>
                                                    <a href="#editProductModal" class="edit" data-toggle="modal"
                                                        data-product-id=<%=products.get(i).getProductId()%> ><i
                                                            class="material-icons" data-toggle="tooltip"
                                                            title="Edit">&#xE254;</i></a>
                                                    <a href="#deleteProductModal" class="delete" data-toggle="modal"
                                                        data-product-id=<%=products.get(i).getProductId()%> ><i
                                                            class="material-icons" data-toggle="tooltip"
                                                            title="Delete">&#xE872;</i></a>
                                                </td>
                                            </tr>
                                            <% } %>


                            </tbody>
                        </table>

                    </div>
                    <!-- Add Modal HTML -->
                    <div id="addProductModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="Product/create" method="post" enctype="multipart/form-data">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Add Product</h4>
                                        <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">&times;</button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="form-group">
                                            <label>Product ID</label>
                                            <input type="text" class="form-control" name="ProductId" required>
                                        </div>
                                        <div class="form-group">
                                            <label>Product Name</label>
                                            <input type="text" class="form-control" name="ProductName" required>
                                        </div>
                                        <div class="form-group"
                                            style="display:  flex ; align-items: center; justify-content: space-between;">
                                            <label>Category</label>
                                            <select name="CategoryId" id="">
                                                <option value="1" selected>Food</option>
                                                <option value="2">Drink</option>
                                            </select>
                                        </div>
                                        <div class="form-group">
                                            <label>Price</label>
                                            <input type="number" step="0.01" class="form-control" name="Price" required>
                                        </div>

                                        <div class="form-group">
                                            <label>Image</label>
                                            <input type="file" name="Image" id="image-input">
                                        </div>

                                        <div class="form-group" id="image-preview"></div>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="button" class="btn btn-default" data-dismiss="modal"
                                            value="Cancel">
                                        <input type="submit" class="btn btn-success" value="Add">
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <!-- Edit Modal HTML -->
                    <div id="editProductModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">

                            </div>
                        </div>
                    </div>
                    <!-- Delete Modal HTML -->
                    <div id="deleteProductModal" class="modal fade">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <form action="Product/delete" method="delete">
                                    <div class="modal-header">
                                        <h4 class="modal-title">Delete Product</h4>
                                        <button type="button" class="close" data-dismiss="modal"
                                            aria-hidden="true">&times;</button>
                                    </div>
                                    <div class="form-group">
                                        <label hidden>Product ID</label>
                                        <input hidden type="text" readonly class="form-control" name="ProductIdD"
                                            required>
                                    </div>
                                    <div class="modal-body">
                                        <p>Are you sure you want to delete this product?</p>
                                        <p class="text-warning"><small>This action cannot be undone.</small></p>
                                    </div>
                                    <div class="modal-footer">
                                        <input type="button" class="btn btn-default" data-dismiss="modal"
                                            value="Cancel">
                                        <input type="submit" class="btn btn-danger" value="Delete">
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            <script>
                const js_products = <%= jsonProducts %>;

                const editButtons = document.querySelectorAll('.edit');
                editButtons.forEach(function (editButton) {
                    editButton.addEventListener('click', function (event) {
                        event.preventDefault();

                        const productId = this.getAttribute('data-product-id');
                        populateFormFields(productId);
                    });
                });

                // function getProductById(productId) {
                //     return js_products.find(prod => prod.ProductId == productId);
                // }


                function populateFormFields(productId) {
                    $('#editProductModal .modal-content').load('./Product/update?ProductId=' + productId);
                    // const productIdInput = document.querySelector('input[name="ProductIdE"]');
                    // const productNameInput = document.querySelector('input[name="ProductNameE"]');
                    // const categoryIdInput = document.querySelector('select[name="CategoryIdE"]');
                    // const priceInput = document.querySelector('input[name="PriceE"]');
                    //
                    // productIdInput.value = product.ProductId;
                    // productNameInput.value = product.ProductName;
                    // categoryIdInput.value = product.CategoryId;
                    // priceInput.value = product.Price;
                }

                const imageInput = document.getElementById('image-input');
                const imagePreview = document.getElementById('image-preview');

                imageInput.addEventListener('change', function (event) {
                    const file = event.target.files[0];

                    const reader = new FileReader();

                    reader.onload = function (e) {
                        const img = document.createElement('img');
                        img.src = e.target.result;;
                        img.classList.add('preview-image');
                        imagePreview.innerHTML = '';
                        imagePreview.appendChild(img);
                    }

                    reader.readAsDataURL(file);
                });

                const deleteButtons = document.querySelectorAll('.delete');
                deleteButtons.forEach(function (deleteButton) {
                    deleteButton.addEventListener('click', function (event) {
                        event.preventDefault();

                        const productId = this.getAttribute('data-product-id');
                        const productIdInput = document.querySelector('input[name="ProductIdD"]');
                        productIdInput.value = productId;
                    });
                });

                $(document).ready(()=>{
                    var q = getUrlVars();
                    $("#searchBar").val(q["keyword"]);
                    $("#searchClass").val(q["class"]);
                });
            </script>