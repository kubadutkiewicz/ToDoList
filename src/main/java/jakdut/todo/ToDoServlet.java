package jakdut.todo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
class ToDoServlet {
    private final Logger logger = LoggerFactory.getLogger(ToDoServlet.class);

    private ToDoRepository repository;

    ToDoServlet(ToDoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<ToDo>> findAll() {
        logger.info("Got request");
        return ResponseEntity.ok(repository.findAll());
    }

    @PutMapping("/{id}")
    ResponseEntity<ToDo> toggleTodo(@PathVariable Integer id) {
        var todo = repository.findById(id);
        todo.ifPresent(t -> {
            t.setDone(!t.isDone());
            repository.save(t);
        });
        return todo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    ResponseEntity<ToDo> saveTodo(@RequestBody ToDo todo) {
        return ResponseEntity.ok(repository.save(todo));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<ToDo> deleteTodo(@PathVariable Integer id) {
        var isRemoved = repository.findById(id);
        isRemoved.ifPresent(r -> repository.deleteById(id));
        return isRemoved.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}


