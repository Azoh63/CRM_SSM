package cn.edu.yibinu.crm;

import org.junit.Test;

import java.util.ResourceBundle;

public class BundleTest {
    @Test
    public void testBundle(){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString("确定决策者");
        System.out.println("可能性" + possibility);
    }
}
