package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("update User us " +
            "set us.name = :name " +
            "where us.id = :id")
    void updateName(@Param("name") String name, @Param("id") long id);

    @Modifying
    @Query("update User us " +
            "set us.email = :email " +
            "where us.id = :id")
    void updateEmail(@Param("email") String email, @Param("id") long id);
}
