<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.ArrayList"%>
<%@ page language="java" import="lazyfood.demo.models.Bean.Order"%>
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
			</tr>
		</thead>
		<tbody>
			<%
			ArrayList<Order> orders = (ArrayList<Order>) request.getAttribute("orders");
			for (int i = 0; i < orders.size(); i++) {
			%>
			<tr>
				<td><%=orders.get(i).getOrderId()%></td>
				<td><%=orders.get(i).getCustomerId()%></td>
				<td><%=orders.get(i).getCustomerName()%></td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>