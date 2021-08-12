package ru.job4j.todo.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbmStore;
import ru.job4j.todo.utils.ItemAdapterJson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ToDoServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Item.class, new ItemAdapterJson())
            .create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        Collection<Item> items = HbmStore.instOf().getAllItems();
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(items);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json; charset=utf-8");
        Item reqItem = GSON.fromJson(req.getReader(), Item.class);
        Item item;
        if (reqItem.getId() == 0) {
            item = HbmStore.instOf().saveItem(reqItem);
        } else {
            item = HbmStore.instOf().updateItem(reqItem);
        }
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(item);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }
}
