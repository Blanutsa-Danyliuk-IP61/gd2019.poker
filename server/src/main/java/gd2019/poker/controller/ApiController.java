package gd2019.poker.controller;

import gd2019.poker.model.dto.SimpleRequest;
import gd2019.poker.service.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiController {

    private final Repository repository;

    public ApiController(Repository repository) {
        this.repository = repository;
    }

    @PostMapping("/check/login")
    public boolean checkLogin(@RequestBody SimpleRequest<String> request) {
        return repository.isLoginUnique(request.getData());
    }
}
