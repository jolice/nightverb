package ru.jolice.nightverb.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.nio.file.Path;

@Entity
@NoArgsConstructor
@Setter
@Table(name = "as_files")
public class SavedFile {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "VARCHAR(255)")
    @Getter
    private String id;

    public SavedFile(TaskMeta task) {
        this.taskMeta = task;
    }

    @Getter
    @Column(name = "path")
    private String path;

    @Getter
    @Column(name = "file_name")
    private String fileName;

    public void path(Path path) {
        this.fileName = path.getFileName().toString();
        this.path = path.toString();
    }

    @OneToOne(mappedBy = "file")
    private TaskMeta taskMeta;


}
