package cn.edu.yibinu.crm.workbench.mapper;

import cn.edu.yibinu.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int insert(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int insertSelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    ClueRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int updateByPrimaryKeySelective(ClueRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_remark
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int updateByPrimaryKey(ClueRemark record);

    /**
     * 根据线索id插入市场活动备注信息
     * @param clueRemark 备注
     * @return
     */
    int insertClueRemark(ClueRemark clueRemark);

    /**
     * 查询线索备注列表
     * @return 返回的就是一个列表
     */
    List<ClueRemark> selectClueRemarkList(String clueId);

    /**
     * 通过线索id查询出线索备注列表，没有连接查询
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemarkListForConvert(String clueId);
}




















