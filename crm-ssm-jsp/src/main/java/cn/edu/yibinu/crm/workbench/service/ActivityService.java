package cn.edu.yibinu.crm.workbench.service;

import cn.edu.yibinu.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int saveActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String,Object> map);

    int queryActivityByConditionForTotal(Map<String,Object> map);

    int deleteActivityByIds(String[] ids);

    Activity queryActivityById(String id);

    int editActivity(Activity activity);

    List<Activity> queryActivityList();

    int updateActivityListForFile(List<Activity> activityList);

    List<Activity> queryActivityListByIds(String[] id);

    Activity queryActivityByIdForDetail(String id);
}



