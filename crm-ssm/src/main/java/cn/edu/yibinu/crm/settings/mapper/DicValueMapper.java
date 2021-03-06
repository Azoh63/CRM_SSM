package cn.edu.yibinu.crm.settings.mapper;

import cn.edu.yibinu.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sat May 21 15:24:35 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sat May 21 15:24:35 CST 2022
     */
    int insert(DicValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sat May 21 15:24:35 CST 2022
     */
    int insertSelective(DicValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sat May 21 15:24:35 CST 2022
     */
    DicValue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sat May 21 15:24:35 CST 2022
     */
    int updateByPrimaryKeySelective(DicValue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_dic_value
     *
     * @mbggenerated Sat May 21 15:24:35 CST 2022
     */
    int updateByPrimaryKey(DicValue record);

    /**
     * 根据typeCode选择出divValue列表
     * @param typeCode
     * @return
     */
    List<DicValue> selectDicValueForConvert(String typeCode);

    /**
     * tran页面请求数据字典值
     * @param typeCode
     * @return
     */
    List<DicValue> selectDicValueForTran(String typeCode);

    /**
     * 根据type_code查出所有的DicValue对象
     * @return
     */
    List<DicValue> selectAllDicValueByTypeCode(String typeCode);

}


















