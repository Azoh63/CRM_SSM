package cn.edu.yibinu.crm.activity;

import cn.edu.yibinu.crm.workbench.service.ActivityService;
import cn.edu.yibinu.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ActivityTest {
    /**
     * 这个测试不能单独使用，因为activityMapper对象使用的是自动注入，所以要启动项目用Controller去调用
     */
    @Test
    public void testSelectQueryActivity(){
        ActivityService service = new ActivityServiceImpl();
        Map<String,Object> map = new HashMap<>();
        map.put("name","测试市场活动1");
        service.queryActivityByConditionForPage(map);
    }
}
