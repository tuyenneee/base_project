package com.ivnd.knowledgebase.model.response;

import com.ivnd.knowledgebase.model.entity.Person;
import com.ivnd.knowledgebase.model.enumeration.Gender;
import lombok.Builder;
import lombok.Data;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Data
@Builder
public class PersonResponse {
    private Long id;
    private String fullName;
    private int age;
    private Gender gender;
    private String identityNo;
    private String phone;

    public static PersonResponse of(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .fullName(person.getFullName())
                .age(person.getAge())
                .gender(person.getGender())
                .identityNo(person.getIdentityNo())
                .phone(person.getPhone())
                .build();
    }
}
