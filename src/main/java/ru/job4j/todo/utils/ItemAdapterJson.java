package ru.job4j.todo.utils;

import com.google.gson.*;
import ru.job4j.todo.model.Item;

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
        return result;
    }

    @Override
    public Item deserialize(JsonElement jsonElement, Type type,
                            JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String description = object.get("description").getAsString();
        JsonElement idElement = object.get("id");
        if (idElement == null) {
            return new Item(description);
        }
        int id = idElement.getAsInt();
        Timestamp created = Timestamp.valueOf(
                LocalDateTime.from(DATE_FORMAT.get().parse(object.get("created").getAsString()))
        );
        boolean done = object.get("done").getAsBoolean();
        return new Item(id, description, created, done);
    }
}
