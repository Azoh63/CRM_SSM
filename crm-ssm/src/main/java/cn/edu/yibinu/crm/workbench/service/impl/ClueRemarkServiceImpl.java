package cn.edu.yibinu.crm.workbench.service.impl;

import cn.edu.yibinu.crm.workbench.domain.ClueRemark;
import cn.edu.yibinu.crm.workbench.mapper.ClueRemarkMapper;
import cn.edu.yibinu.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("clueRemarkServiceImpl")
public class ClueRemarkServiceImpl implements ClueRemarkService {
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    @Override
    public int addClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.insertClueRemark(clueRemark);
    }

    @Override
    public List<ClueRemark> queryClueRemarkList(String clueId) {
        return clueRemarkMapper.selectClueRemarkList(clueId);
    }
}
