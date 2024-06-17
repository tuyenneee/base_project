package com.ivnd.knowledgebase.model.request;

import com.ivnd.knowledgebase.common.BaseQuery;
import com.ivnd.knowledgebase.model.enumeration.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Getter
@Setter
public class PersonRequest extends BaseQuery {
    private String fullName;
    private Integer age;
    private Gender gender;
    private String identityNo;
    private String phone;
}
