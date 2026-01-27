package com.ordersystem.customers.dao.impl;

import com.ordersystem.customers.dao.CustomerDao;
import com.ordersystem.customers.dto.request.CustomerUpsertRequest;
import com.ordersystem.customers.dto.response.CustomerPageResponse;
import com.ordersystem.customers.dto.response.CustomerResponse;
import com.ordersystem.customers.dto.response.CustomerSummaryResponse;
import com.ordersystem.customers.service.impl.CustomerServiceImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

@Repository
public class CustomerDaoImpl implements CustomerDao {

    private static final Logger log = LoggerFactory.getLogger(CustomerDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public CustomerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMappers
    private final RowMapper<CustomerResponse> customerRowMapper = new RowMapper<>() {
        @Override
        public CustomerResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerResponse customer = new CustomerResponse();
            customer.setCustomerId(rs.getLong("ID_CLIENTE"));
            customer.setName(rs.getString("NOMBRE"));
            customer.setEmail(rs.getString("EMAIL"));
            customer.setCreatedAt(rs.getDate("FECHA_ALTA").toLocalDate());
            customer.setActive(rs.getString("ACTIVO"));
            return customer;
        }
    };

    private final RowMapper<CustomerSummaryResponse> customerSumaryRowMapper = new RowMapper<>() {
        @Override
        public CustomerSummaryResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
            CustomerSummaryResponse customer = new CustomerSummaryResponse();
            customer.setCustomerId(rs.getLong("ID_CLIENTE"));
            customer.setName(rs.getString("NOMBRE"));
            customer.setEmail(rs.getString("EMAIL"));
            customer.setCreatedAt(rs.getDate("FECHA_ALTA").toLocalDate());
            return customer;
        }
    };

    /**
     * Calls PKG_CLIENTES.LISTAR_CLIENTES
     */
    @Override
    public List<CustomerSummaryResponse> findAll() {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CLIENTES")
                .withProcedureName("LISTAR_CLIENTES")
                .returningResultSet("p_cursor", customerSumaryRowMapper);

        Map<String, Object> result = jdbcCall.execute();

        return (List<CustomerSummaryResponse>) result.get("p_cursor");
    }

    /**
     * Calls PKG_CLIENTES.OBTENER_CLIENTE
     */
    @Override
    public CustomerResponse findById(Long customerId) {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CLIENTES")
                .withProcedureName("OBTENER_CLIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.NUMERIC),
                        new SqlOutParameter("p_cursor", Types.REF_CURSOR)
                )
                .returningResultSet("p_cursor", customerRowMapper);

        Map<String, Object> result = jdbcCall.execute(
                Map.of("p_id", customerId)
        );

        List<CustomerResponse> customers =
                (List<CustomerResponse>) result.get("p_cursor");

        return customers.isEmpty() ? null : customers.get(0);
    }

    /**
     * Calls PKG_CLIENTES.CREAR_CLIENTE
     */
    @Override
    public Long createCustomer(CustomerUpsertRequest request) {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CLIENTES")
                .withProcedureName("CREAR_CLIENTE")
                .declareParameters(
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_email", Types.VARCHAR),
                        new SqlOutParameter("p_id", Types.NUMERIC)
                );

        Map<String, Object> result = jdbcCall.execute(
                Map.of(
                        "p_nombre", request.getName(),
                        "p_email", request.getEmail()
                )
        );

        return ((Number) result.get("p_id")).longValue();
    }

    /**
     * Calls PKG_CLIENTES.ACTUALIZAR_CLIENTE
     */
    @Override
    public void updateCustomer(Long customerId, CustomerUpsertRequest request) {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CLIENTES")
                .withProcedureName("ACTUALIZAR_CLIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.NUMERIC),
                        new SqlParameter("p_nombre", Types.VARCHAR),
                        new SqlParameter("p_email", Types.VARCHAR)
                );

        Map<String, Object> result = jdbcCall.execute(
                Map.of(
                        "p_id", customerId,
                        "p_nombre", request.getName(),
                        "p_email", request.getEmail()
                )
        );
    }

    /**
     * Calls PKG_CLIENTES.DESACTIVAR_CLIENTE
     */
    @Override
    public void deleteCustomer(Long customerId) {

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CLIENTES")
                .withProcedureName("DESACTIVAR_CLIENTE")
                .declareParameters(
                        new SqlParameter("p_id", Types.NUMERIC)
                );

        Map<String, Object> result = jdbcCall.execute(
                Map.of(
                        "p_id", customerId
                )
        );
    }

    /**
     * Calls PKG_CLIENTES.CLIENTES_PAGINADOS clientes_paginados
     */
    @Override
    public CustomerPageResponse customerPage(int page, int size) {

        log.info("Result customers page={}, size={}", page, size);

        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withCatalogName("PKG_CLIENTES")
                .withProcedureName("CLIENTES_PAGINADOS")
                .declareParameters(
                        new SqlParameter("p_pagina", Types.NUMERIC),
                        new SqlParameter("p_por_pagina", Types.NUMERIC),
                        new SqlOutParameter("p_total", Types.NUMERIC),
                        new SqlOutParameter("p_cursor", Types.REF_CURSOR)
                );

        Map<String, Object> result = jdbcCall.execute(
                Map.of(
                        "p_pagina", page,
                        "p_por_pagina", size
                )
        );

        List<CustomerSummaryResponse> customers = (List<CustomerSummaryResponse>) result.get("p_cursor");
        BigDecimal totalBD = (BigDecimal) result.get("p_total");
        Long total = totalBD.longValue();

        log.info("Total customers total={}", total);

        return new CustomerPageResponse(total, customers);

    }

}
