package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Tag;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(Set.class)
public class TagIdsTypeHandler extends BaseTypeHandler<Set<Tag>> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i,
                                    Set<Tag> tags, JdbcType jdbcType) throws SQLException {

        throw new RuntimeException("unsupport mybatis type");

    }

    @Override
    public Set<Tag> getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return get(resultSet.getString(s));
    }

    @Override
    public Set<Tag> getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return get(resultSet.getString(i));
    }

    @Override
    public Set<Tag> getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        throw new RuntimeException("unsupport mybatis type");
    }

    public Set<Tag> get(String s){
        if(StringUtils.isEmpty(s)){
            return new HashSet<>();
        }
        return Arrays.stream(s.split(",")).distinct().map(Integer::parseInt).map(Tag::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
