<%@ page import="lazyfood.demo.models.DTO.ProductDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<% ProductDTO product = (ProductDTO) request.getAttribute("product"); %>
<form action="./Product/update" method="post" enctype="multipart/form-data">
    <div class="modal-header">
        <h4 class="modal-title">Edit Product (<%= product.ProductId%>)</h4>
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
    </div>
    <div class="modal-body">
        <div class="form-group">
            <label>Product ID</label>
            <input type="text" value="<%= product.ProductId%>" readonly class="form-control" name="ProductIdE" required>
        </div>
        <div class="form-group">
            <label>Product Name</label>
            <input type="text" value="<%= product.ProductName%>" class="form-control" name="ProductNameE" required>
        </div>
        <div class="form-group"
             style="display:  flex ; align-items: center; justify-content: space-between;">
            <label>Category</label>
            <select name="CategoryIdE" id="">
                <option value="1">Food</option>
                <option value="2">Drink</option>
            </select>
        </div>
        <div class="form-group">
            <label>Price</label>
            <input type="number" step="0.01" value="<%= product.Price%>" class="form-control" name="PriceE" required>
        </div>

        <div class="form-group">
            <label>Image</label>
            <input type="file" name="ImageE" id="image-input">
        </div>

        <div class="form-group" id="image-preview"></div>
    </div>
    <div class="modal-footer">
        <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel">
        <input type="submit" class="btn btn-info" value="Save">
    </div>
</form>

<script>
    document.querySelector('select[name="CategoryIdE"]').value = "<%= product.CategoryId%>";
</script>