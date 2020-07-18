package me.lqw.blog8.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * Mybatis 拦截器
 * @author liqiwen
 * @version 1.0
 *
 * 用于监控执行的 sql 中 update 和 delete 没有 where 条件的情况
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class }) })
public class BatchUpdateOrDeleteForbiddenPlugins implements Interceptor {


    private final static String WHRER_TAG = "where";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (isUpdateOrDeleteMethod(invocation)) {
            invocation.getArgs()[0] = checkDangerSql(invocation);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }


    private Object checkDangerSql(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation
                .getArgs()[0];
        Object parameter = invocation.getArgs()[1];
        mappedStatement.getSqlSource().getBoundSql(parameter);
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        if (!boundSql.getSql().contains(WHRER_TAG)) {
            throw new Throwable("该更新语句没有 where 条件，不允许执行");
        }
        return getMappedStatement(mappedStatement, boundSql);
    }

    private Object getMappedStatement(MappedStatement mappedStatement, BoundSql boundSql) {
        final BoundSql newBoundSql = new BoundSql(
                mappedStatement.getConfiguration(),boundSql.getSql(),
                boundSql.getParameterMappings(), boundSql.getParameterObject());


        MappedStatement.Builder builder = new MappedStatement.Builder(
                mappedStatement.getConfiguration(), mappedStatement.getId(),
                parameterObject -> newBoundSql, mappedStatement.getSqlCommandType());


        builder.cache(mappedStatement.getCache());
        builder.fetchSize(mappedStatement.getFetchSize());
        builder.flushCacheRequired(mappedStatement.isFlushCacheRequired());
        builder.keyGenerator(mappedStatement.getKeyGenerator());
        builder.resource(mappedStatement.getResource());
        builder.resultMaps(mappedStatement.getResultMaps());
        builder.resultSetType(mappedStatement.getResultSetType());
        builder.statementType(mappedStatement.getStatementType());
        builder.timeout(mappedStatement.getTimeout());
        builder.useCache(mappedStatement.isUseCache());
        return builder.build();
    }



    private boolean isUpdateOrDeleteMethod(Invocation invocation) {
        if (invocation.getArgs()[0] instanceof MappedStatement) {
            MappedStatement mappedStatement = (MappedStatement) invocation
                    .getArgs()[0];
            return SqlCommandType.UPDATE.equals(mappedStatement
                    .getSqlCommandType()) || SqlCommandType.DELETE.equals(mappedStatement.getSqlCommandType());
        }
        return false;
    }


    @Override
    public void setProperties(Properties properties) {

    }
}
