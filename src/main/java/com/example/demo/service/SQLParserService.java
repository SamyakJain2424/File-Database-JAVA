//package com.example.demo.service;
//
//import com.example.demo.table.Column;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class SQLParserService {
//
//    @Autowired
//    private FileDatabaseService fileDatabaseService;
//
//    public void parseAndExecuteTable(String sql) throws IOException {
//        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
//            String tableName = parseTableName(sql);
//            List<Column> columns = parseColumns(sql);
//            fileDatabaseService.createTable(tableName, columns);
//        } else {
//            throw new IllegalArgumentException("Invalid SQL command");
//        }
//    }
//
//    public void parseAndExecuteData(String sql) throws IOException {
//        if (sql.toUpperCase().startsWith("INSERT INTO")) {
//            String tableName = parseTableName(sql);
//
//            if (!fileDatabaseService.tableExists(tableName)) {
//                throw new IllegalArgumentException("Table does not exist: " + tableName);
//            }
//
//            List<Column> tableColumns = fileDatabaseService.getTableColumns(tableName);
//
//            // Extract column names and values
//            String columnPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
//            String valuesPart = sql.substring(sql.indexOf("VALUES") + 6).trim();
//
//            List<String> insertColumns = Arrays.stream(columnPart.split(","))
//                    .map(String::trim)
//                    .collect(Collectors.toList());
//
//            List<String> values = Arrays.stream(valuesPart.replaceAll("[()]", "").split(","))
//                    .map(String::trim)
//                    .collect(Collectors.toList());
//
//            // Validate column duplication
//            Set<String> uniqueColumns = new HashSet<>(insertColumns);
//            if (uniqueColumns.size() != insertColumns.size()) {
//                throw new IllegalArgumentException("Duplicate column names are not allowed");
//            }
//
//            // Validate column and value count
//            if (insertColumns.size() != values.size()) {
//                throw new IllegalArgumentException("Mismatch between column count and values count");
//            }
//
//            validateColumns(insertColumns, tableColumns); // Validate column names
//
//            // Reorder the values based on the table definition
//            List<String> orderedValues = reorderValues(insertColumns, values, tableColumns);
//
//            // Validate data types
//            validateDataTypes(orderedValues, tableColumns);
//
//            // If validation passes, insert the data
//            fileDatabaseService.insertData(orderedValues);
//        } else {
//            throw new IllegalArgumentException("Invalid SQL command");
//        }
//    }
//
//    private String parseTableName(String sql) {
//        String[] parts = sql.split(" ");
//        return parts[2];
//    }
//
//    private List<Column> parseColumns(String sql) {
//        String columnsPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
//        String[] columnsArray = columnsPart.split(",");
//        List<Column> columns = new ArrayList<>();
//
//        for (String columnDef : columnsArray) {
//            String[] columnParts = columnDef.trim().split(" ");
//            String columnName = columnParts[0];
//            String columnType = columnParts[1].toUpperCase(); // Normalize to uppercase for validation
//
//            // Validate that the column type is either STRING or INTEGER
//            if (!columnType.equals("STRING") && !columnType.equals("INTEGER")) {
//                throw new IllegalArgumentException("Invalid data type for column '" + columnName + "'. Allowed types are STRING or INTEGER.");
//            }
//
//            columns.add(new Column(columnName, columnType));
//        }
//
//        return columns;
//    }
//
//
////    private List<Column> parseColumns(String sql) {
////        String columnsPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
////        String[] columnsArray = columnsPart.split(",");
////        List<Column> columns = new ArrayList<>();
////
////        for (String columnDef : columnsArray) {
////            String[] columnParts = columnDef.trim().split(" ");
////            String columnName = columnParts[0];
////            String columnType = columnParts[1].toUpperCase();
////            columns.add(new Column(columnName, columnType));
////        }
////
////        return columns;
////    }
//
//    private void validateColumns(List<String> insertColumns, List<Column> tableColumns) {
//        Set<String> tableColumnNames = tableColumns.stream()
//                .map(Column::getName)
//                .collect(Collectors.toSet());
//
//        for (String insertColumn : insertColumns) {
//            if (!tableColumnNames.contains(insertColumn)) {
//                throw new IllegalArgumentException("Column name " + insertColumn + " does not match the table definition");
//            }
//        }
//    }
//
//    private List<String> reorderValues(List<String> insertColumns, List<String> values, List<Column> tableColumns) {
//        // Reorder values based on the order of columns in the table definition
//        Map<String, String> columnValueMap = new HashMap<>();
//        for (int i = 0; i < insertColumns.size(); i++) {
//            columnValueMap.put(insertColumns.get(i), values.get(i));
//        }
//
//        List<String> orderedValues = new ArrayList<>();
//        for (Column column : tableColumns) {
//            orderedValues.add(columnValueMap.get(column.getName()));
//        }
//
//        return orderedValues;
//    }
//
//    private void validateDataTypes(List<String> values, List<Column> tableColumns) {
//        for (int i = 0; i < values.size(); i++) {
//            String value = values.get(i);
//            String expectedType = tableColumns.get(i).getType();
//
//            if (expectedType.equals("INTEGER")) {
//                if (!isInteger(value)) {
//                    throw new IllegalArgumentException("Expected an INTEGER for column " + tableColumns.get(i).getName() + ", but got: " + value);
//                }
//            }
//            // Add other data type validations here (e.g., STRING, BOOLEAN, etc.)
//        }
//    }
//
//    private boolean isInteger(String value) {
//        try {
//            Integer.parseInt(value);
//            return true;
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }
//}













package com.example.demo.service;

import com.example.demo.table.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SQLParserService {

    @Autowired
    private FileDatabaseService fileDatabaseService;

    private String createdTableName; // Store the name of the created table

    public void parseAndExecuteTable(String sql) throws IOException {
        if (sql.toUpperCase().startsWith("CREATE TABLE")) {
            String tableName = parseTableName(sql);
            createdTableName = tableName; // Save the created table name
            List<Column> columns = parseColumns(sql);
            fileDatabaseService.createTable(tableName, columns);
        } else {
            throw new IllegalArgumentException("Invalid SQL command for table creation.");
        }
    }

    public void parseAndExecuteData(String sql) throws IOException {
        if (sql.toUpperCase().startsWith("INSERT INTO")) {
            String tableName = parseTableName(sql);

            // Validate table name
            if (!tableName.equalsIgnoreCase(createdTableName)) {
                throw new IllegalArgumentException("Table name does not match. Expected: " + createdTableName);
            }

            if (!fileDatabaseService.tableExists()) {
                throw new IllegalArgumentException("Table does not exist.");
            }

            List<String> insertColumns = parseInsertColumns(sql);
            List<String> values = parseValues(sql);

            List<Column> tableColumns = fileDatabaseService.getTableMetadata();

            // Validate columns, but allow fewer columns than in the table
            validateInsertColumns(insertColumns, tableColumns);

            // Prepare ordered values with defaults
            List<String> orderedValues = reorderValuesWithDefaults(insertColumns, values, tableColumns);

            // Validate data types
            validateDataTypes(orderedValues, tableColumns);

            // Insert the reordered values
            fileDatabaseService.insertData(String.join(",", orderedValues));
        } else {
            throw new IllegalArgumentException("Invalid SQL command for data insertion.");
        }
    }

    private String parseTableName(String sql) {
        String[] parts = sql.split(" ");
        return parts[2]; // Table name comes after "CREATE TABLE" or "INSERT INTO"
    }

    private List<Column> parseColumns(String sql) {
        String columnsPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
        String[] columnsArray = columnsPart.split(",");
        List<Column> columns = new ArrayList<>();

        for (String columnDef : columnsArray) {
            String[] columnParts = columnDef.trim().split(" ");
            String columnName = columnParts[0];
            String columnType = columnParts[1];
            if (!columnType.equalsIgnoreCase("STRING") && !columnType.equalsIgnoreCase("INTEGER")) {
                throw new IllegalArgumentException("Invalid data type. Only STRING or INTEGER are allowed.");
            }
            columns.add(new Column(columnName, columnType));
        }

        return columns;
    }

    private List<String> parseInsertColumns(String sql) {
        String columnPart = sql.substring(sql.indexOf("(") + 1, sql.indexOf(")"));
        String[] columnArray = columnPart.split(",");
        List<String> columns = new ArrayList<>();
        for (String column : columnArray) {
            columns.add(column.trim());
        }
        return columns;
    }

    private List<String> parseValues(String sql) {
        String valuesPart = sql.substring(sql.lastIndexOf("(") + 1, sql.lastIndexOf(")"));
        String[] valuesArray = valuesPart.split(",");
        List<String> values = new ArrayList<>();
        for (String value : valuesArray) {
            values.add(value.trim());
        }
        return values;
    }

    private void validateInsertColumns(List<String> insertColumns, List<Column> tableColumns) {
        // Check for extra columns
        for (String column : insertColumns) {
            boolean found = false;
            for (Column tableColumn : tableColumns) {
                if (column.equals(tableColumn.getName())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Column " + column + " does not match the table definition.");
            }
        }
        // No need to check for column count, as fewer columns are allowed
    }

    private List<String> reorderValuesWithDefaults(List<String> insertColumns, List<String> values, List<Column> tableColumns) {
        List<String> orderedValues = new ArrayList<>();
        for (Column column : tableColumns) {
            int index = insertColumns.indexOf(column.getName());
            if (index != -1) {
                orderedValues.add(values.get(index));
            } else {
                // Assign default values for missing columns
                if (column.getType().equals("STRING")) {
                    orderedValues.add("null"); // or you can choose to leave it empty, depending on your needs
                } else if (column.getType().equals("INTEGER")) {
                    orderedValues.add("0"); // Default for integer
                }
            }
        }
        return orderedValues;
    }

    private void validateDataTypes(List<String> values, List<Column> columns) {
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            Column column = columns.get(i);

            if (column.getType().equalsIgnoreCase("INTEGER")) {
                // Check if the value is a valid integer (not enclosed in quotes)
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    throw new IllegalArgumentException("Expected an INTEGER for column " + column.getName() + ", but got: " + value);
                }
                try {
                    if (value.equals("null")) {
                        continue; // Allow null values for INTEGER
                    }
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Expected an INTEGER for column " + column.getName() + ", but got: " + value);
                }
            } else if (column.getType().equalsIgnoreCase("STRING")) {
                // Check if the STRING value is enclosed in double quotes
                if (!value.startsWith("\"") || !value.endsWith("\"")) {
                    if (!value.equals("null")) { // Allow null values for STRING
                        throw new IllegalArgumentException("Expected a STRING for column " + column.getName() + ", but got: " + value);
                    }
                }
            }
        }
    }
}

