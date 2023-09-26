package ru.practicum.shareit.user.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;


@Slf4j
@Primary
@Repository
@RequiredArgsConstructor
public class UserDbRepository implements UserRepository {
    private static final int MIN_ROW_NUMBER_EFFECTED_BY_QUERY = 1;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        long userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).longValue();
        user.setId(userId);
        return user;
    }

    @Override
    public User update(User user) throws UserWithEmailAlreadyExists {
        String sqlQuery = "UPDATE users SET\n";

        if (StringUtils.hasText(user.getName()) && StringUtils.hasText(user.getEmail())) {
            sqlQuery += "name = ?,\n" +
                    "email = ?\n" +
                    "WHERE user_id = ?\n";

            jdbcTemplate.update(sqlQuery,
                    user.getName(),
                    user.getEmail(),
                    user.getId()
            );
        } else if (StringUtils.hasText(user.getName()) && !StringUtils.hasText(user.getEmail())) {
            sqlQuery += "name = ?\n" +
                    "WHERE user_id = ?\n";

            jdbcTemplate.update(sqlQuery,
                    user.getName(),
                    user.getId()
            );
        } else if (!StringUtils.hasText(user.getName()) && StringUtils.hasText(user.getEmail())) {
            sqlQuery += "email = ?\n" +
                    "WHERE user_id = ?\n";

            try {
                jdbcTemplate.update(sqlQuery,
                        user.getEmail(),
                        user.getId()
                );
            } catch (DataAccessException e) {
                String message = String.format("the user with an email {%s} already exists", user.getEmail());
                throw new UserWithEmailAlreadyExists(message);
            }
        }

        return findById(user.getId());
    }

    @Override
    public User findById(long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public boolean contains(long id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?) AS is_user";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery,
                (rs, rn) -> rs.getBoolean("is_user"), id));
    }

    @Override
    public boolean contains(String email) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?) AS is_user";

        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery,
                (rs, rn) -> rs.getBoolean("is_user"), email));
    }

    @Override
    public boolean delete(long id) {
        String sqlQuery = "" +
                "DELETE\n" +
                "FROM\n" +
                "users\n" +
                "WHERE\n" +
                "user_id = ?";

        int rowEffectedByQuery = jdbcTemplate.update(sqlQuery, id);
        return rowEffectedByQuery >= MIN_ROW_NUMBER_EFFECTED_BY_QUERY;
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "" +
                "SELECT\n" +
                "user_id,\n" +
                "name,\n" +
                "email\n" +
                "FROM\n" +
                "users";

        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    //------------------------------------------------------------------------------------------------------------------

    private User mapRowToUser(ResultSet rs, int rn) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .build();
    }
}
