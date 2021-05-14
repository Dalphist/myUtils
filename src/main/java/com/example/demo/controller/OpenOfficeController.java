package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.openoffice.FileUtils;
import com.example.demo.openoffice.OpenOfficeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-05-07 15:39
 * @since 0.1.0
 **/
@RestController
public class OpenOfficeController {
    public static final DateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat(
            "yyyyMM");


    @Autowired
    private OpenOfficeConverter openOfficeConverter;


    @Value("${fileUploadPath}")
    private String fileUploadPath ;


    @RequestMapping(value = "/o_docUpload", method = RequestMethod.POST)
    public String docUpload(@RequestParam(value = "Filedata", required = false) MultipartFile file) {
        JSONObject data = new JSONObject();
        String origName=file.getOriginalFilename();
        // TODO 检查允许上传的后缀

        //先把文件上传到服务器
        String extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + extName;
        //文件所在绝对路径 上传路径和文件名
        String path = fileUploadPath + fileName;
        File toFile=new File(path);
        if (!toFile.getParentFile().exists()){
            //文件夹不存在，先创建文件夹
            toFile.getParentFile().mkdirs();
        }
        try {
            //进行文件复制上传
            FileCopyUtils.copy(file.getInputStream(), new FileOutputStream(toFile));
        } catch (Exception e) {
            //上传失败
            e.printStackTrace();
        }

        //这个是word文档图片存放的路径
        String docImgPath=fileUploadPath+generateMonthname()+"/";
        openOfficeConverter.setFilePath(docImgPath);
        path = path.replace("\\", "/");
        try {
            File outFile = openOfficeConverter.convert(path, OpenOfficeConverter.HTML);
            String html = FileUtils.toHtmlString(outFile);
            String txt = FileUtils.clearFormat(FileUtils.subString(html, "<HTML>", "</HTML>"), docImgPath);
            System.out.println(txt);
            data.put("status", 0);
            data.put("txt", txt);
            data.put("title", origName);
            return  data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            data.put("status", 1);
        }
        return "";
    }


    /**
     * 根据月份生成文件夹名称
     * @return
     */
    public static String generateMonthname() {
        return YEAR_MONTH_FORMAT.format(new Date());
    }
}
