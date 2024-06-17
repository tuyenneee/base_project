package com.ivnd.knowledgebase.anotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Loggable {
    String value() default "";
}
