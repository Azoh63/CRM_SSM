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

        //??????????????????????????????
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

        //??????????????????
        customerMapper.insertCustomer(customer);

        //??????????????????
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
        //?????????????????????
        contactsMapper.insertContacts(contacts);

        //?????????????????????????????????
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkListForConvert(clue.getId());
        CustomerRemark customerRemark = null;
        ContactsRemark contactsRemark = null;
        List<CustomerRemark> customerRemarkList = null;
        List<ContactsRemark> contactsRemarkList = null;

        //????????????????????????????????????????????????
        if (clueRemarkList != null && clueRemarkList.size() > 0){
            //?????????????????????????????????
            try{
                customerRemarkList = new ArrayList<>();
                contactsRemarkList = new ArrayList<>();
                //????????????
                for (ClueRemark clueRemark : clueRemarkList){
                    //??????????????????????????????????????????????????????
                    customerRemark = new CustomerRemark();
                    customerRemark.setId(UUIDUtils.getUUID());
                    customerRemark.setEditFlag(clueRemark.getEditFlag());
                    customerRemark.setCustomerId(customer.getId());
                    customerRemark.setCreateBy(sessionUser.getId());
                    customerRemark.setCreateTime(DateFormatUtils.dateTimeFormat(new Date()));
                    customerRemark.setNoteContent(clueRemark.getNoteContent());
                    customerRemark.setEditBy(clueRemark.getEditBy());
                    customerRemark.setEditTime(clueRemark.getEditTime());

                    //?????????????????????list??????
                    customerRemarkList.add(customerRemark);

                    //????????????????????????list??????
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
                //??????????????????????????????
                customerRemarkMapper.insertCustomerRemark(customerRemarkList);
                //?????????????????????????????????
                contactsRemarkMapper.insertContactsRemark(contactsRemarkList);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        //????????????????????????????????????
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

                    //???relation???????????????
                    contactsActivityRelationList.add(contactsActivityRelation);
                }
                //??????????????????
                contactsActivityRelationMapper.insertRelation(contactsActivityRelationList);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //??????????????????????????????????????????
        String isCreateTran = (String) map.get("isCreateTran");
        if("true".equals(isCreateTran)){
            try{
                //?????????????????????????????????????????????
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

                //????????????????????????????????????????????????????????????
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
                        //????????????mapper
                        tranRemarkList.add(tranRemark);
                    }
                    //???????????????????????????
                    tranRemarkMapper.insertTranRemarkList(tranRemarkList);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        try{
            if(clueActivityRelationList != null && clueActivityRelationList.size() > 0){
                //???????????????????????????????????????????????????????????????
                clueActivityRelationMapper.deleteRelationByClueId(clue.getId());
            }
            //??????????????????
            if (clueRemarkList != null && clueRemarkList.size() > 0){
                clueRemarkMapper.deleteRemarkByClueId(clue.getId());
            }
            //??????????????????????????????????????????????????????????????????????????????
            clueMapper.deleteClueById(clue.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}























