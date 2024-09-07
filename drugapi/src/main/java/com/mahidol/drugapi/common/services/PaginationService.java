package com.mahidol.drugapi.common.services;

import com.mahidol.drugapi.common.models.Pagination;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class PaginationService<T> {
    public List<T> paginate(List<T> li, Pagination pagination) {
        int total = li.size();
        int number = pagination.getNumber();
        int size = pagination.getSize();
        int start = (number - 1) * size;

        if (start >= total) return Collections.emptyList();
        else {
            int end = Math.min(start + size, total);
            return li.subList(start, end);
        }
    }
}
