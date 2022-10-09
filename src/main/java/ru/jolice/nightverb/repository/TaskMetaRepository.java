package ru.jolice.nightverb.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.jolice.nightverb.entity.TaskMeta;

import java.util.List;

public interface TaskMetaRepository extends CrudRepository<TaskMeta, Long> {

    @Query(value = "select file_name, file_id from as_tasks at join as_files af on at.file_id = af.id and archived = true and user_id = :userId order by at.id desc limit 10",
            nativeQuery = true)
    List<HistoryProjection> getRecentHistory(long userId);


}
