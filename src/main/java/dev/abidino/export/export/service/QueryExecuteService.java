package dev.abidino.export.export.service;

import dev.abidino.export.export.api.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryExecuteService {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<List<Object>> executeQuery(String query, List<Filter> filterList) {
        if (!isQuerySafe(query)) {
            throw new RuntimeException("query is unsafe : " + query);
        }

        Map<String, Object> filterMap = filterList.stream().collect(Collectors.toMap(Filter::key, Filter::value));
        MapSqlParameterSource parameters = new MapSqlParameterSource(filterMap);

        return jdbcTemplate.query(query, parameters, (ResultSet rs) -> {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<List<Object>> rows = new ArrayList<>();
            while (rs.next()) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                rows.add(row);
            }
            return rows;
        });
    }

    public Long executeCountQuery(String query, List<Filter> filterList) {
        if (!isQuerySafe(query)) {
            throw new RuntimeException("query is unsafe : " + query);
        }

        Map<String, Object> filterMap = filterList.stream().collect(Collectors.toMap(Filter::key, Filter::value));
        String optimizedQuery = removeOrderByClause(query);
        String countQuery = "SELECT COUNT(*) FROM (" + optimizedQuery + ") AS count_query";

        return jdbcTemplate.queryForObject(countQuery,filterMap, Long.class);
    }

    private boolean isQuerySafe(String query) {
        String lowerCaseQuery = query.toLowerCase();
        boolean containsProhibitedWords = (
                lowerCaseQuery.contains("delete") ||
                lowerCaseQuery.contains("drop") ||
                lowerCaseQuery.contains("alter") ||
                lowerCaseQuery.contains("--") ||
                lowerCaseQuery.contains(";")
        );

        return lowerCaseQuery.startsWith("select") && !containsProhibitedWords;
    }

    private String removeOrderByClause(String query) {
        int orderByIndex = query.toLowerCase().lastIndexOf("order by");
        if (orderByIndex != -1) {
            return query.substring(0, orderByIndex).trim();
        }
        return query;
    }
}
