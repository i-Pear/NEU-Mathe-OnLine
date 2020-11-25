package com.ipear.web.training.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="exercise_record",indexes = {@Index(columnList = "uid")})
@Getter
@Setter
public class ExerciseRecord{

    @Id
    public String uid;

    @Column(length = 100000)
    public String record;

}
