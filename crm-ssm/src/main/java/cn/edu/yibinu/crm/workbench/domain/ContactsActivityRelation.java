package cn.edu.yibinu.crm.workbench.domain;

public class ContactsActivityRelation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_contacts_activity_relation.id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_contacts_activity_relation.contacts_id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    private String contactsId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_contacts_activity_relation.activity_id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    private String activityId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_contacts_activity_relation.id
     *
     * @return the value of tbl_contacts_activity_relation.id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_contacts_activity_relation.id
     *
     * @param id the value for tbl_contacts_activity_relation.id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_contacts_activity_relation.contacts_id
     *
     * @return the value of tbl_contacts_activity_relation.contacts_id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    public String getContactsId() {
        return contactsId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_contacts_activity_relation.contacts_id
     *
     * @param contactsId the value for tbl_contacts_activity_relation.contacts_id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    public void setContactsId(String contactsId) {
        this.contactsId = contactsId == null ? null : contactsId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_contacts_activity_relation.activity_id
     *
     * @return the value of tbl_contacts_activity_relation.activity_id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_contacts_activity_relation.activity_id
     *
     * @param activityId the value for tbl_contacts_activity_relation.activity_id
     *
     * @mbggenerated Thu May 26 16:58:08 CST 2022
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId == null ? null : activityId.trim();
    }
}