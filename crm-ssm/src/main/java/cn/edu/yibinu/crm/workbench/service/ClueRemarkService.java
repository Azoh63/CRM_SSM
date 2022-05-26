package cn.edu.yibinu.crm.workbench.service;

import cn.edu.yibinu.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    int addClueRemark(ClueRemark clueRemark);

    List<ClueRemark> queryClueRemarkList(String clueId);
}
