package org.example;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.junit.jupiter.api.Assertions;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

class UserDAOTest {
    static UserDAO userDAO;
    @BeforeAll
    static void initialise()
    {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        userDAO = context.getBean("userDAO", UserDAO.class);
        try {
            userDAO.insert(new User("Nick", "Goodman", "Ger", "Ger", Role.Customer));
            userDAO.insert(new User("Nick", "Goodman", "V", "V", Role.Customer));
            userDAO.insert(new User("Lack", "Vanil", "T", "T", Role.Customer));
        }
        catch (DuplicateFieldException ignored){}
    }
    @Test
    void selectAll() {
        Object[] daoMas = userDAO.selectAll().toArray();
        Object[] mas = new User[]{
                new User("Nick", "Goodman", "Ger", "Ger", Role.Customer),
                new User("Nick", "Goodman", "V", "V", Role.Customer),
                new User("Lack", "Vanil", "T", "T", Role.Customer)
        };
        Assertions.assertArrayEquals(mas, daoMas);
    }

    @Test
    void selectID() {
        List<User> list = userDAO.selectAll();
        Object daoUser = userDAO.select(list.get(0).getId());
        Object user = list.get(0);
        Assertions.assertEquals(user,daoUser);
    }
    @Test
    void selectWrongID() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> userDAO.select(0));
    }
    @Test
    void selectUsername() {
        List<User> list = userDAO.selectAll();
        Object daoUser = userDAO.select(list.get(0).getUsername());
        Object user = list.get(0);
        Assertions.assertEquals(user,daoUser);
    }
    @Test
    void selectWrongUsername() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> userDAO.select("krrr"));
    }

    @Test
    void update() {
        List<User> list = userDAO.selectAll();
        User user = new User("Nick", "Goodman", "Cool", "Ger", Role.Customer);
        try { userDAO.update(user,list.get(0).getId());}
        catch (DuplicateFieldException ignored){}
        Assertions.assertEquals(user, userDAO.select(list.get(0).getId()));
    }
    @Test
    void updateDuplicateUsername() {
        List<User> list = userDAO.selectAll();
        User user = new User("Lack", "Vanil", "V", "T", Role.Customer);
        Assertions.assertThrows(DuplicateFieldException.class, () -> userDAO.update(user, list.get(2).getId()));
    }

    @Test
    void insert() {
        User user = new User("Nill", "Kiggers", "Nig...", "Nig", Role.Customer);
        try { userDAO.insert(user);}
        catch (DuplicateFieldException ignored){}
        List<User> list = userDAO.selectAll();
        Assertions.assertEquals(user, userDAO.select(list.get(list.size() - 1).getId()));
    }
    @Test
    void insertDuplicateUsername() {
        User user = new User("Nill", "Kiggers", "V", "Nig", Role.Customer);
        Assertions.assertThrows(DuplicateFieldException.class, () -> userDAO.insert(user));
    }

    @Test
    void delete() {
        List<User> list = userDAO.selectAll();
        userDAO.delete(list.get(2).getId());
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> userDAO.select(list.get(2).getId()));
    }
    @AfterAll
    static void clear() {
        userDAO.clearTable();
        Object[] mas = userDAO.selectAll().toArray();
        Object[] emptyMas = new User[]{};
        Assertions.assertArrayEquals(emptyMas, mas);
    }
}