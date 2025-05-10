package com.example.blanza;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class SQLLoader {
    private static final String SQL_FILE_PATH = "../sql/expense_queries.sql";
    private static final HashMap<String, String> queries = new HashMap<>();

    static {
        try (BufferedReader reader = new BufferedReader(new FileReader(SQL_FILE_PATH))) {
            String line;
            String key = null;
            StringBuilder value = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("-- @")) {
                    if (key != null && value.length() > 0) {
                        queries.put(key, value.toString().trim());
                        value.setLength(0);
                    }
                    key = line.substring(4).trim();
                } else if (!line.trim().startsWith("--")) {
                    value.append(line).append("\n");
                }
            }
            if (key != null && value.length() > 0) {
                queries.put(key, value.toString().trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String queryName) {
        return queries.get(queryName);
    }
}
