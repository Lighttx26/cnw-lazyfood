package lazyfood.demo.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lazyfood.demo.models.BO.ProductBO;
import lazyfood.demo.models.Bean.Product;
import lazyfood.demo.utils.general;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 100 // 100 MB
)
@WebServlet(urlPatterns = {
        "/Admin/Product",
        "/Admin/Product/view",
        "/Admin/Product/create",
        "/Admin/Product/update",
        "/Admin/Product/delete",
        "/api/Product/getAllProductIds"
})
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductBO productBO;

    @Override
    public void init() throws ServletException {
        productBO = new ProductBO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getServletPath();
        String role = (String) req.getSession().getAttribute("role");

        String id = req.getParameter("id");
        switch (action) {
            // api
            case "/api/Product/getAllProductIds":
                getAllProductIds(req, resp);
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    private void getAllProductIds(HttpServletRequest req, HttpServletResponse resp) {
        ArrayList<Product> products = null;
        try {
            products = productBO.getAllProducts();
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(products));
            out.flush();
        } catch (SQLException | IOException e) {
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
        ArrayList<Product> products = null;
        try {
            products = productBO.getAllProducts();
        } catch (SQLException e) {
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
            Double price = Double.parseDouble(req.getParameter("Price"));

            InputStream file = null;
            try {
                file = req.getPart("Image").getInputStream();
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }

            Product product = new Product(id, name, cid, price, general.fileToBlob(file));

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
            Double price = Double.parseDouble(req.getParameter("PriceE"));

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
                product.setCategoryId(cid);
                product.setPrice(price);

                try {
                    if (req.getPart("ImageE").getSize() > 0)
                        product.setImage(general.fileToBlob(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productBO.updateProduct(product);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("product", product);
                    try {
                        // TODO: Replace path
                        req.getRequestDispatcher("/Admin/pages/ManageProduct.jsp").forward(req, resp);
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
                req.getRequestDispatcher("/Admin/Product/testindex.jsp"); // TODO: Replace path
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
