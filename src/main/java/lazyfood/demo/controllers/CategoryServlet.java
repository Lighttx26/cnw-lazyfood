package lazyfood.demo.controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lazyfood.demo.models.BO.CategoryBO;
import lazyfood.demo.models.Entity.Category;

@WebServlet(urlPatterns = {
        "/Category",
        "/Category/create",
        "/Category/update",
        "/Category/delete",
})
public class CategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryBO categoryBO;

    @Override
    public void init() {
        categoryBO = new CategoryBO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        String id = req.getParameter("id");
        switch (action) {
            case "/Category":
                ShowAllCategories(req, resp);
                break;
            case "/Category/create":
                ShowCreateForm(req, resp);
                break;
            case "/Category/update":
                ShowUpdateForm(req, resp, id);
                break;
            case "/Category/delete":
                DeleteItem(req, resp, id);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        String id = req.getParameter("id");
        switch (action) {
            case "/Category/create":
                CreateItem(req, resp);
                break;
            case "/Category/update":
                UpdateItem(req, resp, id);
                break;
            case "/Category/delete":
                DeleteItem(req, resp, id);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    private void ShowAllCategories(HttpServletRequest req, HttpServletResponse resp) {
        ArrayList<Category> categories = null;
        try {
            categories = categoryBO.getAllCaterories();
        } catch (Exception e) {
            InternalServerErrorPage(req, resp);
            return;
        }

        req.setAttribute("categories", categories);
        try {
            String role = (String) req.getSession().getAttribute("role");
            if (role == null)
                req.getRequestDispatcher("/Customer/Category/index.jsp").forward(req, resp);
            else if (role.equals("customer"))
                req.getRequestDispatcher("/Customer/Category/index.jsp").forward(req, resp);
            else if (role.equals("admin"))
                req.getRequestDispatcher("/Admin/Category/index.jsp").forward(req, resp);
        } catch (Exception e) {
            NotFoundErrorPage(req, resp);
        }
    }

    private void ShowCreateForm(HttpServletRequest req, HttpServletResponse resp) {
        String role = (String) req.getSession().getAttribute("role");
        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        } else if (role.equals("admin")) {
            try {
                req.getRequestDispatcher("/Admin/Category/create.jsp").forward(req, resp);
            } catch (Exception e) {
                NotFoundErrorPage(req, resp);
            }
        } else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void CreateItem(HttpServletRequest req, HttpServletResponse resp) {
        String role = (String) req.getSession().getAttribute("role");

        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {
            String id = req.getParameter("CategoryId");
            String name = req.getParameter("CategoryName");

            Category category = new Category(id, name);
            try {
                categoryBO.addCategory(category);
            } catch (Exception e) {
                req.setAttribute("category", category);
                req.setAttribute("error", e.getMessage());
                try {
                    req.getRequestDispatcher("/Admin/Category/create.jsp").forward(req, resp);
                } catch (Exception e1) {
                    NotFoundErrorPage(req, resp);
                }
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void ShowUpdateForm(HttpServletRequest req, HttpServletResponse resp, String id) {
        String role = (String) req.getSession().getAttribute("role");
        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {

            Category category = null;
            try {
                category = categoryBO.getCategoryById(id);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (category == null) {
                NotFoundErrorPage(req, resp);
            }

            else {
                req.setAttribute("category", category);
                try {
                    req.getRequestDispatcher("/Admin/Category/update.jsp").forward(req, resp);
                } catch (Exception e) {
                    NotFoundErrorPage(req, resp);
                }
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void UpdateItem(HttpServletRequest req, HttpServletResponse resp, String id) {
        String role = (String) req.getSession().getAttribute("role");

        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {
            String name = req.getParameter("CategoryName");

            Category category = null;
            try {
                category = categoryBO.getCategoryById(id);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (category != null) {
                category.setCategoryName(name);
                try {
                    categoryBO.updateCategory(category);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("category", category);
                    try {
                        req.getRequestDispatcher("/Admin/Category/update.jsp").forward(req, resp);
                    } catch (Exception e1) {
                        NotFoundErrorPage(req, resp);
                    }
                }
            }

            else {
                NotFoundErrorPage(req, resp);
            }

        }

        else {
            UnauthorizedErrorPage(req, resp);
        }
    }

    private void DeleteItem(HttpServletRequest req, HttpServletResponse resp, String id) {
        String role = (String) req.getSession().getAttribute("role");
        if (role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (role.equals("admin")) {
            try {
                categoryBO.deleteCategory(id);
            } catch (SQLException e) {
                req.setAttribute("error", e.getMessage());
                req.getRequestDispatcher("/Admin/Category/index.jsp");
            }
        }

        else {
            UnauthorizedErrorPage(req, resp);
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
