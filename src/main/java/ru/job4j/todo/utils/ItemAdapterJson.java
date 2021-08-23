package ru.job4j.todo.utils;

import com.google.gson.*;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.store.HbmStore;
import ru.job4j.todo.store.Store;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ItemAdapterJson implements JsonSerializer<Item>, JsonDeserializer<Item> {
    private static final ThreadLocal<DateTimeFormatter> DATE_FORMAT
            = ThreadLocal.withInitial(() -> DateTimeFormatter.ISO_DATE_TIME);

    @Override
    public JsonElement serialize(Item item, Type type,
                                 JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        result.add("id", new JsonPrimitive(item.getId()));
        result.add("description", new JsonPrimitive(item.getDescription()));
        result.add("created",
                new JsonPrimitive(DATE_FORMAT.get().format(item.getCreated().toLocalDateTime())));
        result.add("done", new JsonPrimitive(item.isDone()));
        result.add("user", new JsonPrimitive(item.getUser().getName()));
        JsonArray categories = new JsonArray();
        item.getCategories()
                .stream()
                .map(Category::getId)
                .forEach(id -> categories.add(new JsonPrimitive(id)));
        result.add("categories", categories);
        return result;
    }

    @Override
    public Item deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String description = object.get("description").getAsString();
        JsonElement idElement = object.get("id");
        Item item;
        if (idElement == null) {
            item = new Item(description);
        } else {
            int id = idElement.getAsInt();
            Timestamp created = Timestamp.valueOf(
                    LocalDateTime.from(DATE_FORMAT.get().parse(object.get("created").getAsString()))
            );
            boolean done = object.get("done").getAsBoolean();
            item = new Item(id, description, created, done);
        }
        JsonArray categories = object.get("categories").getAsJsonArray();
        Store store = HbmStore.instOf();
        categories.forEach(categoryId ->
                item.addCategory(store.findCategoryById(categoryId.getAsInt())));
        return item;
    }
}
