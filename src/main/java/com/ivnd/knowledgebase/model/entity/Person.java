package com.ivnd.knowledgebase.model.entity;

import com.ivnd.knowledgebase.model.enumeration.Gender;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Table(name = "person")
@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Person extends AbstractAuditingUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "phone")
    private String phone;

    @Column(name = "identity_no")
    private String identityNo;

}
