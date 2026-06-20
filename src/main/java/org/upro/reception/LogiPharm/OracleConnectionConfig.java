package org.upro.reception.LogiPharm;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class OracleConnectionConfig {

    @Value("${pharmdb.URL}")
    private String url;

    @Value("${pharmdb.USERNAME}")
    private String username;

    @Value("${pharmdb.PASSWORD}")
    private String password;

    @Value("${pharmdb.DRIVER}")
    private String driver;


}