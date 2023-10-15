package com.ptit.elearningsecurity.entity.lecture;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "image_lesson")
public class ImageLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String imageName;
    private String imageUrl;
    private String type;
}
