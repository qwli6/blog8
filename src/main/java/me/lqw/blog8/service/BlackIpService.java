package me.lqw.blog8.service;

import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.BlackIpMapper;
import me.lqw.blog8.model.BlackIp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BlackIpService implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());


    private final BlackIpMapper blackIpMapper;

    public BlackIpService(BlackIpMapper blackIpMapper) {
        this.blackIpMapper = blackIpMapper;
    }


    @Transactional(readOnly = true)
    public Optional<BlackIp> findByIp(String ip) throws LogicException {
        return blackIpMapper.findByIp(ip);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public int save(BlackIp blackIp) throws LogicException {

        blackIpMapper.findByIp(blackIp.getIp()).ifPresent(e -> {
            throw new LogicException("blackIp.exists", "黑名单已经存在");
        });
        //根据 ip 解析地址
        blackIp.setAddress("");

        blackIpMapper.insert(blackIp);

        return blackIp.getId();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Integer id) throws LogicException {
        blackIpMapper.findById(id).orElseThrow(() ->
                new LogicException("blackIp.delete.notExists", "黑名单不存在"));
        blackIpMapper.delete(id);
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("BlackIpService afterPropertiesSet()...");
    }
}
