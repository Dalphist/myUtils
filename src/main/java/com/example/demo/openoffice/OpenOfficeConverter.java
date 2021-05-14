package com.example.demo.openoffice;

import lombok.extern.slf4j.Slf4j;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.office.DefaultOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
/**
 * @author DJF
 * @version 0.1.0
 * @Description 启动类
 * @create 2021-05-07 14:55
 * @since 0.1.0
 **/
@Component
@Slf4j
public class OpenOfficeConverter {


    @Value("${com.example.demo.openoffice.officeHome}")
    public String officeHome;

    @Value("${com.example.demo.openoffice.officePort}")
    public Integer officePort;


    public void startService() {
        DefaultOfficeManagerConfiguration configuration = new DefaultOfficeManagerConfiguration();
        try {
            log.info("准备启动服务....");
            //设置OpenOffice.org安装目录
            configuration.setOfficeHome(getOfficeHome());
            //设置转换端口，默认为8100
            configuration.setPortNumber(getPort());
            //设置任务执行超时为5分钟
            configuration.setTaskExecutionTimeout(1000 * 60 * 5L);
            //设置任务队列超时为24小时
            configuration.setTaskQueueTimeout(1000 * 60 * 60 * 24L);

            officeManager = configuration.buildOfficeManager();
            //启动服务
            officeManager.start();
            log.info("office转换服务启动成功!");
        } catch (Exception ce) {
            log.error("office转换服务启动失败!详细信息:" + ce);
        }
    }

    public void stopService() {
        log.info("关闭office转换服务....");
        if (officeManager != null) {
            officeManager.stop();
        }
        log.info("关闭office转换成功!");
    }


    /**
     * 转换格式
     *
     * @param inputFile 需要转换的原文件路径
     * @param fileType  要转换的目标文件类型 html,pdf
     */
    public File convert(String inputFile, String fileType) {
        String outputFile = UploadUtils.generateFilename(getFilePath(), fileType);
        if (inputFile.endsWith(".txt")) {
            String odtFile = FileUtils.getFilePrefix(inputFile) + ".odt";
            if (new File(odtFile).exists()) {
                log.error("odt文件已存在！");
                inputFile = odtFile;
            } else {
                try {
                    FileUtils.copyFile(inputFile, odtFile);
                    inputFile = odtFile;
                } catch (FileNotFoundException e) {
                    log.error("文档不存在！");
                    e.printStackTrace();
                }
            }
        }
        OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
        File output = new File(outputFile);
        converter.convert(new File(inputFile), output);
        return output;
    }


    public void init() {
        OpenOfficeConverter coverter = new OpenOfficeConverter(officeHome, officePort);
        coverter.startService();
        this.openOfficeConverter = coverter;
    }

    public void destroy() {
        this.openOfficeConverter.stopService();
    }


    @Autowired
    private OpenOfficeConverter openOfficeConverter;
    private static OfficeManager officeManager;
    public static final String HTML = "html";
    public static final String PDF = "pdf";
    public static final String TXT = "txt";
    public static final String DOC = "doc";
    public static final String DOCX = "docx";
    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
    public static final String PPT = "ppt";
    public static final String PPTX = "pptx";
    public static final String WPS = "wps";
    private int port = 8100;
    private String filePath;


    public OpenOfficeConverter(String officeHome, int port, String filePath) {
        super();
        this.officeHome = officeHome;
        this.port = port;
        this.filePath = filePath;
    }

    public OpenOfficeConverter(String officeHome, int port) {
        super();
        this.officeHome = officeHome;
        this.port = port;
    }

    public OpenOfficeConverter() {
        super();
    }


    public String getOfficeHome() {
        return officeHome;
    }


    public int getPort() {
        return port;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
