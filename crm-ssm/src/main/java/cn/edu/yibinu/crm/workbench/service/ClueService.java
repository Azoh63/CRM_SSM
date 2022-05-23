package cn.edu.yibinu.crm.workbench.service;

import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<DicValue> queryClueDicValueByTypeCode(String typeCode);

    int addClue(Clue clue);

    List<Clue> queryClueByConditionForPage(Map map);

    int queryClueByConditionForTotal();

    Clue queryClueForDetail(String id);
}
