package ru.kdv.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.Exception.DataBaseException;
import ru.kdv.study.Exception.NoDataFoundException;
import ru.kdv.study.model.Product;
import ru.kdv.study.repository.mapper.ExistProductMapper;
import ru.kdv.study.repository.mapper.ProductMapper;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private static String INSERT = """
            INSERT 
              INTO product.product ("name", price) 
            VALUES(:name, :price)
            RETURNING *
            """;

    private static String UPDATE = """
            UPDATE product.product 
               SET "name" = :name,
                   price = :price
             WHERE id = :id
            RETURNING *
            """;

    private static String SELECT_BY_ID = """
            SELECT id, "name", price, create_date
              FROM product.product
             WHERE id = :id
            """;

    private static String EXIST_BY_ID = """
            SELECT EXISTS(SELECT 1
                           FROM product.product
                          WHERE id = :id) as value
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ProductMapper productMapper;
    private final ExistProductMapper existProductMapper;

    public Product insert(final Product product) {
        try {
            return jdbcTemplate.queryForObject(INSERT, productToSql(product), productMapper);
        } catch (Exception e) {
            throw handleExceptionProduct(e, product);
        }
    };

    public Product update(final Product product) {
        try {
            return jdbcTemplate.queryForObject(UPDATE, productToSql(product), productMapper);
        } catch (Exception e) {
            throw handleExceptionProduct(e, product);
        }
    }

    public Product getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new MapSqlParameterSource("id", id) , productMapper);
        } catch (Exception e) {
            throw handleExceptionProduct(e, id);
        }
    }

    public Boolean existById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(EXIST_BY_ID, new MapSqlParameterSource("id", id) , existProductMapper);
        } catch (Exception e) {
            throw handleExceptionProduct(e, id);
        }
    }

    private MapSqlParameterSource productToSql(final Product product) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", product.getId());
        params.addValue("name", product.getName());
        params.addValue("price", product.getPrice());

        return params;
    }

    private RuntimeException handleExceptionProduct(Exception e, final Product product) {
        if (e instanceof EmptyResultDataAccessException) {
            return NoDataFoundException.create(String.format("Товар не найден [%s]", product.getId()));
        } else if (e.getMessage().contains("product_uk1")) {
            return DataBaseException.create(String.format("Товар с именем \"%s\" уже существует", product.getName()));
        } else {
            return DataBaseException.create(e.getMessage());
        }
    }

    private RuntimeException handleExceptionProduct(Exception e, Long id) {
        if (e instanceof EmptyResultDataAccessException) {
            return NoDataFoundException.create(String.format("Товар не найден [%s]", id));
        } else {
            return DataBaseException.create(e.getMessage());
        }
    }
}