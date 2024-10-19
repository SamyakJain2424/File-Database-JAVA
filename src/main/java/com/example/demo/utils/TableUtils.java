package com.example.demo.utils;

import com.example.demo.entity.Column;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TableUtils {

    public String parseTableName(String sql) {
        String[] parts = sql.split(" ");
        return parts[2].toUpperCase();
    }

    public List<Column> parseColumns(String sql) {
        String columnsPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
        String[] columnsArray = columnsPart.split(",");
        List<Column> columns = new ArrayList<>();

        for (String columnDef : columnsArray) {
            String[] columnParts = columnDef.trim().split(" ");
            String columnName = columnParts[0].toUpperCase();
            String columnType = columnParts[1].toUpperCase();
            if (!columnType.equalsIgnoreCase("STRING") && !columnType.equalsIgnoreCase("INTEGER")) {
                throw new IllegalArgumentException("Invalid data type. Only STRING or INTEGER are allowed.");
            }
            columns.add(new Column(columnName, columnType));
        }

        return columns;
    }

    public List<String> parseInsertColumns(String sql) {
        String columnPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
        if(columnPart.endsWith(","))
        {
            throw new IllegalArgumentException("Unexpected token \",\" found");
        }
        String[] columnArray = columnPart.split(",");
        List<String> columns = new ArrayList<>();
        for (String column : columnArray) {
            columns.add(column.trim().toUpperCase());
        }
        return columns;
    }

    public List<String> parseValues(String sql) {
        String valuesPart = sql.substring(sql.lastIndexOf("(") + 1, sql.lastIndexOf(")"));

        if(valuesPart.endsWith(","))
        {
            throw new IllegalArgumentException("Unexpected token \",\" found");
        }

        String[] valuesArray = valuesPart.split(",");
        List<String> values = new ArrayList<>();
        for (String value : valuesArray) {
            values.add(value.trim());
        }
        return values;
    }

    public void validateInsertColumns(List<Column> metadataColumns, List<String> insertColumns) {
        Set<String> validColumnNames = metadataColumns.stream()
                .map(Column::getName)
                .collect(Collectors.toSet());

        Set<String> uniqueColumns = new HashSet<>();
        for (String col : insertColumns) {
            if (!validColumnNames.contains(col)) {
                throw new IllegalArgumentException("Invalid column name: " + col + ". Allowed column names are: " + validColumnNames);
            }

            if (!uniqueColumns.add(col)) {
                throw new IllegalArgumentException("Duplicate column name detected: " + col);
            }
        }
    }

    public List<String> reorderValuesWithDefaults(List<String> insertColumns, List<String> values, List<Column> tableColumns) {
        List<String> orderedValues = new ArrayList<>();
        for (Column column : tableColumns) {
            int index = insertColumns.indexOf(column.getName());
            if (index != -1) {
                orderedValues.add(values.get(index));
            } else {
                if (column.getType().equalsIgnoreCase("STRING")) {
                    orderedValues.add("null");
                } else if (column.getType().equalsIgnoreCase("INTEGER")) {
                    orderedValues.add("0");
                }
            }
        }
        return orderedValues;
    }

    public void validateDataTypes(List<String> values, List<Column> columns) {
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            Column column = columns.get(i);

            if (column.getType().equalsIgnoreCase("INTEGER")) {
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    throw new IllegalArgumentException("Expected an INTEGER for column " + column.getName() + ", but got: " + value);
                }
                try {
                    if (value.equals("null")) {
                        continue;
                    }
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Expected an INTEGER for column " + column.getName() + ", but got: " + value);
                }
            } else if (column.getType().equalsIgnoreCase("STRING")) {
                if (!value.startsWith("\"") || !value.endsWith("\"")) {
                    if (!value.equals("null")) {
                        throw new IllegalArgumentException("Expected a STRING for column " + column.getName() + ", but got: " + value);
                    }
                }
            }
        }
    }

}

