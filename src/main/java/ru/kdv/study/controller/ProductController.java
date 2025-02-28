package ru.kdv.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kdv.study.model.Product;
import ru.kdv.study.model.ProductExist;
import ru.kdv.study.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Создать товар")
    public Product createProduct(@RequestBody final Product product) {
        return productService.create(product);
    }

    @PatchMapping
    @Operation(summary = "Обновить товар")
    public Product updateProduct(@RequestBody final Product product) {
        return productService.update(product);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить товар по id")
    public Product getById(@PathVariable final Long id) {
        return productService.getById(id);
    }

    @PostMapping("/exist")
    @Operation(summary = "Проверить существование товаров с указанными id")
    public List<ProductExist> checkExistingProduct(@RequestBody final List<Long> listProductId) {
        return productService.productExistList(listProductId);
    }
}