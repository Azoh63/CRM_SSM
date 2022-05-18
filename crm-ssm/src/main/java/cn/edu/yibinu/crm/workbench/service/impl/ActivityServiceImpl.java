package cn.edu.yibinu.crm.workbench.service.impl;

import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.mapper.ActivityMapper;
import cn.edu.yibinu.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Resource
    private ActivityMapper activityMapper;

    @Override
    public int saveActivity(Activity activity) {
        //return activityMapper.insert(activity);
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String,Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryActivityByConditionForTotal(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForTotal(map);
    }

    @Override
    public int deleteActivityByIds(String[] ids) {
        return activityMapper.deleteActivityByIds(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public int editActivity(Activity activity) {
        return activityMapper.updateActivity(activity);
    }

    @Override
    public List<Activity> queryActivityList() {
        return activityMapper.selectActivityList();
    }

    @Override
    public int updateActivityListForFile(List<Activity> activityList) {
        return activityMapper.insertActivityListForFile(activityList);
    }

    @Override
    public List<Activity> queryActivityListByIds(String[] id) {
        return activityMapper.selectActivityByIds(id);
    }

    @Override
    public Activity queryActivityByIdForDetail(String id) {
        return activityMapper.selectActivityByIdForDetail(id);
    }


}
