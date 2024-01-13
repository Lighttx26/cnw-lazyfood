<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="lazyfood.demo.models.Entity.Order" %>
<%@ page import="lazyfood.demo.models.Entity.ProductInOrder" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="lazyfood.demo.models.Entity.Product" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<% Order order = (Order) request.getAttribute("order");
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
String formattedDateTime = order.getTime().format(formatter);
String isdelivered = null;
    if (order.isDelivered()) isdelivered = "Delivered";
    else isdelivered = "Delivering";
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
            <input type="text" class="form-control" name="orderId" value="<%= order.getOrderId()%>" readonly>
        </div>
        <div class="form-group">
            <label>Address</label>
            <input type="text" class="form-control" name="address" value="<%= order.getAddress()%>" readonly>
        </div>
        <div class="form-group">
            <label>Phone</label>
            <input type="text" class="form-control" name="phone" value="<%= order.getPhoneNumber()%>" readonly>
        </div>
        <div class="form-group">
            <label>Time</label>
            <input type="text" class="form-control" name="time" value="<%= formattedDateTime%>" readonly>
        </div>
        <div class="form-group">
            <label>Status</label>
            <input type="text" class="form-control" name="status" value="<%= isdelivered%>" readonly>
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
                <% ArrayList<ProductInOrder> products = order.getProducts();
                double total = 0;
                    for (ProductInOrder product : products) {
                        Product p = product.getProduct();
                        total += p.getPrice() * product.getQuantity();
                        %>
                    <tr>
                        <td><%= p.getProductId()%></td>
                        <td><%= p.getProductName()%></td>
                        <td><%= product.getQuantity()%></td>
                        <td>$<%= String.format("%.2f",p.getPrice() * product.getQuantity())%></td>
                    </tr>
                    <%
                    }
                %>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td><b>$<%= String.format("%.2f",total) %></b></td>
                </tr>
                </tbody>
            </table>
        </div>

    </div>
    <div class="modal-footer">
        <input type="button" class="btn btn-default" data-dismiss="modal" value="Cancel">
    </div>
</form>