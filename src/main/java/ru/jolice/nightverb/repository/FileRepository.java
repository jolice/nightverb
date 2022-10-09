package ru.jolice.nightverb.repository;

import org.springframework.data.repository.CrudRepository;
import ru.jolice.nightverb.entity.SavedFile;

public interface FileRepository extends CrudRepository<SavedFile, String> {

}
