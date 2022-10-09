package ru.jolice.nightverb.controller;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.jolice.nightverb.dto.out.UserSession;
import ru.jolice.nightverb.entity.User;
import ru.jolice.nightverb.repository.UserRepository;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final UserRepository repository;

    @PostMapping("/session/start")
    public UserSession get() {
        val user = new User();
        repository.save(user);
        return new UserSession(user.id());
    }

}
