package lazyfood.demo.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lazyfood.demo.models.BO.OrderBO;
import lazyfood.demo.models.BO.UserBO;
import lazyfood.demo.models.DTO.OrderDTO;
import lazyfood.demo.models.DTO.OrderDetailsDTO;
import lazyfood.demo.models.DTO.ProductInOrderDTO;
import lazyfood.demo.models.DTO.UserDTO;
import lazyfood.demo.utils.IdGenerator;

@WebServlet(urlPatterns = {
        "/Order",
        "/Order/view",
        "/Order/create",
        "/Order/delete",
        "/Admin/Order",
        "/Admin/Order/view",
        "/Admin/Order/update",
})
public class OrderServlet extends HttpServlet {

    private OrderBO orderBO;
    @Override
    public void init() {
        orderBO = new OrderBO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        String sess_user_role = (String) req.getSession().getAttribute("role");
        String param_id = req.getParameter("OrderId");

        switch (action) {
            case "/Order":
                ShowAllMyOrders(req, resp);
                break;
            case "/Admin/Order":
                if (sess_user_role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!sess_user_role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else
                    ShowAllOrders(req, resp);
                break;
            case "/Order/view":
                if (param_id != null)
                    ShowDetailsOrder(req, resp, param_id);
                else
                    ShowAllMyOrders(req, resp);
                break;
            case "/Order/create":
                if (sess_user_role == null) {
                    UnauthorizedErrorPage(req, resp);
                } else {
                    ShowOrderComponent(req, resp);
                }
                break;

            case "/Admin/Order/view":
                if (sess_user_role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!sess_user_role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else if (param_id != null)
                    ShowDetailsOrder(req, resp, param_id);
                else
                    ShowAllOrders(req, resp);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String action = req.getServletPath();
        String sess_user_role = (String) req.getSession().getAttribute("role");

        switch (action) {
            case "/Order/create":
                if (sess_user_role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!sess_user_role.equals("customer"))
                    UnauthorizedErrorPage(req, resp);
                else
                    CreateItem(req, resp);
                break;

            case "/Admin/Order/update":
                if (sess_user_role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!sess_user_role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else
                    UpdateItem(req, resp);
                break;

            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    private void ShowAllOrders(HttpServletRequest req, HttpServletResponse resp) {
        List<OrderDTO> orderDTOList = null;

        try {
            orderDTOList = orderBO.getAllOrders();
        } catch (SQLException e) {
            InternalServerErrorPage(req, resp);
            return;
        }

        req.setAttribute("orders", orderDTOList);

        try {
            req.getRequestDispatcher("/Admin/pages/ManageOrder.jsp").forward(req, resp);
        } catch (Exception e) {
            NotFoundErrorPage(req, resp);
        }
    }

    private void ShowAllMyOrders(HttpServletRequest req, HttpServletResponse resp) {
        List<OrderDTO> orderDTOList = null;
        String sess_user_role = (String) req.getSession().getAttribute("role");

        try {
            if (sess_user_role != null && (sess_user_role.equals("customer") || sess_user_role.equals("admin"))) {
                String user_id = (String) req.getSession().getAttribute("userid");
                orderDTOList = orderBO.getOrdersByUser(user_id);
            }
        } catch (SQLException e) {
            InternalServerErrorPage(req, resp);
            return;
        }

        req.setAttribute("orders", orderDTOList);

        try {
            req.getRequestDispatcher("/Customer/Order/Orders.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            NotFoundErrorPage(req, resp);
        }
    }

    private void ShowDetailsOrder(HttpServletRequest req, HttpServletResponse resp, String id) {
        String sess_user_role = (String) req.getSession().getAttribute("role");

        if (sess_user_role == null) {
            NotFoundErrorPage(req, resp);
            return;
        }

        else if (!(sess_user_role.equals("admin") || sess_user_role.equals("customer"))) {
            NotFoundErrorPage(req, resp);
            return;
        }

        OrderDetailsDTO orderDetailsDTO = orderBO.getOrderDetails(id);

        if (sess_user_role.equals("admin")) {
            req.setAttribute("order", orderDetailsDTO);
            try {
                req.getRequestDispatcher("/Admin/pages/OrderDetails.jsp").forward(req, resp);
            } catch (Exception e) {
                e.printStackTrace();
                NotFoundErrorPage(req, resp);
            }
        }

        else {
            String sess_user_id = (String) req.getSession().getAttribute("userid");
            if (orderDetailsDTO._Order.CustomerId.equals(sess_user_id)) {
                req.setAttribute("order", orderDetailsDTO);
                try {
                    req.getRequestDispatcher("/Customer/Order/OrderDetails.jsp").forward(req, resp);
                } catch (Exception e) {
                    NotFoundErrorPage(req, resp);
                }
            }

            else {
                NotFoundErrorPage(req, resp);
            }
        }
    }

    private void CreateItem(HttpServletRequest request, HttpServletResponse response) throws IOException {

        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        JsonObject orderInformation = gson.fromJson(reader, JsonObject.class);

        // get order's basic information
        String sess_customer_id = request.getSession().getAttribute("userid").toString();
        String newOrder_phone = orderInformation.get("phone").getAsString();
        String newOrder_addr = orderInformation.get("addr").getAsString();
        LocalDateTime newOrder_time = LocalDateTime.now();
        String newOrder_orderId = "ord" + IdGenerator.generateId("ord", newOrder_time.toString());

        // get products in order
        List<ProductInOrderDTO> productsInOrder = new ArrayList<>();
        JsonArray cart = orderInformation.getAsJsonArray("cart");
        for (JsonElement item : cart) {
            JsonObject obj = item.getAsJsonObject();
            JsonPrimitive id = obj.getAsJsonPrimitive("ProductId");
            JsonPrimitive quantity = obj.getAsJsonPrimitive("Quantity");

            productsInOrder.add(new ProductInOrderDTO() {{
                ProductId = id.getAsString();
                Quantity = quantity.getAsInt();
            }});
        }

        OrderDTO orderDTO = new OrderDTO() {{
            OrderId = newOrder_orderId;
            CustomerId = sess_customer_id;
            Address = newOrder_addr;
            PhoneNumber = newOrder_phone;
            OrderDatetime = newOrder_time;
            IsDelivered = false;
        }};

        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO() {{
            _Order = orderDTO;
            Products = productsInOrder;
        }};

        try {
            orderBO.createOrder(orderDetailsDTO);
        } catch (Exception e) {
            e.printStackTrace();
            InternalServerErrorPage(request, response);
        }

        response.sendRedirect(request.getContextPath() + "/Order");
    }

    private void ShowOrderComponent(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String sess_user_id = (String) req.getSession().getAttribute("userid");
            UserDTO userDTO = (new UserBO()).getUserById(sess_user_id);

            req.setAttribute("addr", userDTO.Address);
            req.setAttribute("phone", userDTO.PhoneNumber);

            req.getRequestDispatcher("/Customer/Order/OrderComponent.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            NotFoundErrorPage(req, resp);
        }
    }

    public void UpdateItem(HttpServletRequest request, HttpServletResponse response) {

        String param_orderId = request.getParameter("OrderId");

        try {
            orderBO.setOrderDelivered(param_orderId, true);
        } catch (Exception e) {
            e.printStackTrace();
            InternalServerErrorPage(request, response);
        }

        try {
            response.sendRedirect(request.getContextPath() + "/Admin/?page=order");
        } catch (Exception e) {
            NotFoundErrorPage(request, response);
        }
    }

    private void ShowErrorPage(HttpServletRequest req, HttpServletResponse resp, String errorCode) {
        try {
            req.getRequestDispatcher("/Error/Error" + errorCode + ".jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void NotFoundErrorPage(HttpServletRequest req, HttpServletResponse resp) {
        ShowErrorPage(req, resp, "404");
    }

    private void UnauthorizedErrorPage(HttpServletRequest req, HttpServletResponse resp) {
        ShowErrorPage(req, resp, "401");
    }

    private void InternalServerErrorPage(HttpServletRequest req, HttpServletResponse resp) {
        ShowErrorPage(req, resp, "500");
    }
}
