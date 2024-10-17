package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.table.Column;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SQLParserService {

    @Autowired
    private FileDatabaseService fileDatabaseService;

    public void parseAndExecute(String sql) throws IOException {
        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            String tableName = parseTableName(sql);
            List<Column> columns = parseColumns(sql);
            fileDatabaseService.createTable(tableName, columns);
        } else if (sql.toUpperCase().startsWith("INSERT INTO")) {
            String valuesPart = sql.substring(sql.indexOf("VALUES") + 6).trim();
            fileDatabaseService.insertData(valuesPart);
        } else {
            throw new IllegalArgumentException("Invalid SQL command");
        }
    }

    private String parseTableName(String sql) {
        // Extract table name from the CREATE TABLE SQL command
        String[] parts = sql.split(" ");
        return parts[2];
    }

    private List<Column> parseColumns(String sql) {
        String columnsPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
        String[] columnsArray = columnsPart.split(",");
        List<Column> columns = new ArrayList<>();

        for (String columnDef : columnsArray) {
            String[] columnParts = columnDef.trim().split(" ");
            String columnName = columnParts[0];
            String columnType = columnParts[1];
            columns.add(new Column(columnName, columnType));
        }

        return columns;
    }
}
