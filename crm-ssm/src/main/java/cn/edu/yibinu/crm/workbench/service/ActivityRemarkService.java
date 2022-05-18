package cn.edu.yibinu.crm.workbench.service;

import cn.edu.yibinu.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> queryActivityRemarkByActivityId(String id);

    int saveActivityRemark(ActivityRemark remark);

    int deleteActivityRemarkById(String id);

    int editActivityRemark(ActivityRemark remark);
}
