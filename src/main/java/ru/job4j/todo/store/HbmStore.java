package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;


import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class HbmStore implements Store, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Collection<Item> getAllItems() {
        return tx(
                session -> session.createQuery(
                        "select distinct i FROM Item i JOIN FETCH i.categories order by i.created"
                ).list()
        );
    }

    @Override
    public Item saveItem(Item item) {
        return tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    @Override
    public Item updateItem(Item item) {
        return tx(
                session -> {
                    session.update(item);
                    return item;
                }
        );
    }

    @Override
    public User findUserByEmail(String email) {
        return tx(
                session -> {
                    String hql = "from User where email = :paramEmail";
                    Query query = session.createQuery(hql);
                    query.setParameter("paramEmail", email);
                    return (User) query.uniqueResult();
                }
        );
    }

    @Override
    public User saveUser(User user) {
        return tx(
                session -> {
                    session.save(user);
                    return user;
                }
        );
    }

    @Override
    public Collection<Category> getAllCategories() {
        return tx(
                session -> (List<Category>) session.createQuery("from Category").list()
        );
    }

    @Override
    public Category findCategoryById(int id) {
        return tx(
                session -> session.get(Category.class, id)
        );
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
