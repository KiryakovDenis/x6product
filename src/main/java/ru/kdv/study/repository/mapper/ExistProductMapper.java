package ru.kdv.study.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.kdv.study.model.ProductExist;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ExistProductMapper implements RowMapper<ProductExist> {
    @Override
    public ProductExist mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ProductExist.builder()
                .id(rs.getLong("id"))
                .isExist(rs.getBoolean("is_exist"))
                .build();
    }
}
