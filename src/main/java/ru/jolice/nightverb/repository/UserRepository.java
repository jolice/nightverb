package ru.jolice.nightverb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jolice.nightverb.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {

}
