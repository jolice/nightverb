package ru.jolice.nightverb.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "as_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @Getter
    private long id;

    @OneToMany(mappedBy = "user")
    private List<TaskMeta> tasks;

}
