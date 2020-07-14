package me.lqw.blog8.mapper;

import me.lqw.blog8.model.Moment;
import me.lqw.blog8.model.MomentArchive;
import me.lqw.blog8.model.vo.MomentQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MomentMapper {


    void insert(Moment moment);

    Integer count(MomentQueryParam queryParam);

    List<Moment> selectPage(MomentQueryParam queryParam);

    Optional<Moment> findById(@Param("id") Integer id);

    void update(Moment moment);

    MomentArchive selectLatestMoments();

    int countMomentArchive();

    List<MomentArchive> selectMomentArchivePage(MomentQueryParam queryParam);

    void increaseHits(@Param("id") Integer id, @Param("hits") int hits);
}
