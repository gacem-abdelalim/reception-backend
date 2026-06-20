package org.upro.reception.LogiPharm;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Repository;
import org.upro.reception.DTO.FournisseurDto;
import org.upro.reception.DTO.ProduitDto;
import org.upro.reception.db.Entity.DynamicQuery;
import org.upro.reception.db.Repo.DynamicQueryRepository;

import java.util.List;


@Slf4j
@Repository
public class oracleReadRepository {

    private JdbcTemplate oracleJdbc;

    private NamedParameterJdbcTemplate namedJdbc;

    @Autowired
    private OracleConnectionConfig  oracleConnectionConfig;


    @Autowired
    private DynamicQueryRepository dynamicQueryRepository;
    @PostConstruct
    public void init() {



        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(oracleConnectionConfig.getDriver());
        ds.setUrl(oracleConnectionConfig.getUrl());
        ds.setUsername(oracleConnectionConfig.getUsername());
        ds.setPassword(oracleConnectionConfig.getPassword());

        this.oracleJdbc = new JdbcTemplate(ds);

        this.namedJdbc = new NamedParameterJdbcTemplate(ds);
    }



    public List<ProduitDto> getProduits() {

        DynamicQuery dq = dynamicQueryRepository.findByName("reception-products")
                .orElseThrow(() -> new RuntimeException("Query 'reception-products' not found"));

        String sql = dq.getSqlText();

        return oracleJdbc.query(sql, (rs, rowNum) ->
                new ProduitDto(
                        rs.getInt("med_id"),
                        rs.getString("produit"),
                        rs.getString("labo"),
                        rs.getString("dosage"),
                        rs.getString("condit"),
                        rs.getString("forme")
                )
        );
    }


    public List<FournisseurDto> getFournisseurs() {

        DynamicQuery dq = dynamicQueryRepository.findByName("reception-fourneseur")
                .orElseThrow(() -> new RuntimeException("Query 'reception-fourneseur' not found"));

        String sql = dq.getSqlText();

        return oracleJdbc.query(sql, (rs, rowNum) ->
                new FournisseurDto(
                        rs.getInt("ter_id"),
                        rs.getString("fournisseur")
                )
        );
    }


}
