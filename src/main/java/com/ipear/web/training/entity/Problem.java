package com.ipear.web.training.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="problems",indexes = {@Index(columnList = "id"),@Index(columnList = "chapter")})
@Getter
@Setter
public class Problem {

    @Id
    public int id;

    @Column
    public byte[] img0;

    @Column
    public byte[] img1;

    @Column
    public byte[] img2;

    @Column
    public byte[] img3;

    @Column
    public byte[] img4;

    @Column
    public byte[] imgans;

    @Column
    public int answer;

    @Column
    public String chapter;

}
