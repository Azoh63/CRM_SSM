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

import java.util.*;

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

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;

    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;

    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranRemarkMapper tranRemarkMapper;

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

        //先去查询出线索备注列表
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkListForConvert(clue.getId());
        CustomerRemark customerRemark = null;
        ContactsRemark contactsRemark = null;
        List<CustomerRemark> customerRemarkList = null;
        List<ContactsRemark> contactsRemarkList = null;

        //不为空时再进行判断会加大使用效率
        if (clueRemarkList != null && clueRemarkList.size() > 0){
            //插入语句可能会导致异常
            try{
                customerRemarkList = new ArrayList<>();
                contactsRemarkList = new ArrayList<>();
                //遍历备注
                for (ClueRemark clueRemark : clueRemarkList){
                    //将每一个遍历到的备注添加进数据库表中
                    customerRemark = new CustomerRemark();
                    customerRemark.setId(UUIDUtils.getUUID());
                    customerRemark.setEditFlag(clueRemark.getEditFlag());
                    customerRemark.setCustomerId(customer.getId());
                    customerRemark.setCreateBy(sessionUser.getId());
                    customerRemark.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
                    customerRemark.setNoteContent(clueRemark.getNoteContent());
                    customerRemark.setEditBy(clueRemark.getEditBy());
                    customerRemark.setEditTime(clueRemark.getEditTime());

                    //添加进客户备注list当中
                    customerRemarkList.add(customerRemark);

                    //添加到联系人备注list当中
                    contactsRemark = new ContactsRemark();
                    contactsRemark.setId(UUIDUtils.getUUID());
                    contactsRemark.setContactsId(contacts.getId());
                    contactsRemark.setCreateBy(sessionUser.getId());
                    contactsRemark.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
                    contactsRemark.setEditBy(clueRemark.getEditBy());
                    contactsRemark.setEditFlag(clueRemark.getEditFlag());
                    contactsRemark.setEditTime(clueRemark.getEditTime());
                    contactsRemark.setNoteContent(clueRemark.getNoteContent());
                    contactsRemarkList.add(contactsRemark);
                }
                //插入一个客户备注列表
                customerRemarkMapper.insertCustomerRemark(customerRemarkList);
                //插入一个联系人备注列表
                contactsRemarkMapper.insertContactsRemark(contactsRemarkList);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        //去查询线索和市场活动列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectRelationByClueId(clue.getId());
        ContactsActivityRelation contactsActivityRelation = null;
        List<ContactsActivityRelation> contactsActivityRelationList = null;
        if (clueActivityRelationList != null && clueActivityRelationList.size() > 0){
            contactsActivityRelationList = new ArrayList<>();
            try{
                for (ClueActivityRelation relation : clueActivityRelationList){
                    contactsActivityRelation = new ContactsActivityRelation();
                    contactsActivityRelation.setId(UUIDUtils.getUUID());
                    contactsActivityRelation.setContactsId(contacts.getId());
                    contactsActivityRelation.setActivityId(relation.getActivityId());

                    //将relation加入列表中
                    contactsActivityRelationList.add(contactsActivityRelation);
                }
                //正式插入数据
                contactsActivityRelationMapper.insertRelation(contactsActivityRelationList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //现在就是观察需不需要创建交易
        String isCreateTran = (String) map.get("isCreateTran");
        if("true".equals(isCreateTran)){
            try{
                //说明需要创建，去获取交易的信息
                Tran tran = new Tran();
                tran.setId(UUIDUtils.getUUID());
                tran.setMoney((String) map.get("money"));
                tran.setName((String) map.get("name"));
                tran.setExpectedDate((String) map.get("expectedDate"));
                tran.setStage((String) map.get("stage"));
                tran.setSource((String) map.get("source"));
                tran.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
                tran.setCreateBy(sessionUser.getId());
                tran.setCustomerId(customer.getId());
                tran.setContactsId(contacts.getId());
                tran.setCreateBy(sessionUser.getId());
                tran.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
                tranMapper.insertTran(tran);

                //同时把线索备注信息添加到交易备注信息中去
                List<TranRemark> tranRemarkList = new ArrayList<>();
                TranRemark tranRemark = null;
                if(clueRemarkList != null && clueRemarkList.size() > 0){
                    for (ClueRemark clueRemark : clueRemarkList){
                        tranRemark = new TranRemark();
                        tranRemark.setId(UUIDUtils.getUUID());
                        tranRemark.setCreateBy(clueRemark.getCreateBy());
                        tranRemark.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
                        tranRemark.setEditBy(clueRemark.getEditBy());
                        tranRemark.setEditFlag(clueRemark.getEditFlag());
                        tranRemark.setNoteContent(clueRemark.getNoteContent());
                        tranRemark.setEditTime(clueRemark.getEditTime());
                        tranRemark.setTranId(tran.getId());
                        //列表传入mapper
                        tranRemarkList.add(tranRemark);
                    }
                    //循环结束，开始传值
                    tranRemarkMapper.insertTranRemarkList(tranRemarkList);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        try{
            if(clueActivityRelationList != null && clueActivityRelationList.size() > 0){
                //全部都转换完成了，删除市场活动和线索的联系
                clueActivityRelationMapper.deleteRelationByClueId(clue.getId());
            }
            //删除线索备注
            if (clueRemarkList != null && clueRemarkList.size() > 0){
                clueRemarkMapper.deleteRemarkByClueId(clue.getId());
            }
            //线索肯定是存在的，不需要判断是否存在，直接就可以删除
            clueMapper.deleteClueById(clue.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}























