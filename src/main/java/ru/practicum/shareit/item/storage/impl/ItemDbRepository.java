//package ru.practicum.shareit.item.storage.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.Primary;
//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.stereotype.Repository;
//import org.springframework.util.StringUtils;
//import ru.practicum.shareit.item.exception.IncorrectOwnerException;
//import ru.practicum.shareit.item.model.Item;
//import ru.practicum.shareit.item.storage.ItemRepository;
//import ru.practicum.shareit.request.model.ItemRequest;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Collection;
//
//@Slf4j
//@Primary
//@Repository
//@RequiredArgsConstructor
//public class ItemDbRepository implements ItemRepository {
//    private final JdbcTemplate jdbcTemplate;
//
//    @Override
//    public Item create(Item item) {
//        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
//        simpleJdbcInsert
//                .withTableName("item")
//                .usingGeneratedKeyColumns("item_id");
//        long itemId = simpleJdbcInsert.executeAndReturnKey(item.toMap()).longValue();
//        item.setId(itemId);
//
//        String sqlQuery = "INSERT INTO users_item (user_id, item_id) VALUES (?, ?)";
//        jdbcTemplate.update(sqlQuery, item.getOwner(), itemId);
//
//        return item;
//    }
//
//    @Override
//    public boolean contains(long id) {
//        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM item WHERE item_id = ?) AS is_item";
//
//        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sqlQuery,
//                (rs, rn) -> rs.getBoolean("is_item"), id));
//    }
//
//    @Override
//    public Item findById(long id) {
//        String sqlQuery = "SELECT * FROM item WHERE item_id = ?";
//
//        try {
//            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToItem, id);
//        } catch (EmptyResultDataAccessException e) {
//            return null;
//        }
//    }
//
//    @Override
//    public Item update(Item item) throws IncorrectOwnerException {
//        long userId = findUserId(item.getId());
//        String sqlQuery = "UPDATE item SET ";
//
//        if (userId != item.getOwner()) {
//            String message = String.format("The item does not belong to a user with id { %d }", item.getOwner());
//            throw new IncorrectOwnerException(message);
//        }
//
//        if (!StringUtils.hasText(item.getName())
//                && !StringUtils.hasText(item.getDescription())
//                && item.getAvailable() == null) {
//            sqlQuery += "" +
//                    "description = ?, " +
//                    "available = ? " +
//                    "WHERE item_id = ?";
//
//            jdbcTemplate.update(sqlQuery, item.getDescription(), item.getAvailable(), item.getId());
//        } else if (!StringUtils.hasText(item.getDescription()) && item.getAvailable() == null) {
//            sqlQuery += "" +
//                    "name = ? " +
//                    "WHERE item_id = ?";
//            jdbcTemplate.update(sqlQuery, item.getName(), item.getId());
//        } else if (!StringUtils.hasText(item.getName()) && item.getAvailable() == null) {
//            sqlQuery += "" +
//                    "description = ? " +
//                    "WHERE item_id = ?";
//
//            jdbcTemplate.update(sqlQuery, item.getDescription(), item.getId());
//        } else if (!StringUtils.hasText(item.getName()) && !StringUtils.hasText(item.getDescription())) {
//            sqlQuery += "" +
//                    "available = ? " +
//                    "WHERE item_id = ?";
//
//            jdbcTemplate.update(sqlQuery, item.getAvailable(), item.getId());
//        } else if (item.getAvailable() == null) {
//            sqlQuery += "" +
//                    "name = ?, " +
//                    "description = ? " +
//                    "WHERE item_id = ?";
//
//            jdbcTemplate.update(sqlQuery, item.getName(), item.getDescription(), item.getId());
//        } else if (!StringUtils.hasText(item.getDescription())) {
//            sqlQuery += "" +
//                    "name = ?, " +
//                    "available = ? " +
//                    "WHERE item_id = ?";
//
//            jdbcTemplate.update(sqlQuery, item.getName(), item.getAvailable(), item.getId());
//        } else {
//            sqlQuery += "" +
//                    "name = ?, " +
//                    "description = ?, " +
//                    "available = ? " +
//                    "WHERE item_id = ?";
//
//            jdbcTemplate.update(sqlQuery, item.getName(), item.getDescription(), item.getAvailable(), item.getId());
//        }
//
//        return findById(item.getId());
//    }
//
//    @Override
//    public Collection<Item> findAll(long userId) {
//        String sqlQuery = "" +
//                "SELECT " +
//                "* " +
//                "FROM " +
//                "item " +
//                "WHERE " +
//                "item_id IN " +
//                "    ( " +
//                "    SELECT " +
//                "    item_id " +
//                "    FROM " +
//                "    users_item " +
//                "    WHERE " +
//                "    user_id = ? " +
//                "    )";
//
//        return jdbcTemplate.query(sqlQuery, this::mapRowToItem, userId);
//    }
//
//    @Override
//    public Collection<Item> searchByName(String text) {
//        String textForQuery = "%" + text + "%";
//        String sqlQuery = "SELECT " +
//                "* " +
//                "FROM item " +
//                "WHERE " +
//                "available = TRUE " +
//                "AND " +
//                "description ILIKE ?";
//
//        return jdbcTemplate.query(sqlQuery, this::mapRowToItem, textForQuery);
//    }
//
//    //------------------------------------------------------------------------------------------------------------------
//
//    private Item mapRowToItem(ResultSet rs, int rn) throws SQLException {
//        long owner = findUserId(rs.getLong("item_id"));
//
//        return Item.builder()
//                .id(rs.getLong("item_id"))
//                .name(rs.getString("name"))
//                .description(rs.getString("description"))
//                .available(rs.getBoolean("available"))
//                .request(ItemRequest.builder().build())
//                .owner(owner)
//                .build();
//    }
//
//    private Long findUserId(long itemId) {
//        String sqlQuery = "" +
//                "SELECT " +
//                "user_id " +
//                "FROM users_item " +
//                "WHERE item_id = ?";
//
//        return jdbcTemplate.queryForObject(sqlQuery, (rs, rn) -> rs.getLong("user_id"), itemId);
//    }
//}
