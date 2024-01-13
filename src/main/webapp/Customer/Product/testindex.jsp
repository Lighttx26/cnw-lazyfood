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
	<h1>Customer</h1>
	<table border="solid" width="80%">
		<thead>
			<tr>
				<th>#</th>
				<th>Name</th>
				<th>Category</th>
				<th>Price</th>
			</tr>
		</thead>
		<tbody>
			<%
			ArrayList<Product> products = (ArrayList<Product>) request.getAttribute("products");
			for (int i = 0; i < products.size(); i++) {
			%>
			<tr>
				<td><%=(i+1)%></td>
				<td><%=products.get(i).getProductName()%></td>
				<td><%=products.get(i).getCategoryName()%></td>
				<td><%=products.get(i).getPrice()%></td>
				<td><img src="data:image/jpeg;base64,<%=products.get(i).getImage()%>"></td>
			</tr>
			<%
			}
			%>
		</tbody>
	</table>
</body>
</html>