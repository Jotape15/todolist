package me.jotape.todolist_api.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "tbl_users")
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    public UUID id;

    @Column(unique = true, nullable = false)
    public String username;

    @Column(nullable = false)
    public String name;

    @Column(nullable = false)
    public String password;

    @CreationTimestamp
    public LocalDateTime created;
}
