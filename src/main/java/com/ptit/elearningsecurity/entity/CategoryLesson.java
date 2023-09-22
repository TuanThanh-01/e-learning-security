package com.ptit.elearningsecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.time.Instant;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String categoryName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "create_date")
    private Instant createdAt;

    @Column(name = "update_date")
    private Instant updatedAt;

    @OneToMany(mappedBy = "categoryLesson")
    private List<Lesson> lessons;
}
