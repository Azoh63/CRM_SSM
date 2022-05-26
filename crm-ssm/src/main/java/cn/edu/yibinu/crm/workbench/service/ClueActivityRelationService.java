package cn.edu.yibinu.crm.workbench.service;

import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int addClueActivityRelation(List<ClueActivityRelation> carList);

    List<Activity> queryActivityByNameForRelation(String name,String clueId);
}
