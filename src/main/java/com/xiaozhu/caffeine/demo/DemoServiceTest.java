package com.xiaozhu.caffeine.demo;

import com.xiaozhu.caffeine.annotation.CacheEvict;
import com.xiaozhu.caffeine.annotation.Cacheable;
import com.xiaozhu.caffeine.common.CacheInstance;

import com.xiaozhu.caffeine.model.Student;
import com.xiaozhu.caffeine.model.StudentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@Service
public class DemoServiceTest {
    @Cacheable(cacheName = CacheInstance.STUDENT_INFO,  //枚举类存放的缓存名
            cacheNameSuffix = "selectStudentList",//缓存前缀, 对这部分缓存的唯一标识, 这里可以使用方法名, 方便查找和删除
            keys= {"conditon"})						//只需要将参数condition作为缓存key
    public StudentVO selectStudentList(Student conditon, Class cls){
        //业务代码
        HashMap<String,Object> vlaueMap = new HashMap<String,Object>();
        vlaueMap.put("{\"conditon\":{\"name\":\"testzyt-1\"}}",conditon);
        StudentVO studentVO = StudentVO.builder().name(conditon.getName()).id(conditon.getId()).build();
        return studentVO;
    }

    @CacheEvict(cacheName = CacheInstance.STUDENT_INFO,  //枚举类存放的缓存名
            cacheNameSuffix = "selectStudentList")	//缓存前缀, 清除该标识下的所有缓存
    public void delCache(){
        //业务代码
        log.info("本地缓存删除成功！！");
    }
}
