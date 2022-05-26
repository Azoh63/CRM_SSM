package cn.edu.yibinu.crm.workbench.service.impl;

import cn.edu.yibinu.crm.commons.utils.Contents;
import cn.edu.yibinu.crm.commons.utils.DateFormatUtils;
import cn.edu.yibinu.crm.commons.utils.UUIDUtils;
import cn.edu.yibinu.crm.settings.domain.DicValue;
import cn.edu.yibinu.crm.settings.domain.User;
import cn.edu.yibinu.crm.settings.mapper.DicValueMapper;
import cn.edu.yibinu.crm.workbench.domain.*;
import cn.edu.yibinu.crm.workbench.mapper.*;
import cn.edu.yibinu.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private DicValueMapper dicValueMapper;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

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

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public void doConvertClue(Map<String, Object> map) {
        Clue clue = (Clue) map.get("clue");
        Customer customer = new Customer();
        User sessionUser = (User) map.get(Contents.SESSION_USER);

        //将线索中客户信息转换
        customer.setId(UUIDUtils.getUUID());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setOwner(clue.getOwner());
        customer.setName(clue.getCompany());
        customer.setDescription(clue.getDescription());
        customer.setCreateBy(sessionUser.getId());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customer.setAddress(clue.getAddress());
        customer.setEditBy(clue.getEditBy());
        customer.setEditTime(clue.getEditTime());

        //首先新建客户
        customerMapper.insertCustomer(customer);

        //转换成联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setAddress(clue.getAddress());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setDescription(clue.getDescription());
        contacts.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
        contacts.setCreateBy(sessionUser.getId());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setEmail(clue.getEmail());
        contacts.setAppellation(clue.getAppellation());
        contacts.setFullname(clue.getFullname());
        contacts.setCustomerId(customer.getId());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setEditBy(clue.getEditBy());
        contacts.setEditTime(clue.getEditTime());
        //新建一个联系人
        contactsMapper.insertContacts(contacts);

        //先去查询粗线索备注列表
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkListForConvert(clue.getId());
        //遍历备注
        for (ClueRemark clueRemark : clueRemarkList){
            //将每一个遍历到的备注添加进数据库表中

        }
    }
}























