package cn.edu.yibinu.crm.workbench.mapper;

import cn.edu.yibinu.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri May 06 17:32:54 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri May 06 17:32:54 CST 2022
     */
    int insert(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri May 06 17:32:54 CST 2022
     */
    int insertSelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri May 06 17:32:54 CST 2022
     */
    Activity selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri May 06 17:32:54 CST 2022
     */
    int updateByPrimaryKeySelective(Activity record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity
     *
     * @mbggenerated Fri May 06 17:32:54 CST 2022
     */
    int updateByPrimaryKey(Activity record);

    /**
     * 插入一个Activity信息
     * @param activity 从表单获取的对象
     * @return
     */
    int insertActivity(Activity activity);

    /**
     * 根据查询条件查询市场活动列表
     * @param map 从前端传来的6个数据
     * @return 市场活动列表
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 查出市场活动的总条数
     * @param map 传进来的前端参数
     * @return  返回总条数
     */
    int selectActivityByConditionForTotal(Map<String,Object>map);

    /**
     * 根据返回的id数组删除对应的市场活动
     * @param ids id
     * @return 删除的状态码
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查询单个市场活动
     * @param id
     * @return
     */
    Activity selectActivityById(String id);

    /**
     * 根据传过来的实体类对象修改数据库中的数据
     * @param activity
     * @return 影响记录条数
     */
    int updateActivity(Activity activity);

    /**
     * 返回数据库的所有市场活动
     * @return
     */
    List<Activity> selectActivityList();

    /**
     * 通过选择xls文件导入市场活动
     * @param activityList
     * @return
     */
    int insertActivityListForFile(List<Activity> activityList);

    /**
     * 通过选择的一组id数组来查询市场活动列表
     * @param id
     * @return
     */
    List<Activity> selectActivityByIds(String[] id);

    /**
     * 通过id找到响应的详细列表
     * @param id
     * @return
     */
    Activity selectActivityByIdForDetail(String id);
}























