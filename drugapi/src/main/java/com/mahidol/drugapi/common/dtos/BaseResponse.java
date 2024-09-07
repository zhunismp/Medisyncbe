package com.mahidol.drugapi.common.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseResponse<T> {
    protected List<T> data;
    protected int total;
    protected boolean isCompleted;
}
