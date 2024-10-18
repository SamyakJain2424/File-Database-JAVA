//package com.example.demo.service;
//
//import com.example.demo.table.Column;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class FileDatabaseService {
//    private static final String METADATA_FILE = "metadata.txt";
//    private static final String DATA_FILE = "table_data.txt";
//
//    public void createTable(String tableName, List<Column> columns) throws IOException {
//        // Clear the data file when a new table is created
//        try (BufferedWriter dataWriter = new BufferedWriter(new FileWriter(DATA_FILE))) {
//            dataWriter.write(""); // Clear the file
//        }
//
//        // Write metadata to the file in a structured plain text format
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(METADATA_FILE))) {
//            writer.write("Table Name: " + tableName);
//            writer.newLine();
//            for (Column column : columns) {
//                writer.write("Column: " + column.getName() + ", Type: " + column.getType());
//                writer.newLine();
//            }
//        }
//    }
//
//    public void insertData(List<String> orderedValues) throws IOException {
//        // Write data entries to the table data file in the correct column order
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
//            writer.write(String.join(",", orderedValues));
//            writer.newLine();
//        }
//    }
//
//    public boolean tableExists(String tableName) throws IOException {
//        try (BufferedReader reader = new BufferedReader(new FileReader(METADATA_FILE))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("Table Name: " + tableName)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public List<Column> getTableColumns(String tableName) throws IOException {
//        List<Column> columns = new ArrayList<>();
//        boolean foundTable = false;
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(METADATA_FILE))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("Table Name: " + tableName)) {
//                    foundTable = true;
//                    continue;
//                }
//                if (foundTable && line.startsWith("Column: ")) {
//                    String[] parts = line.split(", Type: ");
//                    String columnName = parts[0].split(": ")[1];
//                    String columnType = parts[1];
//                    columns.add(new Column(columnName, columnType));
//                }
//                if (foundTable && !line.startsWith("Column: ")) {
//                    break; // Stop reading after all columns are read
//                }
//            }
//        }
//
//        if (!foundTable) {
//            throw new IllegalArgumentException("Table " + tableName + " not found in metadata");
//        }
//
//        return columns;
//    }
//}














package com.example.demo.service;

import com.example.demo.table.Column;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDatabaseService {
    private static final String METADATA_FILE = "metadata.txt";
    private static final String DATA_FILE = "table_data.txt";

    // Create the table metadata
    public void createTable(String tableName, List<Column> columns) throws IOException {
        // Clear the data file on table creation
        clearDataFile();

        // Write metadata
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            writer.write("Table Name: " + tableName);
            writer.newLine();
            for (Column column : columns) {
                writer.write("Column: " + column.getName() + ", Type: " + column.getType());
                writer.newLine();
            }
        }
    }

    // Insert data into the data file
    public void insertData(String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    // Check if the table exists
    public boolean tableExists() {
        File file = new File(METADATA_FILE);
        return file.exists();
    }

    // Get the table metadata
    public List<Column> getTableMetadata() throws IOException {
        List<Column> columns = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(METADATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Column:")) {
                    String[] parts = line.split(",");
                    String name = parts[0].split(":")[1].trim();
                    String type = parts[1].split(":")[1].trim();
                    columns.add(new Column(name, type));
                }
            }
        }
        return columns;
    }

    // Clear the data file on table creation
    private void clearDataFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            writer.write(""); // Clear the file
        }
    }
}
