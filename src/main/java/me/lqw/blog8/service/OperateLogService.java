package me.lqw.blog8.service;

import me.lqw.blog8.mapper.OperateLogMapper;
import me.lqw.blog8.model.OperateLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateLogService implements InitializingBean {

    private final OperateLogMapper operateLogMapper;

    public OperateLogService(OperateLogMapper operateLogMapper) {
        this.operateLogMapper = operateLogMapper;
    }


    public List<OperateLog> selectLatestOperateLogs(int count){
        return operateLogMapper.selectLatestOperateLogs(count);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
