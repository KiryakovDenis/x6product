package ru.kdv.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kdv.study.exception.BadRequestException;
import ru.kdv.study.model.Product;
import ru.kdv.study.model.ProductExist;
import ru.kdv.study.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    public Product create(final Product product) {
        validate(product);
        return productRepository.insert(product);
    }

    @Transactional(rollbackFor = Exception.class)
    public Product update(final Product product) {
        validate(product);
        return productRepository.update(product);
    }

    @Transactional(readOnly = true)
    @Cacheable(value="x6Product", key="#id")
    public Product getById(final Long id) {
        return productRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductExist> productExistList(List<Long> productListId) {
        return productRepository.existById(productListId);
    }

    private void validate(final Product product) {
        if (!StringUtils.hasText(product.getName())) {
            throw BadRequestException.create("Необходимо указать название товара");
        }

        if (product.getPrice() == null) {
            throw BadRequestException.create("Необходимо указать цену товара");
        }

        if (product.getPrice().compareTo(BigDecimal.valueOf(0L)) < 0) {
            throw BadRequestException.create("Цена товара не может быть отрицательной");
        }
    }
}