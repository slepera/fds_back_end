package com.entities;
import javax.persistence.*;

@Entity(name="log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String date;
    private String level;
    private String component;
}
