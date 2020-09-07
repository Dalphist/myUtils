package util;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author DJF
 * @version 0.1.0
 * @Description 根据git基线对比 抽取编译后文件，java文件按class文件抽出
 * @create 2020-06-03 14:22
 * @since 0.1.0
 **/
public class ExportClassFileList {
    private static File gitRepoFile;        //git库文件夹
    private static String sourceCodePath;   //源码所在路径
    private static String desPath;          //抽取代码的目标路径
    private static String incrementFilePath;     //增量文件所在路径
    private static String git = "git";      //git 命令
    //private static String git = "/opt/yusys/git/bin/git";
    private static String classPath;        //class文件相对路径

    public static void main(String[] args) throws Exception {

        classPath = "target\\dev_cis\\WEB-INF\\classes\\";
        sourceCodePath = "D:\\temp\\TCTS\\";
        incrementFilePath = "D:\\temp_increment\\dev_cis";
        String currentTag = "8.0";
        String lastTag = "7.0";
//        sourceCodePath = System.getProperty("sourceCodePath");

//        String currentTag = System.getProperty("currentTag");
//        String lastTag = System.getProperty("lastTag");

        System.out.println(sourceCodePath);
        System.out.println(incrementFilePath);
        System.out.println(currentTag);
        System.out.println(lastTag);
        gitRepoFile = new File(sourceCodePath);

        //获取两个基线对应的commitId
        String currentCommitId = getCommitIdFromTag(currentTag);
        String lastCommitId = getCommitIdFromTag(lastTag);

        deleteFolder(incrementFilePath);    //删除原增量目录
//        initDelShellFile();
        moveChangedFiles(currentCommitId, lastCommitId, incrementFilePath);
    }


    private static void moveChangedFiles(String currentCommitId, String lastCommitId, String incrementSourceCodePath) throws Exception {
        String cmd = git + " diff  --name-status \"" + lastCommitId + "\" \"" + currentCommitId + "\"";
        BufferedReader commitBr = getNewBufferedReader(cmd);
        String commitLine = null;
        while ((commitLine = commitBr.readLine()) != null) {
            if (commitLine.startsWith("R100")) { // 改名
                String[] arr = commitLine.split("\t");
                String fileFullName = trans(arr[1]);
                String addFilePath = arr[1]; // 重命名前的文件
                writeIntoDelShell(addFilePath);
                String delFilePath = arr[2]; // 重命名后的文件
//                copyFile(delFilePath, incrementSourceCodePath);
            } else if (commitLine.startsWith("D")) { // 删除
                String delFilePath = commitLine.split("\t")[1];
//                writeIntoDelShell(delFilePath);
            } else { // 新增和修改
                String addFilePath = commitLine.split("\t")[1];
                trans(addFilePath);
//                copyFile(trans(addFilePath), incrementSourceCodePath);
            }
        }
    }

    //将源码文件路径转为编译后路径
    private static String trans(String fileName) throws IOException {
        //判断系统
        String srcPath = "src\\main\\";
        String separator = "\\";
        boolean isWindows = fileName.indexOf(srcPath) > 0;
        if(!isWindows){
            srcPath = "src/main/";
            separator = "/";
        }
        String classFilePath;
        if (fileName.indexOf(separator + "java") != -1) {//对java源文件目录下的文件处理

            if (fileName.endsWith(".java")) {
                classFilePath = fileName.replace(".java", ".class").replace(srcPath + "java", classPath);    //编译后
                String classfileFullName = classPath + classFilePath;
                String desFileNameStr = incrementFilePath + separator + classFilePath;
                File desFilePath = new File(desFileNameStr);
//            if (!desFilePath.exists()) {
//                desFilePath.mkdirs();
//            }
                Files.copy(new File(classfileFullName).toPath(), desFilePath.toPath());
                System.out.println(fileName + "复制完成");
            }
        }
// else if (fullFileName.indexOf("/resource") != -1) {//对resource文件的处理
//            String fileName = fullFileName;
//            fullFileName = classPath + fileName;
//
//            String tempDesPath = fileName.substring(0, fileName.lastIndexOf("/"));
//            String desFilePathStr = desPath + "/" + version + "/WEB-INF" + tempDesPath.replace("src/main/resources", "/classes/");
//            String desFileNameStr = desPath + "/" + version + "/WEB-INF" + fileName.replace("src/main/resources", "/classes/");
//            File desFilePath = new File(desFilePathStr);
//            if (!desFilePath.exists()) {
//                desFilePath.mkdirs();
//            }
//            copyFile(fullFileName, desFileNameStr);
//            System.out.println(fullFileName + "复制完成");
//        } else if (fullFileName.indexOf("/webapp/WEB-INF/views") != -1) {//对web应用文件的处理
//            String fileName = fullFileName;
//            fullFileName = classPath + fileName;
//
//            String tempDesPath = fileName.substring(0, fileName.lastIndexOf("/"));
//            String desFilePathStr = desPath + "/" + version + "/WEB-INF/" + tempDesPath.replace("src/main/webapp/WEB-INF/", "");
//            String desFileNameStr = desPath + "/" + version + "/WEB-INF/" + fileName.replace("src/main/webapp/WEB-INF/", "");
//            File desFilePath = new File(desFilePathStr);
//            if (!desFilePath.exists()) {
//                desFilePath.mkdirs();
//            }
//            copyFile(fullFileName, desFileNameStr);
//            System.out.println(fullFileName + "复制完成");
//        } else {//对静态资源文件的处理
//            String fileName = fullFileName;
//            fullFileName = classPath + fileName;
//
//            String tempDesPath = fileName.substring(0, fileName.lastIndexOf("/"));
//            String desFilePathStr = desPath + "/" + version + tempDesPath.replace("src/main/webapp", "");
//            String desFileNameStr = desPath + "/" + version + fileName.replace("src/main/webapp", "");
//            File desFilePath = new File(desFilePathStr);
//            if (!desFilePath.exists()) {
//                desFilePath.mkdirs();
//            }
//            copyFile(fullFileName, desFileNameStr);
//            System.out.println(fullFileName + "复制完成");
//        }
        return "";
    }



    private static String getCommitIdFromTag(String currentTag) throws Exception {
        String cmd = git + " show " + currentTag + " --pretty=tformat:%h";
        BufferedReader commitBr = getNewBufferedReader(cmd);
        return commitBr.readLine(); // 第一行即是commitId
    }

    private static BufferedReader getNewBufferedReader(String cmd) throws IOException {
		String[] str = { "cmd", "/C", cmd }; // windows
//        String[] str = {"/bin/sh", "-c", cmd}; //linux
        Process p = Runtime.getRuntime().exec(str, null, gitRepoFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf-8"));
        return br;
    }


    // 删除文件夹
    public static void deleteFolder(String folder) throws IOException {
        Path start = Paths.get(folder);
        if (Files.notExists(start)) {
            return;
        }

        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override //构成了一个内部类
            // 处理文件
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            // 再处理目录
            public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw e;
                }
            }
        });
        System.out.println("删除成功！");
    }

    public static void writeIntoDelShell(String content) {
        String delShellPath = incrementFilePath + "delShell.sh";
        File file = new File(delShellPath);
        FileWriter writer = null;
        BufferedWriter bw = null;
        try {
            writer = new FileWriter(file, true);
            bw = new BufferedWriter(writer);
            content = getRealPath(content);
            bw.write("rm " + content.replaceAll("\r\n", "") + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 处理开发路径和部署路径的修改
    private static String getRealPath(String content) {
        if (content.startsWith("WebContent/")) {
            content = content.substring("WebContent/".length());
        }
        if (content.startsWith("src")) {
            content = content.replace("src", "WEB-INF/classes").replace(".java", ".class");
        }
        return content;
    }

    private static void initDelShellFile() {
        String delShellPath = incrementFilePath + "delShell.sh";
        File file = new File(delShellPath);
        FileWriter writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new FileWriter(file);
            writer.write("#!/bin/bash" + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
