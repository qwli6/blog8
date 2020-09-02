package me.lqw.blog8.service;

import me.lqw.blog8.exception.AbstractBlogException;
import me.lqw.blog8.exception.LogicException;
import me.lqw.blog8.mapper.BlackIpMapper;
import me.lqw.blog8.model.BlackIp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 黑名单业务处理实现
 *
 * @author liqiwen
 * @version 1.0
 * @since 1.0
 */
@Service
public class BlackIpService extends AbstractBaseService<BlackIp> {

    /**
     * 黑名单持久化 Mapper
     */
    private final BlackIpMapper blackIpMapper;

    /**
     * 构造方法注入
     *
     * @param blackIpMapper blackIpMapper
     */
    public BlackIpService(BlackIpMapper blackIpMapper) {
        this.blackIpMapper = blackIpMapper;
    }

    /**
     * 根据 ip 查找黑名单
     *
     * @param ip ip
     * @return BlackIp
     * @throws LogicException LogicException
     */
    @Transactional(readOnly = true)
    public Optional<BlackIp> selectByIp(String ip) throws AbstractBlogException {
        return blackIpMapper.selectByIp(ip);
    }

    /**
     * 保存黑名单
     *
     * @param blackIp blackIp
     * @return BlackIp
     * @throws LogicException 逻辑异常
     *                        1. 黑名单存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public BlackIp save(BlackIp blackIp) throws AbstractBlogException {

        blackIpMapper.selectByIp(blackIp.getIp()).ifPresent(e -> {
            throw new LogicException("blackIp.exists", "黑名单已经存在");
        });
        //根据 ip 解析地址
        blackIp.setAddress("");

        blackIpMapper.insert(blackIp);

        return blackIp;
    }

    /**
     * 根据 id 删除黑名单
     *
     * @param id id
     * @throws LogicException 逻辑异常
     *                        1. 黑名单不存在异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delete(Integer id) throws AbstractBlogException {
        blackIpMapper.selectById(id).orElseThrow(() ->
                new LogicException("blackIp.delete.notExists", "黑名单不存在"));
        blackIpMapper.deleteById(id);
    }

    /**
     * 更新黑名单操作
     *
     * @param blackIp blackIp
     * @throws LogicException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void update(BlackIp blackIp) throws AbstractBlogException {
        BlackIp old = blackIpMapper.selectById(blackIp.getId()).orElseThrow(()
                -> new LogicException("blackIp.notExists", "黑名单不存在"));
        if (old.getIp().equals(blackIp.getIp())) {
            throw new LogicException("blackIp.sameIp", "更新的内容相同");
        }

        blackIpMapper.update(blackIp);
    }

    /**
     * 获取所有的黑名单
     *
     * @return list
     */
    @Transactional(readOnly = true)
    public List<BlackIp> selectAll() {
        return blackIpMapper.selectAll();
    }

    /**
     * 根据 ip 删除黑名单
     *
     * @param ip ip
     * @throws LogicException 逻辑异常
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteByIp(String ip) throws AbstractBlogException {
        BlackIp blackIp = blackIpMapper.selectByIp(ip).orElseThrow(()
                -> new LogicException("blackIpService.ip.notExists", "黑名单不存在"));
        blackIpMapper.deleteById(blackIp.getId());
    }

}
