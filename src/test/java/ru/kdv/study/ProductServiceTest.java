package ru.kdv.study;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kdv.study.exception.BadRequestException;
import ru.kdv.study.model.Product;
import ru.kdv.study.repository.ProductRepository;
import ru.kdv.study.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    private final Product validProduct = Product.builder()
            .id(1L)
            .name("Товар1")
            .price(BigDecimal.valueOf(100L))
            .createDate(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("Успешное создание товара")
    public void SuccessProductCreate() {
        Mockito.when(productRepository.insert(validProduct)).thenReturn(validProduct);

        Product result = productService.create(validProduct);

        assertThat(result).isEqualTo(validProduct);

        verify(productRepository).insert(validProduct);
    }

    @Test
    @DisplayName("Успешное создание товара")
    public void SuccessProductUpdate() {
        Mockito.when(productRepository.update(validProduct)).thenReturn(validProduct);

        Product result = productService.update(validProduct);

        assertThat(result).isEqualTo(validProduct);

        verify(productRepository).update(validProduct);
    }

    private final Product productNullName = Product.builder()
            .id(1L)
            .name(null)
            .price(BigDecimal.valueOf(100L))
            .createDate(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("Валидация null значения в качестве названия товара")
    public void validateNameNullValue(){
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> productService.create(productNullName));
    }

    private final Product productEmptyName = Product.builder()
            .id(1L)
            .name("")
            .price(BigDecimal.valueOf(100L))
            .createDate(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("Валидация пустой строки в качестве названия товара")
    public void validateNameEmptyValue(){
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> productService.create(productEmptyName));
    }

    private final Product productNullPrice = Product.builder()
            .id(1L)
            .name("Товар")
            .price(null)
            .createDate(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("Валидация null в качестве цены товара")
    public void validatePriceNullValue(){
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> productService.create(productNullPrice));
    }

    private final Product productNegativePrice = Product.builder()
            .id(1L)
            .name("Товар")
            .price(BigDecimal.valueOf(-10L))
            .createDate(LocalDateTime.now())
            .build();

    @Test
    @DisplayName("Валидация отрицательного значения в качестве цены товара")
    public void validatePriceNegativeValue(){
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> productService.create(productNegativePrice));
    }

}
