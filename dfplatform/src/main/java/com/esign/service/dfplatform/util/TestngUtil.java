package com.esign.service.dfplatform.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.testng.TestNG;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: huangtai
 * @Description: testng工具类
 * @Date: 2020/9/24 16:38
 */
public class TestngUtil {

    /**
     * 创建testng.xml
     *
     * @param taskId
     * @param sceneName
     * @param className
     */
    public static void createTestngXml(String taskId, String sceneName, String className) {

        Document document = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("suite");
        document.setRootElement(root);
        root.addAttribute("name", "automation");
        root.addAttribute("parallel", "Automatically generate xml");
        root.addAttribute("thread-count", String.valueOf("1"));

        Element taskIdParam = root.addElement("parameter");
        taskIdParam.addAttribute("name", "taskId");
        taskIdParam.addAttribute("value", taskId);

        Element sceneNameParam = root.addElement("parameter");
        sceneNameParam.addAttribute("name", "sceneName");
        sceneNameParam.addAttribute("value", sceneName);

        Element test = root.addElement("test");
        test.addAttribute("name", "test");

        Element classes = test.addElement("classes");
        Element classNode = classes.addElement("class");
        classNode.addAttribute("name", className);

        Element listeners = root.addElement("listeners");  //子标签
        Element listener = listeners.addElement("listener");
        listener.addAttribute("class-name", "com.esign.service.dfplatform.base.ExtentTestNGListener");

        OutputFormat format = new OutputFormat("    ", true);
        XMLWriter xmlWrite2;
        try {

            xmlWrite2 = new XMLWriter(new FileOutputStream(taskId + ".xml"), format);
            xmlWrite2.write(document);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * 执行testng.xml
     *
     * @param taskId
     */
    public static void executeTestngXml(String taskId) {

        //执行testng.xml
        TestNG testNG = new TestNG();
        List<String> suites = new ArrayList<String>();
        suites.add(taskId + ".xml");
        testNG.setTestSuites(suites);
        testNG.run();
    }
}
