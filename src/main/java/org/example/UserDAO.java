package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import java.util.List;
@Component
public class UserDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public List<User> selectAll()
    {
        return jdbcTemplate.query("SELECT * FROM users;", new UserMapper());
    }
    public User select(int id) throws EmptyResultDataAccessException
    {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id = ?;", new Object[]{id},
                new UserMapper()).stream().findAny().orElseThrow(
                        () -> new EmptyResultDataAccessException(0));
    }
    public User select(String username) throws EmptyResultDataAccessException
    {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?;", new Object[]{username},
                new UserMapper()).stream().findAny().orElseThrow(
                () -> new EmptyResultDataAccessException(0));
    }
    public void update(User user, int id) throws DuplicateFieldException
    {
        try
        {
            select(user.getUsername());
            throw new DuplicateFieldException("Username already exists");
        }
        catch (EmptyResultDataAccessException ignored) {}
        jdbcTemplate.update("UPDATE users SET first_name = ?, last_name = ?, username = ?, " +
                        "pass = ?, user_role = ? WHERE user_id = ?;", user.getFirstName(), user.getLastName(),
                user.getUsername(), user.getPassword(), user.roleToString(), id);
    }
    public void insert(User user) throws DuplicateFieldException
    {
        try
        {
            select(user.getUsername());
            throw new DuplicateFieldException("Username already exists");
        }
        catch (EmptyResultDataAccessException ignored) {}
        jdbcTemplate.update("INSERT INTO users(first_name, last_name, username, pass, user_role) " +
                        "VALUES (?, ?, ?, ?, ?);",
                user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.roleToString());
    }

    public void delete(int id)
    {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }
    public void clearTable()
    {
        jdbcTemplate.update("DELETE FROM users WHERE user_id > 0");
    }

}
