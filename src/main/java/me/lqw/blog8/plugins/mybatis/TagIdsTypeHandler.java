package me.lqw.blog8.plugins.mybatis;

import me.lqw.blog8.model.Tag;
import me.lqw.blog8.util.StringUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 标签 id 处理器
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
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

    /**
     * s => set
     *
     * @param s s
     * @return Set
     */
    public Set<Tag> get(String s) {
        if (StringUtil.isBlank(s)) {
            return new HashSet<>();
        }
        return Arrays.stream(s.split(",")).distinct().map(Integer::parseInt).map(Tag::new)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
