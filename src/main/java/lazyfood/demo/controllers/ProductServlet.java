package lazyfood.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lazyfood.demo.models.BO.CategoryBO;
import lazyfood.demo.models.BO.ProductBO;
import lazyfood.demo.models.Entity.Product;
import lazyfood.demo.utils.general;
import org.hibernate.Hibernate;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
@WebServlet(urlPatterns = {
        "/Admin/Product",
        "/Admin/Product/view",
        "/Admin/Product/create",
        "/Admin/Product/update",
        "/Admin/Product/delete",
        "/api/Product/getAllProduct",
        "/api/Product/getProductById",
})
public class ProductServlet extends HttpServlet {

    private ProductBO productBO;
    private CategoryBO categoryBO;

    @Override
    public void init() {
        productBO = new ProductBO();
        categoryBO = new CategoryBO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        String role = (String) req.getSession().getAttribute("role");

        String id = req.getParameter("id");
        switch (action) {
            // api
            case "/api/Product/getAllProduct":
                getAllProduct(req, resp);
                break;
            case "/api/Product/getProductById":
                getProductById(req, resp);
                break;
            case "/Admin/Product":
                if (role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else
                    ShowAllProducts(req, resp);
                break;
            case "/Admin/Product/view":
                if (role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else if (id != null)
                    ShowDetailsProduct(req, resp, id);
                else
                    ShowAllProducts(req, resp);
                break;
            case "/Admin/Product/update":
                ShowUpdateForm(req, resp);
                break;

            case "/Admin/Product/delete":
                DeleteItem(req, resp);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        switch (action) {
            case "/Admin/Product/create":
                CreateItem(req, resp);
                break;
            case "/Admin/Product/update":
                UpdateItem(req, resp);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();

        switch (action) {
            case "/Admin/Product/delete":
                DeleteItem(req, resp);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    private void getAllProduct(HttpServletRequest req, HttpServletResponse resp) {
        List<Product> products = null;
        resp.setContentType("application/json");
        try {
            products = productBO.getAllProducts();
            PrintWriter out = resp.getWriter();

            JsonArray jsonArray = new JsonArray();

            // Add each element to the JSON array with additional attributes
            for (Product product : products) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Image", product.getBase64Image());
                jsonObject.addProperty("Price", product.getPrice());
                jsonObject.addProperty("ProductId", product.getProductId());
                jsonObject.addProperty("ProductName", product.getProductName());

                jsonArray.add(jsonObject);
            }

            out.print(new Gson().toJson(jsonArray));
            out.flush();
        } catch (IOException e) {
            try {
                PrintWriter out = resp.getWriter();
                out.print("{\"error\": \"500\"}");
                out.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void getProductById(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        Product product = null;
        try {
            product = productBO.getProductById(id);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(product));
            out.flush();
        } catch (IOException e) {
            resp.setContentType("application/json");
            try {
                PrintWriter out = resp.getWriter();
                out.print("{\"error\": \"500\"}");
                out.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void ShowAllProducts(HttpServletRequest req, HttpServletResponse resp) {

        List<Product> products = null;
        try {
            String keyword = req.getParameter("keyword");
            String searchClass = req.getParameter("class");

            if (keyword == null && searchClass == null)
                products = productBO.getAllProducts();
//            else {
//                if (searchClass == null || searchClass.isEmpty())
//                    searchClass = "ProductName";
//                products = productBO.filterProduct(keyword, searchClass);
//            }
        } catch (Exception e) {
            InternalServerErrorPage(req, resp);
            return;
        }

        req.setAttribute("products", products);
        try {
            String role = (String) req.getSession().getAttribute("role");
            if (role == null)
                req.getRequestDispatcher("/Customer/Product/testindex.jsp").forward(req, resp); // TODO: Replace path
            else if (role.equals("customer"))
                req.getRequestDispatcher("/Customer/Product/testindex.jsp").forward(req, resp); // TODO: Replace path
            else if (role.equals("admin"))
                req.getRequestDispatcher("/Admin/pages/ManageProduct.jsp").forward(req, resp);
        } catch (Exception e) {
            NotFoundErrorPage(req, resp);
        }
    }

    private void ShowDetailsProduct(HttpServletRequest req, HttpServletResponse resp, String id) {
        Product product = null;
        try {
            product = productBO.getProductById(id);
        } catch (Exception e) {
            InternalServerErrorPage(req, resp);
        }

        if (product == null) {
            NotFoundErrorPage(req, resp);
        } else {
            try {
                req.setAttribute("products", product);
                String role = (String) req.getSession().getAttribute("role");
                if (role == null)
                    req.getRequestDispatcher("Customer/Product/details.jsp").forward(req, resp); // TODO: Replace path
                else if (role.equals("customer"))
                    req.getRequestDispatcher("/Customer/Product/details.jsp").forward(req, resp); // TODO: Replace path
                else if (role.equals("admin"))
                    req.getRequestDispatcher("/Admin/Product/details.jsp").forward(req, resp); // TODO: Replace path
            } catch (Exception e) {
                NotFoundErrorPage(req, resp);
            }

        }
    }

    private void CreateItem(HttpServletRequest req, HttpServletResponse resp) {
        String role = (String) req.getSession().getAttribute("role");

        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {
            String id = req.getParameter("ProductId");
            String name = req.getParameter("ProductName");
            String cid = req.getParameter("CategoryId");
            double price = Double.parseDouble(req.getParameter("Price"));

            InputStream file = null;
            try {
                file = req.getPart("Image").getInputStream();
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }

            Product product = new Product();
            product.setProductId(id);
            product.setProductName(name);
            product.setCategory(categoryBO.getCategoryById(cid));
            product.setPrice(price);
            product.setImage(general.InputStreamToByteArray(file));

            try {
                productBO.addProduct(product);
            } catch (Exception e) {
                req.setAttribute("product", product);
                req.setAttribute("error", e.getMessage());
                try {
                    req.getRequestDispatcher("/Admin/Product/testcreate.jsp").forward(req, resp); // TODO: Replace path
                } catch (Exception e1) {
                    NotFoundErrorPage(req, resp);
                }
            }

            try {
                resp.sendRedirect("..?page=product");
            } catch (IOException e) {
                NotFoundErrorPage(req, resp);
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void ShowUpdateForm(HttpServletRequest req, HttpServletResponse resp) {

        String role = (String) req.getSession().getAttribute("role");
        String ProductId = req.getParameter("ProductId");
        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        } else if (role.equals("admin")) {
            Product product = null;
            try {
                product = productBO.getProductById(ProductId);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (product == null) {
                NotFoundErrorPage(req, resp);
            }

            else {
                req.setAttribute("product", product);
                try {
                    req.getRequestDispatcher("/Admin/Product/edit.jsp").forward(req, resp);
                } catch (Exception e) {
                    NotFoundErrorPage(req, resp);
                }
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void UpdateItem(HttpServletRequest req, HttpServletResponse resp) {
        String role = (String) req.getSession().getAttribute("role");

        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {
            String id = req.getParameter("ProductIdE");
            String name = req.getParameter("ProductNameE");
            String cid = req.getParameter("CategoryIdE");
            double price = Double.parseDouble(req.getParameter("PriceE"));

            InputStream file = null;
            try {
                file = req.getPart("ImageE").getInputStream();
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }

            Product product = null;
            try {
                product = productBO.getProductById(id);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (product != null) {
                product.setProductName(name);
                product.setCategory(categoryBO.getCategoryById(cid));
                product.setPrice(price);

                try {
                    if (req.getPart("ImageE").getSize() > 0)
                        product.setImage(general.InputStreamToByteArray(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productBO.updateProduct(product);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("product", product);
                    try {
                        req.getRequestDispatcher("/Admin/Product/edit.jsp").forward(req, resp);
                    } catch (Exception e1) {
                        NotFoundErrorPage(req, resp);
                    }
                }
            }

            else {
                NotFoundErrorPage(req, resp);
            }

            try {
                resp.sendRedirect("..?page=product");
            } catch (IOException e) {
                NotFoundErrorPage(req, resp);
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void DeleteItem(HttpServletRequest req, HttpServletResponse resp) {
        String role = (String) req.getSession().getAttribute("role");
        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {
            try {
                String id = req.getParameter("ProductIdD");
                productBO.deleteProduct(id);
            } catch (SQLException e) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/Admin/pages/ManageProduct.jsp");
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
        }

        try {
            resp.sendRedirect("..?page=product");
        } catch (IOException e) {
            NotFoundErrorPage(req, resp);
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
