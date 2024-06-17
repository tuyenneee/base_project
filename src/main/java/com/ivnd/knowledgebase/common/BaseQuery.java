package com.ivnd.knowledgebase.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Objects;

/**
 * @author: tuyen.nguyenvan
 * @email: tuyen.nguyen3@ivnd.com.vn
 * @Date: 16/05/2024
 */
@Getter
@Setter
public class BaseQuery {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate toDate;

    private String sort;

    private Integer page;

    private Integer size;

    public static class Constants {
        private Constants() {}

        private static final int DEFAULT_PAGE = 0;
        private static final int DEFAULT_SIZE = 20;
        private static final int DEFAULT_MAX_SIZE = 100;
    }

    public Integer getPage() {
        return this.getDefaultPage(this.page, Constants.DEFAULT_PAGE);
    }

    public Integer getDefaultPage(Integer page, Integer defaultPage) {
        if (Objects.isNull(page) || page < 0) {
            return defaultPage;
        }
        return page;
    }

    public Integer getSize() {
        return this.getDefaultSize(this.size, Constants.DEFAULT_SIZE, Constants.DEFAULT_MAX_SIZE);
    }

    public Integer getDefaultSize(Integer size, Integer defaultSize, Integer defaultMaxSize) {
        if (Objects.isNull(size) || size < 1) {
            return defaultSize;
        }
        if (size > defaultMaxSize) {
            return defaultMaxSize;
        }
        return size;
    }
}
