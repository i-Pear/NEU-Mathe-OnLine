package com.ipear.web.training.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="users",indexes = {@Index(columnList = "alias")})
@Getter
@Setter
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int uid;

    @Column
    public String alias;

    @Column
    public String password;

    @Column
    public Boolean sex;

}
