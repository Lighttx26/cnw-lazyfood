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
import lazyfood.demo.models.DTO.ProductDTO;
import lazyfood.demo.utils.BinaryTypeHandler;

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
        String sess_user_role = (String) req.getSession().getAttribute("role");

        String param_productId = req.getParameter("id");
        switch (action) {
            // api
            case "/api/Product/getAllProduct":
                getAllProduct(req, resp);
                break;
            case "/api/Product/getProductById":
                getProductById(req, resp);
                break;
            case "/Admin/Product":
                if (sess_user_role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!sess_user_role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else
                    ShowAllProducts(req, resp);
                break;
            case "/Admin/Product/view":
                if (sess_user_role == null)
                    UnauthorizedErrorPage(req, resp);
                else if (!sess_user_role.equals("admin"))
                    UnauthorizedErrorPage(req, resp);
                else if (param_productId != null)
                    ShowDetailsProduct(req, resp, param_productId);
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
        List<ProductDTO> productDTOList = null;
        resp.setContentType("application/json");
        try {
            productDTOList = productBO.getAllProducts();
            PrintWriter out = resp.getWriter();

            JsonArray jsonArray = new JsonArray();

            // Add each element to the JSON array with additional attributes
            for (ProductDTO productDTO : productDTOList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("Image", productDTO.Image);
                jsonObject.addProperty("Price", productDTO.Price);
                jsonObject.addProperty("ProductId", productDTO.ProductId);
                jsonObject.addProperty("ProductName", productDTO.ProductName);

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
        String param_productId = req.getParameter("id");
        ProductDTO productDTO = null;
        try {
            productDTO = productBO.getProductById(param_productId);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(new Gson().toJson(productDTO));
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

        List<ProductDTO> productDTOList = null;
        try {
            String param_keyword = req.getParameter("keyword");
            String param_searchClass = req.getParameter("class");

            if (param_keyword == null && param_searchClass == null)
                productDTOList = productBO.getAllProducts();
//            else {
//                if (searchClass == null || searchClass.isEmpty())
//                    searchClass = "ProductName";
//                products = productBO.filterProduct(keyword, searchClass);
//            }
        } catch (Exception e) {
            InternalServerErrorPage(req, resp);
            return;
        }

        req.setAttribute("products", productDTOList);
        try {
            String sess_user_role = (String) req.getSession().getAttribute("role");
            if (sess_user_role == null)
                req.getRequestDispatcher("/Customer/Product/testindex.jsp").forward(req, resp); // TODO: Replace path
            else if (sess_user_role.equals("customer"))
                req.getRequestDispatcher("/Customer/Product/testindex.jsp").forward(req, resp); // TODO: Replace path
            else if (sess_user_role.equals("admin"))
                req.getRequestDispatcher("/Admin/pages/ManageProduct.jsp").forward(req, resp);
        } catch (Exception e) {
            NotFoundErrorPage(req, resp);
        }
    }

    private void ShowDetailsProduct(HttpServletRequest req, HttpServletResponse resp, String id) {
        ProductDTO productDTO = null;
        try {
            productDTO = productBO.getProductById(id);
        } catch (Exception e) {
            InternalServerErrorPage(req, resp);
        }

        if (productDTO == null) {
            NotFoundErrorPage(req, resp);
        } else {
            try {
                req.setAttribute("products", productDTO);
                String sess_user_role = (String) req.getSession().getAttribute("role");
                if (sess_user_role == null)
                    req.getRequestDispatcher("Customer/Product/details.jsp").forward(req, resp); // TODO: Replace path
                else if (sess_user_role.equals("customer"))
                    req.getRequestDispatcher("/Customer/Product/details.jsp").forward(req, resp); // TODO: Replace path
                else if (sess_user_role.equals("admin"))
                    req.getRequestDispatcher("/Admin/Product/details.jsp").forward(req, resp); // TODO: Replace path
            } catch (Exception e) {
                NotFoundErrorPage(req, resp);
            }

        }
    }

    private void CreateItem(HttpServletRequest req, HttpServletResponse resp) {
        String sess_user_role = (String) req.getSession().getAttribute("role");

        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {
            String newProd_id = req.getParameter("ProductId");
            String newProd_name = req.getParameter("ProductName");
            String newProd_cid = req.getParameter("CategoryId");
            double newProd_price = Double.parseDouble(req.getParameter("Price"));

            InputStream file = null;
            try {
                file = req.getPart("Image").getInputStream();
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }

            ProductDTO productDTO = new ProductDTO();
            productDTO.ProductId = newProd_id;
            productDTO.ProductName = newProd_name;
            productDTO.CategoryId = newProd_cid;
            productDTO.Price = newProd_price;
            productDTO.Image = BinaryTypeHandler.InputStreamToBase64(file);

            try {
                productBO.addProduct(productDTO);
            } catch (Exception e) {
                req.setAttribute("product", productDTO);
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

        String sess_user_role = (String) req.getSession().getAttribute("role");
        String param_productId = req.getParameter("ProductId");
        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        } else if (sess_user_role.equals("admin")) {
            ProductDTO productDTO = null;
            try {
                productDTO = productBO.getProductById(param_productId);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (productDTO == null) {
                NotFoundErrorPage(req, resp);
            }

            else {
                req.setAttribute("product", productDTO);
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
        String sess_user_role = (String) req.getSession().getAttribute("role");

        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {
            String param_id = req.getParameter("ProductIdE");
            String param_name = req.getParameter("ProductNameE");
            String param_cid = req.getParameter("CategoryIdE");
            double param_price = Double.parseDouble(req.getParameter("PriceE"));

            InputStream file = null;
            try {
                file = req.getPart("ImageE").getInputStream();
            } catch (IOException | ServletException e) {
                e.printStackTrace();
            }

            ProductDTO productDTO = null;
            try {
                productDTO = productBO.getProductById(param_id);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (productDTO != null) {
                productDTO.ProductName = param_name;
                productDTO.CategoryId = param_cid;
                productDTO.Price = param_price;

                try {
                    if (req.getPart("ImageE").getSize() > 0)
                        productDTO.Image = BinaryTypeHandler.InputStreamToBase64(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    productBO.updateProduct(productDTO);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("product", productDTO);
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
        String sess_user_role = (String) req.getSession().getAttribute("role");
        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {
            try {
                String param_productId = req.getParameter("ProductIdD");
                productBO.deleteProduct(param_productId);
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
