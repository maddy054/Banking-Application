package persistance;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class QueryBuilder {
	
	private String url = "jdbc:mysql://localhost:3306/ZBank";
	private String userName = "root";
	private String password = "";

    private String tableName;
    private List<Integer> uesdColumns;
    private List<Integer> whereConditions;
    private boolean limit = false;
    private boolean offset = false;
    private boolean between = false;
    private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, userName, password);
	}

    public QueryBuilder(String tableName) {
        this.tableName = tableName;
        this.uesdColumns = new ArrayList<>();
        this.whereConditions = new ArrayList<>();
    }

    public QueryBuilder column(int... columns) {
        for (int column : columns) {
        	uesdColumns.add(column);
        }
        return this;
    }
 
    public QueryBuilder where(int... conditions) {
    	
    	for(int condition : conditions) {
    		whereConditions.add(condition);
    	}
        
        return this;
    }
 
    public QueryBuilder limit() {
    	limit = true;
    	return this;
    }
    public QueryBuilder offset() {
    	offset = true;
    	return this;
    }
    public QueryBuilder between() {
    	between = true;
    	return this;
    }
    public String buildSelect() throws SQLException {
        StringBuilder query = new StringBuilder("SELECT ");
        
       System.out.println("used "+uesdColumns);
      
        if (uesdColumns.isEmpty()) {
        	
            query.append("*");
        } else {
        	
        	 List<String> selectColumns = getColumnNames(uesdColumns);
        	 
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
            System.out.println("Where column "+getColumnNames(whereConditions));
            for (int i = 0; i < getColumnNames(whereConditions).size(); i++) {
                query.append(getColumnNames(whereConditions).get(i)).append(" = ? ");
                if (i < whereConditions.size() - 1) {
                    query.append(" AND ");
                }
            }
        }
        if(between) {
        	query.append(" AND BETWEEN ? AND ? ");
        }
        if(limit) {
        	query.append(" LIMIT ? ");
        }
        if(offset) {
        	query.append(" OFFSET ? ");
        }
        System.out.println(query);

        return query.toString();
    }


    public String buildUpdate() throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
        List<String> updateColumns = getColumnNames(this.uesdColumns);
        
        for (int i = 0; i < updateColumns.size(); i++) {
            query.append(updateColumns.get(i)).append(" = ?");
            if (i < updateColumns.size() - 1) {
                query.append(", ");
            }
        }

        if (!whereConditions.isEmpty()) {
            query.append(" WHERE ");
            for (int i = 0; i < whereConditions.size(); i++) {
                query.append(getColumnNames(whereConditions).get(i)).append(" = ? ");
                if (i < whereConditions.size() - 1) {
                    query.append(" AND ");
                }
            }
        }
        System.out.println(query);
        return query.toString();
    }

    public String buildInsert() throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName);
        System.out.println("used column "+uesdColumns);
        List<String> insertColumns = getColumnNames(uesdColumns);
        System.out.println("insert column "+insertColumns);
        
        if(!insertColumns.isEmpty()) {
        	query.append(" ( ");
        	
        	 for (int i = 0; i < insertColumns.size(); i++) {
                 query.append(insertColumns.get(i));
                 if (i < insertColumns.size() - 1) {
                     query.append(", ");
                 }
             }
        	 query.append(") ");
        }
 
        query.append(" VALUES (");

        for (int i = 0; i < insertColumns.size(); i++) {
            query.append("?");
            if (i < insertColumns.size() - 1) {
                query.append(", ");
            }
        }

        query.append(")");
        System.out.println(query);
        return query.toString();
    }
    
    public <V> List<String> getColumnNames(List<V> column) throws SQLException {
    	
    	List<String> columnNames = new ArrayList<String>();
        try(Connection connection = getConnection()) {
        	DatabaseMetaData metaData = connection.getMetaData();
            
        	 try (ResultSet rs = metaData.getColumns("ZBank", null, tableName, null)) {
        		 
    
        		 if(column.isEmpty()) {
        			 while(rs.next()) {
        				 columnNames.add(rs.getString("COLUMN_NAME")); 
        			 }
        		 }else {
        			 int count =1;
        			 while (rs.next()) {
        				 if(column.contains(count)) {
        					 
        					 columnNames.add(rs.getString("COLUMN_NAME")); 
        				 }
        				 count = count+1;
        			 }
        		 }
        		return columnNames;
            }
        }
    }
	public  List<Map<String, Object>> executeQuery(String query,Object... values) throws SQLException  {
	

		  List<Map<String,Object>> resultList = new ArrayList<>();
		 
	    	try (Connection connection = getConnection()) {
	    		PreparedStatement statement = connection.prepareStatement(query);
	    		setValues(statement, values);
	    			
	    		ResultSet resultSet = statement.executeQuery() ;
	    		ResultSetMetaData metaData = resultSet.getMetaData();
	    		int columnCount = metaData.getColumnCount();
	   			System.out.println("column count "+columnCount);
	   			
	   			while (resultSet.next()) {
	   				
	   				Map<String,Object> resultMap = new HashMap<>();
    				
    				for (int i = 1; i <= columnCount; i++) {
    					resultMap.put(metaData.getColumnName(i), resultSet.getObject(i));
	   				}
    				resultList.add(resultMap);
	   			}
          return resultList;
	    	}    
		}
	
	public void execute(String query, Object...values ) throws SQLException {
		try (Connection connection = getConnection()) {
    		PreparedStatement statement = connection.prepareStatement(query);
    		setValues(statement, values);
    		statement.execute();
		}
	}
	
	private void setValues(PreparedStatement statement,Object[] values) throws SQLException {
		
		for(int i=1;i <= values.length;i++) {
			System.out.println("values "+values[i-1]);
		
			statement.setObject(i,values[i-1]);
		}
	}
}
