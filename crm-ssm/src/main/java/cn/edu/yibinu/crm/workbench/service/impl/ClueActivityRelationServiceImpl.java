package cn.edu.yibinu.crm.workbench.service.impl;

import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.ClueActivityRelation;
import cn.edu.yibinu.crm.workbench.mapper.ClueActivityRelationMapper;
import cn.edu.yibinu.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueActivityRelationServiceImpl")
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int addClueActivityRelation(List<ClueActivityRelation> carList) {
        return clueActivityRelationMapper.insertClueActivityRelation(carList);
    }

    @Override
    public List<Activity> queryActivityByNameForRelation(String name, String clueId) {
        return clueActivityRelationMapper.selectActivityByNameForRelation(name,clueId);
    }
}
