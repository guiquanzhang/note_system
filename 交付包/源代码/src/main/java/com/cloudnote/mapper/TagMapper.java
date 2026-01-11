package com.cloudnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudnote.entity.Tag;
import com.cloudnote.vo.TagVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 标签Mapper
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    
    /**
     * 获取用户的标签列表（包含使用次数）
     */
    @Select("SELECT t.tag_id, t.name, t.color, COUNT(nt.note_id) as count " +
            "FROM tag t " +
            "LEFT JOIN note_tag nt ON t.tag_id = nt.tag_id " +
            "WHERE t.user_id = #{userId} " +
            "GROUP BY t.tag_id, t.name, t.color " +
            "ORDER BY count DESC, t.created_at DESC")
    List<TagVO> selectTagsWithCount(@Param("userId") Integer userId);
    
    /**
     * 根据名称查询标签
     */
    @Select("SELECT * FROM tag WHERE user_id = #{userId} AND name = #{name}")
    Tag selectByName(@Param("userId") Integer userId, @Param("name") String name);
}
