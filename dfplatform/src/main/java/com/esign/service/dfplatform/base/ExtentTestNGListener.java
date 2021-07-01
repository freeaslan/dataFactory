package com.esign.service.dfplatform.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.ResourceCDN;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @Description:TestNG监听生成测试报告
 * @author: 侯兰东
 * @date: 2021.06.07
 */
@Slf4j
public class ExtentTestNGListener extends TestListenerAdapter {

    private ExtentReports extent;
    private ExtentTest suiteTest;

    @Override
    public void onTestFailure(ITestResult tr) {
        //用例标题
        testCaseTitle(tr);
        Throwable e = tr.getThrowable().fillInStackTrace();
        for (String testLog : Reporter.getOutput(tr)) {
            //日志信息
            suiteTest.debug(testLog);
        }
        suiteTest.log(Status.FAIL, e);
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        //用例标题
        testCaseTitle(tr);
        for (String testLog : Reporter.getOutput(tr)) {
            //日志信息
            suiteTest.skip(testLog);
        }
        suiteTest.log(Status.SKIP, "关联方法失败，跳过测试");
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        //用例标题
        testCaseTitle(tr);
        for (String testLog : Reporter.getOutput(tr)) {
            //日志信息
            suiteTest.debug(testLog);
        }
    }

    @Override
    public void onStart(ITestContext testContext) {
        //初始化报告信息
        init(testContext.getSuite().getParameter("sceneName"), testContext.getSuite().getParameter("taskId"));
    }

    @Override
    public void onFinish(ITestContext testContext) {

        //报告输出
        extent.flush();
    }

    /**
     * 初始化报告信息
     *
     * @param sceneName 场景名称
     * @param taskId    任务ID
     */
    private void init(String sceneName, String taskId) {

        //创建测试报告路径
        File report = new File("report");
        if (!report.exists() && !report.isDirectory()) {
            report.mkdir();
        }
        //报告输出路径
        final String OUTPUT_FOLDER = "report/" + sceneName + "/";
        //报告名称
        final String FILE_NAME = taskId + ".html";
        //创建文件
        File reportDir = new File(OUTPUT_FOLDER);
        if (!reportDir.exists() && !reportDir.isDirectory()) {
            boolean flag = reportDir.mkdir();
            log.info("文件创建结果：" + flag);
        }
        //报告信息设置
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(OUTPUT_FOLDER + FILE_NAME);
        htmlReporter.config().setDocumentTitle("场景请求报告");
        htmlReporter.config().setReportName("场景请求报告");
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setEncoding("utf-8");
        htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
        htmlReporter.config().setCSS(".node.level-1  ul{ display:none;} .node.level-1.active ul{display:block;}");
        htmlReporter.setAppendExisting(true);
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setReportUsesManualConfiguration(true);
    }

    /**
     * 用例标题
     *
     * @param tr
     */
    private void testCaseTitle(ITestResult tr) {

        //参数作为标题
        if (tr.getParameters().length != 0) {
            String params = tr.getParameters()[0].toString();
            if (params.equals("Y") || params.equals("N")) {
                suiteTest = extent.createTest(tr.getParameters()[1].toString());
            } else {
                suiteTest = extent.createTest(tr.getParameters()[0].toString());
            }
        } else {

            //获取方法的描述作为标题
            String description = tr.getMethod().getDescription();
            if (description != null) {
                suiteTest = extent.createTest(description);
            } else {
                //方法名做为标题
                suiteTest = extent.createTest(tr.getMethod().getMethodName());
            }
        }
    }
}