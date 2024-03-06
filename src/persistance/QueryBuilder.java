package persistance;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.DatabaseMetaData;

public class QueryBuilder {

    private String tableName;
    private List<String> selectColumns;
    private List<String> whereConditions;
    private List<String> updateColumns;
    private List<String> insertColumns;
    private List<String> insertValues;

    public QueryBuilder(String tableName) {
        this.tableName = tableName;
        this.selectColumns = new ArrayList<>();
        this.whereConditions = new ArrayList<>();
        this.updateColumns = new ArrayList<>();
        this.insertColumns = new ArrayList<>();
        this.insertValues = new ArrayList<>();
    }

    public QueryBuilder select(String... columns) {
        for (String column : columns) {
            selectColumns.add(column);
        }
        return this;
    }

    public QueryBuilder update(String... columns) {
        for (String column : columns) {
            updateColumns.add(column);
        }
        return this;
    }

    public QueryBuilder insert(String... columns) {
        for (String column : columns) {
            insertColumns.add(column);
        }
        return this;
    }

    public QueryBuilder values(String... values) {
        for (String value : values) {
            insertValues.add(value);
        }
        return this;
    }

    public QueryBuilder where(String condition) {
        whereConditions.add(condition);
        return this;
    }

    public String buildSelect() {
        StringBuilder query = new StringBuilder("SELECT ");

        if (selectColumns.isEmpty()) {
            query.append("*");
        } else {
            for (int i = 0; i < selectColumns.size(); i++) {
                query.append(selectColumns.get(i));
                if (i < selectColumns.size() - 1) {
                    query.append(", ");
                }
            }
        }

        query.append(" FROM ").append(tableName);

        if (!whereConditions.isEmpty()) {
            query.append(" WHERE ");
            for (int i = 0; i < whereConditions.size(); i++) {
                query.append(whereConditions.get(i));
                if (i < whereConditions.size() - 1) {
                    query.append(" AND ");
                }
            }
        }

        return query.toString();
    }

    public String buildUpdate() {
        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        for (int i = 0; i < updateColumns.size(); i++) {
            query.append(updateColumns.get(i)).append(" = ?");
            if (i < updateColumns.size() - 1) {
                query.append(", ");
            }
        }

        if (!whereConditions.isEmpty()) {
            query.append(" WHERE ");
            for (int i = 0; i < whereConditions.size(); i++) {
                query.append(whereConditions.get(i));
                if (i < whereConditions.size() - 1) {
                    query.append(" AND ");
                }
            }
        }

        return query.toString();
    }

    public String buildInsert() {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");

        for (int i = 0; i < insertColumns.size(); i++) {
            query.append(insertColumns.get(i));
            if (i < insertColumns.size() - 1) {
                query.append(", ");
            }
        }

        query.append(") VALUES (");

        for (int i = 0; i < insertValues.size(); i++) {
            query.append("?");
            if (i < insertValues.size() - 1) {
                query.append(", ");
            }
        }

        query.append(")");

        return query.toString();
    }
    public static List<String> getColumnNames(Connection connection, String tableName) throws SQLException {
        List<String> columnNames = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                columnNames.add(rs.getString("COLUMN_NAME"));
            }
        }
        return columnNames;
    }
}
