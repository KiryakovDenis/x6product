package ru.kdv.study.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class ProductExist {
    private Long id;
    private Boolean isExist;
}
