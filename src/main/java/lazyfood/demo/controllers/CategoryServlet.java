package lazyfood.demo.controllers;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lazyfood.demo.models.BO.CategoryBO;
import lazyfood.demo.models.DTO.CategoryDTO;

@WebServlet(urlPatterns = {
        "/Category",
        "/Category/create",
        "/Category/update",
        "/Category/delete",
})
public class CategoryServlet extends HttpServlet {

    private CategoryBO categoryBO;

    @Override
    public void init() {
        categoryBO = new CategoryBO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        String param_categoryId = req.getParameter("id");
        switch (action) {
            case "/Category":
                ShowAllCategories(req, resp);
                break;
            case "/Category/create":
                ShowCreateForm(req, resp);
                break;
            case "/Category/update":
                ShowUpdateForm(req, resp, param_categoryId);
                break;
            case "/Category/delete":
                DeleteItem(req, resp, param_categoryId);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String action = req.getServletPath();
        String param_categoryId = req.getParameter("id");
        switch (action) {
            case "/Category/create":
                CreateItem(req, resp);
                break;
            case "/Category/update":
                UpdateItem(req, resp, param_categoryId);
                break;
            case "/Category/delete":
                DeleteItem(req, resp, param_categoryId);
                break;
            default:
                NotFoundErrorPage(req, resp);
                break;
        }
    }

    private void ShowAllCategories(HttpServletRequest req, HttpServletResponse resp) {
        List<CategoryDTO> categoryDTOList = null;
        try {
            categoryDTOList = categoryBO.getAllCategories();
        } catch (Exception e) {
            InternalServerErrorPage(req, resp);
            return;
        }

        req.setAttribute("categories", categoryDTOList);
        try {
            String sess_user_role = (String) req.getSession().getAttribute("role");
            if (sess_user_role == null)
                req.getRequestDispatcher("/Customer/Category/index.jsp").forward(req, resp);
            else if (sess_user_role.equals("customer"))
                req.getRequestDispatcher("/Customer/Category/index.jsp").forward(req, resp);
            else if (sess_user_role.equals("admin"))
                req.getRequestDispatcher("/Admin/Category/index.jsp").forward(req, resp);
        } catch (Exception e) {
            NotFoundErrorPage(req, resp);
        }
    }

    private void ShowCreateForm(HttpServletRequest req, HttpServletResponse resp) {
        String sess_user_role = (String) req.getSession().getAttribute("role");
        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        } else if (sess_user_role.equals("admin")) {
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
        String sess_user_role = (String) req.getSession().getAttribute("role");

        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {
            String newCategory_id = req.getParameter("CategoryId");
            String newCategory_name = req.getParameter("CategoryName");

            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.CategoryId = newCategory_id;
            categoryDTO.CategoryName = newCategory_name;
            try {
                categoryBO.addCategory(categoryDTO);
            } catch (Exception e) {
                req.setAttribute("category", categoryDTO);
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
        String sess_user_role = (String) req.getSession().getAttribute("role");
        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {

            CategoryDTO categoryDTO = null;
            try {
                categoryDTO = categoryBO.getCategoryById(id);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (categoryDTO == null) {
                NotFoundErrorPage(req, resp);
            }

            else {
                req.setAttribute("category", categoryDTO);
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
        String sess_user_role = (String) req.getSession().getAttribute("role");

        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {
            String name = req.getParameter("CategoryName");

            CategoryDTO categoryDTO = null;
            try {
                categoryDTO = categoryBO.getCategoryById(id);
            } catch (Exception e) {
                InternalServerErrorPage(req, resp);
            }

            if (categoryDTO != null) {
                categoryDTO.CategoryName = name;
                try {
                    categoryBO.updateCategory(categoryDTO);
                } catch (Exception e) {
                    req.setAttribute("error", e.getMessage());
                    req.setAttribute("category", categoryDTO);
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
        String sess_user_role = (String) req.getSession().getAttribute("role");
        if (sess_user_role == null) {
            UnauthorizedErrorPage(req, resp);
        }

        else if (sess_user_role.equals("admin")) {
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
