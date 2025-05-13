package com.example.blanza;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementSetter {
    void setParameters(PreparedStatement stmt) throws SQLException;
}
