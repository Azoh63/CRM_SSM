package cn.edu.yibinu.crm.UUID;

import org.junit.Test;

import java.util.UUID;

public class UUIDTest {
    @Test
    public void UUIDUtils(){
        System.out.println( UUID.randomUUID().toString().replaceAll("-",""));
    }
}
