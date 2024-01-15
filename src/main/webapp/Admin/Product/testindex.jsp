<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.ArrayList"%>
<%@ page language="java" import="lazyfood.demo.models.Entity.Product"%>
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
				<th>Name</th>
				<th>CategoryId</th>
				<th>CategoryName</th>
				<th>Price</th>
			</tr>
		</thead>
		<tbody>
			<%
			ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products");
			for (int i = 0; i < products.size(); i++) {
			%>
			<tr>
				<td><%=products.get(i).getProductId()%></td>
				<td><%=products.get(i).getProductName()%></td>
				<td><%=products.get(i).getCategory().getCategoryId()%></td>
				<td><%=products.get(i).getCategory().getCategoryName()%></td>
				<td><%=products.get(i).getPrice()%></td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>