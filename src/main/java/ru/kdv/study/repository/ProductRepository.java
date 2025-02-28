package ru.kdv.study.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.Exception.DataBaseException;
import ru.kdv.study.Exception.NoDataFoundException;
import ru.kdv.study.model.Product;
import ru.kdv.study.model.ProductExist;
import ru.kdv.study.repository.mapper.ExistProductMapper;
import ru.kdv.study.repository.mapper.ProductMapper;

import java.util.List;
import java.util.StringJoiner;

@Repository
@RequiredArgsConstructor
public class ProductRepository {

    private static final String INSERT = """
            INSERT INTO product.product ("name", price)
            VALUES(:name, :price)
            RETURNING *
            """;

    private static final String UPDATE = """
            UPDATE product.product
               SET "name" = :name,
                   price = :price
             WHERE id = :id
            RETURNING *
            """;

    private static final String SELECT_BY_ID = """
            SELECT id, "name", price, create_date
              FROM product.product
             WHERE id = :id
            """;

    private static final String EXIST_BY_ARRAY_ID = """
            SELECT u.product_id as id, p.id IS NOT NULL is_exist
              FROM product.product p
              RIGHT JOIN (SELECT UNNEST::int product_id
                            FROM unnest(string_to_array(:ids, ','))) u ON u.product_id = p.id
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
    }

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

    public List<ProductExist> existById(final List<Long> listProductId) {
        try {
            String listIds = StringUtils.join(listProductId, ',');
            return jdbcTemplate.query(
                    EXIST_BY_ARRAY_ID,
                    new MapSqlParameterSource("ids", listIds),
                    existProductMapper
            );
        } catch (Exception e) {
            throw handleExceptionProduct(e);
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
            return DataBaseException.create(new StringJoiner("\n")
                    .add(e.getMessage())
                    .add(e.getCause().getMessage())
                    .toString()
            );
        }
    }

    private RuntimeException handleExceptionProduct(Exception e, Long id) {
        if (e instanceof EmptyResultDataAccessException) {
            return NoDataFoundException.create(String.format("Товар не найден [%s]", id));
        } else {
            return DataBaseException.create(new StringJoiner("\n")
                    .add(e.getMessage())
                    .add(e.getCause().getMessage())
                    .toString()
            );
        }
    }

    private RuntimeException handleExceptionProduct(Exception e) {
        return DataBaseException.create(new StringJoiner("\n")
                .add(e.getMessage())
                .add(e.getCause().getMessage())
                .toString()
        );
    }
}