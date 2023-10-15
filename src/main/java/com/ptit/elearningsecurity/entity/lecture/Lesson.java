package com.ptit.elearningsecurity.entity.lecture;

import javax.persistence.*;
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
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    private String coverImage;

    @Column(name = "create_date")
    private Instant createdAt;

    @Column(name = "update_date")
    private Instant updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_lession_id")
    private CategoryLesson categoryLesson;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "lesson_id")
    private List<ImageLesson> contentsImages;

    @ManyToMany(mappedBy = "lessons")
    private List<Progress> progresses;
}
