package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.OperateLogMapper;
import me.lqw.blog8.model.OperateLog;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志业务实现类
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Service
public class OperateLogService extends AbstractBaseService<OperateLog> {

    /**
     * 操作日志 Mapper
     */
    private final OperateLogMapper operateLogMapper;

    /**
     * 构造方法注入
     *
     * @param operateLogMapper operateLogMapper
     */
    public OperateLogService(OperateLogMapper operateLogMapper) {
        this.operateLogMapper = operateLogMapper;
    }

    /**
     * 获取最近的几条操作记录
     *
     * @param count count
     * @return list
     */
    public List<OperateLog> selectLatestOperateLogs(int count) {
        return operateLogMapper.selectLatestOperateLogs(count);
    }

    /**
     * 保存操作日志
     *
     * @param operateLog operateLog
     * @return OperateLog
     * @throws LogicException LogicException
     */
    @Override
    public OperateLog save(OperateLog operateLog) throws LogicException {
        operateLogMapper.insert(operateLog);
        return operateLog;
    }

    /**
     * 删除操作日志
     *
     * @param id id id
     * @throws LogicException LogicException
     */
    @Override
    public void delete(Integer id) throws LogicException {
        OperateLog dbLog = operateLogMapper.selectById(id).orElseThrow(()
                -> new LogicException("operateLog.notExists", "操作日志不存在"));
        operateLogMapper.delete(dbLog.getId());
    }

    @Deprecated
    @Override
    public void update(OperateLog operateLog) throws LogicException {
        //操作日志不存在更新
    }
}
