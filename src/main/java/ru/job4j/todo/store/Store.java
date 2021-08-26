package ru.job4j.todo.store;

import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Collection;

public interface Store {
    Collection<Item> getAllItems();

    Item saveItem(Item item);

    Item updateItem(Item item);

    Item findItemById(int id);

    User findUserByEmail(String email);

    User saveUser(User user);

    Collection<Category> getAllCategories();

    Category findCategoryById(int id);
}
