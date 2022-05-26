package cn.edu.yibinu.crm.workbench.mapper;

import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Sat May 21 15:17:35 CST 2022
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 通过typeCode查找出响应的dicValue实体
     * @param typeCode
     * @return
     */
    List<DicValue> selectClueDicValueByDicType(String typeCode);

    /**
     * 新建一个clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 根据条件查出线索，并分页
     * @param map
     * @return
     */
    List<Clue> selectClueByConditionForPage(Map map);

    /**
     * 查询出线索的记录总条数
     * @return
     */
    int selectClueByConditionForTotal();

    /**
     * 通过线索的id查询到一个具体的线索
     * @param id
     * @return
     */
    Clue selectClueForDetail(String id);

    /**
     * 查找没有和线索联系的市场活动
     * @return
     */
    List<Activity> selectActivityForContactClue(String clueId);

    /**
     * 通过线索id找到对应线索（是给convert页面使用的clue对象）
     * @return
     */
    Clue selectClueByIdForConvert(String id);
}





















