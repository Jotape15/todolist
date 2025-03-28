package me.jotape.todolist_api.task;

import jakarta.servlet.http.HttpServletRequest;
import me.jotape.todolist_api.task.repository.TaskRepository;
import me.jotape.todolist_api.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    public TaskRepository taskRepository;

    @PostMapping("/create")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        System.out.println(userId);
        taskModel.setUserId((UUID) userId);

        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(taskModel.getStartAt()) || now.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid start or end date"); //arrumar mensagem
        }

        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("começa depois de terminar"); //arrumar mensagem
        }

        TaskModel task = taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/list")
    public List<TaskModel> list(HttpServletRequest request) {
        Object userId = request.getAttribute("userId");
        System.out.println("UserID: " + userId);

        if (userId == null) {
            System.out.println("Atributo userId não encontrado na requisição!");
        }

        List<TaskModel> tasks = taskRepository.findByUserId((UUID) userId);
        System.out.println(userId);
        System.out.println(tasks);
        return tasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
        TaskModel task = this.taskRepository.findById(id).orElse(null);

        if (task == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task not found");
        }

        if (!task.getUserId().equals(request.getAttribute("userId"))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user id");
        }

        Utils.copyNonNullProperties(taskModel, task);
        TaskModel taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);
    }
}
