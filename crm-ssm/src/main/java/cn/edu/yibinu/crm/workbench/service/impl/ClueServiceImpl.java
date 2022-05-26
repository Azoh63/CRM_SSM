package cn.edu.yibinu.crm.workbench.service.impl;

import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.mapper.DicValueMapper;
import cn.edu.yibinu.crm.workbench.domain.Activity;
import cn.edu.yibinu.crm.workbench.domain.Clue;
import cn.edu.yibinu.crm.workbench.domain.ClueActivityRelation;
import cn.edu.yibinu.crm.workbench.mapper.ActivityMapper;
import cn.edu.yibinu.crm.workbench.mapper.ClueMapper;
import cn.edu.yibinu.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private DicValueMapper dicValueMapper;

    @Autowired
    private ActivityMapper activityMapper;

    public List<DicValue> queryClueDicValueByTypeCode(String typeCode) {
        return clueMapper.selectClueDicValueByDicType(typeCode);
    }

    @Override
    public int addClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryClueByConditionForTotal() {
        return clueMapper.selectClueByConditionForTotal();
    }

    @Override
    public Clue queryClueForDetail(String id) {
        return clueMapper.selectClueForDetail(id);
    }

    @Override
    public List<Activity> queryActivityForContactClue(String clueId) {
        return clueMapper.selectActivityForContactClue(clueId);
    }

    @Override
    public Clue queryClueByIdForConvert(String id) {
        return clueMapper.selectClueByIdForConvert(id);
    }

    @Override
    public List<DicValue> queryDicValueForConvert(String typeCode) {
        return dicValueMapper.selectDicValueForConvert(typeCode);
    }

    @Override
    public List<Activity> queryActivityByNameForConvert(String activityName) {
        return activityMapper.selectActivityByNameForConvert(activityName);
    }

    @Override
    public Activity queryActivityById(String activityId) {
        return activityMapper.selectActivityById(activityId);
    }
}
