package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;
import ru.job4j.todo.store.Store;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class RegServlet extends HttpServlet {
    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("reg.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        User user = GSON.fromJson(req.getReader(), User.class);
        Store store = HbmStore.instOf();
        if (store.findUserByEmail(user.getEmail()) != null) {
            resp.setContentType("application/json; charset=utf-8");
            String json = "{ \"error\": \"This email is already registered\" }";
            OutputStream output = resp.getOutputStream();
            output.write(json.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
        } else {
            store.saveUser(user);
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
