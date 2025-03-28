package me.jotape.todolist_api.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import me.jotape.todolist_api.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        UserModel username = this.userRepository.findByUsername(userModel.getUsername());

        if (username == null) {
            String hashed = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
            userModel.setPassword(hashed);

            UserModel saved = this.userRepository.save(userModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
    }
}
