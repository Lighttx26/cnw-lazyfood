<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.ArrayList"%>
<%@ page language="java" import="lazyfood.demo.models.Entity.Order"%>
<%@ page language="java" import="lazyfood.demo.models.Entity.ProductInOrder"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<body>
	<h1>Admin</h1>
	<table border="solid" width="80%">
		<thead>
			<tr>
				<th>Id</th>
				<th>CustomerId</th>
				<th>CustomerName</th>
                <th>Time</th>
                <th>Address</th>
                <th>Phone</th>
                <th>Status</th>
			</tr>
		</thead>
		<tbody>
			<%
			Order order = (Order) request.getAttribute("order");
			%>
			<tr>
				<td><%=order.getOrderId()%></td>
				<td><%=order.getCustomerId()%></td>
				<td><%=order.getCustomerName()%></td>
                <td><%=order.getTime()%></td>
                <td><%=order.getAddress()%></td>
                <td><%=order.getPhoneNumber()%></td>
                <td><%=order.isDelivered()%></td>
			</tr>
		</tbody>
	</table>
    <table border="solid" width="80%">
		<thead>
			<tr>
				<th>ProductId</th>
				<th>ProductName</th>
				<th>Quantity</th>
			</tr>
		</thead>
		<tbody>
			<%
            ArrayList<ProductInOrder> products = order.getProducts();
			for (int i = 0; i < products.size(); i++) {
			%>
			<tr>
				<td><%=products.get(i).getProduct().getProductId()%></td>
				<td><%=products.get(i).getProduct().getProductName()%></td>
				<td><%=products.get(i).getQuantity()%></td>
			</tr>
            <%
			}
			%>
		</tbody>
	</table>
</body>
</html>