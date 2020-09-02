package me.lqw.blog8.job;

import me.lqw.blog8.mapper.OperateLogMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * 清除操作日志 job
 *
 * @author liqiwen
 * @version 1.2
 * @since 1.2
 */
@Component
public class ClearOperateLogJob {

    /**
     * 操作日志 Mapper
     */
    private final OperateLogMapper operateLogMapper;

    /**
     * 构造方法注入
     *
     * @param operateLogMapper 操作日志 Mapper
     */
    public ClearOperateLogJob(OperateLogMapper operateLogMapper) {
        this.operateLogMapper = operateLogMapper;
    }


    @Scheduled(cron = "* 1/2 * * * ?")
    public void clearOperateLogs() {

        LocalDate current = LocalDate.now();
        LocalDate preDate = current.plusDays(1);

//        operateLogMapper.delete(LocalDateTime.of(preDate.getYear(), preDate.getMonthValue()+1, preDate.getDayOfMonth(), 23, 59, 59));
    }
}
