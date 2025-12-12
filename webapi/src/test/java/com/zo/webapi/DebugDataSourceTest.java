package com.zo.webapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

@SpringBootTest
@ActiveProfiles("test")
public class DebugDataSourceTest {

    @Autowired
    DataSource dataSource;

    @Test
    void printJdbcUrl() throws Exception {
        var conn = dataSource.getConnection();
        System.out.println(">>> JDBC URL = " + conn.getMetaData().getURL());
    }
}
