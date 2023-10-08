package com.ptit.elearningsecurity.entity;

import com.ptit.elearningsecurity.entity.discuss.Comment;
import com.ptit.elearningsecurity.entity.discuss.Post;
import com.ptit.elearningsecurity.entity.lecture.Progress;
import com.ptit.elearningsecurity.entity.quiz.Score;
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
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String avatar;
    private Instant createdAt;
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Score> scores;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Progress progress;
}
