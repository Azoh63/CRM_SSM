package cn.edu.yibinu.crm.workbench.mapper;

import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    /**
     * 插入线索和市场活动联系的信息
     * @param carList 线索市场活动联系的对象
     * @return
     */
    int insertClueActivityRelation(List<ClueActivityRelation> carList);

    /**
     * 查询框模糊查询
     * @param name
     * @param clueId
     * @return
     */
    List<Activity> selectActivityByNameForRelation(@Param("name") String name, @Param("clueId") String clueId);

    /**
     * 通过线索的id查出relation的list
     * @return
     */
    List<ClueActivityRelation> selectRelationByClueId(String clueId);

    /**
     * 根据线索的id删除相应的和市场活动的联系
     * @param clueId
     * @return
     */
    int deleteRelationByClueId(String clueId);
}