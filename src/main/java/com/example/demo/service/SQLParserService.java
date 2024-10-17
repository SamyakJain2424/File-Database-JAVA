package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SQLParserService {

    @Autowired
    private FileDatabaseService fileDatabaseService;


    public void parseAndExecute(String sql) throws IOException {
        System.out.println("------------Hello 2----------------");
        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            String tableDef = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
            System.out.print(tableDef);
            fileDatabaseService.createTable(tableDef);
        } else if (sql.toUpperCase().startsWith("INSERT INTO")) {
            String valuesPart = sql.substring(sql.indexOf("VALUES") + 6).trim();
            fileDatabaseService.insertData(valuesPart);
        } else {
            throw new IllegalArgumentException("Invalid SQL command");
        }
    }
}
