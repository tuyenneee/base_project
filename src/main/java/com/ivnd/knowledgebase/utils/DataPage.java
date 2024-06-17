package com.ivnd.knowledgebase.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataPage<T> {
    private List<T> content;
    private long totalElements;
    private int totalPages;
    private Integer page;
    private Integer size;
    private boolean first;
    private boolean last;
    private boolean empty;

    public DataPage(List<T> content, Integer page, Integer size, long totalElements) {
        this.content = content;
        var totalPage = this.getTotalPages(content, size, totalElements);
        this.totalPages = totalPage;
        this.totalElements = this.getTotalElements(totalElements);
        this.page = page;
        this.size = size;
        this.first = this.first(page);
        this.last = this.last(page, totalPage);
        this.empty = this.empty(content);
    }

    private int getTotalPages(List<T> content, Integer size, long totalElements) {
        var currentSize = content.size() == size ? size : content.size();
        return currentSize == 0 ? 1 : (int) Math.ceil((double) totalElements / (double) currentSize);
    }

    private long getTotalElements(long totalElements) {
        return totalElements;
    }

    private boolean empty(List<T> content) {
        return content == null || content.isEmpty();
    }

    private boolean hasNext(Integer page, int totalPages) {
        return page + 1 < totalPages;
    }

    private boolean last(Integer page, int totalPages) {
        return !this.hasNext(page, totalPages);
    }

    private boolean hasPrevious(Integer page) {
        return page > 0;
    }

    private boolean first(Integer page) {
        return !this.hasPrevious(page);
    }
}
