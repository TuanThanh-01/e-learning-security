package com.ptit.elearningsecurity.entity.labCTF;

import com.ptit.elearningsecurity.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class TotalAttemptChallengeCTF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer challengeCTFId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
