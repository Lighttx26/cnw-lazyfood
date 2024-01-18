<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="lazyfood.demo.models.DTO.OrderDetailsDTO" %>
<%@ page import="lazyfood.demo.models.DTO.ProductInOrderDTO" %>

<% OrderDetailsDTO orderDetails = (OrderDetailsDTO) request.getAttribute("order");
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
String formattedDateTime = orderDetails._Order.OrderDatetime.format(formatter);
String is_delivered = null;
    if (orderDetails._Order.IsDelivered) is_delivered = "Delivered";
    else is_delivered = "Delivering";
%>
<form>
    <div class="modal-header">
        <h4 class="modal-title">Order's Detail</h4>
        <button type="button" class="close" data-dismiss="modal"
            aria-hidden="true">&times;</button>
    </div>
    <div class="modal-body">
        <div class="form-group">
            <label>ID</label>
            <input type="text" class="form-control" name="orderId" value="<%= orderDetails._Order.OrderId%>" readonly>
        </div>
        <div class="form-group">
            <label>Address</label>
            <input type="text" class="form-control" name="address" value="<%= orderDetails._Order.Address%>" readonly>
        </div>
        <div class="form-group">
            <label>Phone</label>
            <input type="text" class="form-control" name="phone" value="<%= orderDetails._Order.PhoneNumber%>" readonly>
        </div>
        <div class="form-group">
            <label>Time</label>
            <input type="text" class="form-control" name="time" value="<%= formattedDateTime%>" readonly>
        </div>
        <div class="form-group">
            <label>Status</label>
            <input type="text" class="form-control" name="status" value="<%= is_delivered%>" readonly>
        </div>

        <div class="form-group">
            <label>Order's Detail</label>
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>Product ID</th>
                        <th>Product Name</th>
                        <th>Quantity</th>
                        <th>Total</th>
                    </tr>
                </thead>
                <tbody id="productBox">
                <% List<ProductInOrderDTO> products = orderDetails.Products;
                    for (ProductInOrderDTO product : products) {
                        %>
                    <tr>
                        <td><%= product.ProductId%></td>
                        <td><%= product.ProductName%></td>
                        <td><%= product.Quantity%></td>
                        <td>$<%= String.format("%.2f",product.Price * product.Quantity)%></td>
                    </tr>
                    <%
                    }
                %>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td><b>$<%= String.format("%.2f",orderDetails.TotalPrice) %></b></td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>
    <div class="modal-footer">
        <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel">
    </div>
</form>