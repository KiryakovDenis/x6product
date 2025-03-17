package ru.kdv.study.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class Product implements Serializable {
    Long id;
    String name;
    BigDecimal price;
    LocalDateTime createDate;
}
