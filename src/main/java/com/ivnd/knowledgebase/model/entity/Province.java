package com.ivnd.knowledgebase.model.entity;

import lombok.Data;

import javax.persistence.*;

@Table(name = "province")
@Entity
@Data
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
