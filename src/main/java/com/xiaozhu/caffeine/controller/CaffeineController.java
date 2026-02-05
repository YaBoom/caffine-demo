package com.xiaozhu.caffeine.controller;

import com.xiaozhu.caffeine.demo.DemoServiceTest;
import com.xiaozhu.caffeine.model.ResponseData;
import com.xiaozhu.caffeine.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class CaffeineController {

    @Autowired
    private DemoServiceTest demoServiceTest;

    /**
     * 查询
     * @param student
     * @return
     */
    @RequestMapping(value = "/getStr")
    @ResponseBody
    public ResponseData getStrInfo(Student student) {
        ResponseData respData = new ResponseData();
        respData.setCode(200);
        //Student student =  Student.builder().name("testzyt-1").build();
        respData.setData(demoServiceTest.selectStudentList(student,Student.class));
        respData.setRespTime(new Date());
        return respData;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/delStrInfo")
    public ResponseData delStrInfo() {
        ResponseData respData = new ResponseData();
        respData.setCode(200);
        demoServiceTest.delCache();
        respData.setData(true);
        respData.setRespTime(new Date());
        return respData;
    }
}
