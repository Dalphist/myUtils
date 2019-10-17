package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
* Title: FilesToFile  
* Description:把多个文件的内容整合到一个文件上
* @author djf  
* @date 2019年4月19日
 */
public class FilesToFile {
	
	private static File targetFile;
	
	public static void main(String[] args) {
		String targetFilePath = "C:\\Users\\djf\\Desktop\\a.txt";
		String sourcePath = "C:\\work\\git\\cis\\src\\main\\java\\com\\yusys";
		targetFile = new File(targetFilePath);
		new File(sourcePath);
		traverseFolder(sourcePath);
	}
	
	public static void traverseFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (null == files || files.length == 0) {
                return;
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                        System.out.println("文件:" + file2.getAbsolutePath());
                        excuteCopy(file2);
                    }
                }
            }
        } 
    }
	
	public static List<String> excuteCopy(File sourceFile) {
		List<String> list = new ArrayList<String>();
		FileReader reader = null;
		BufferedReader br = null;
		try {
			reader = new FileReader(sourceFile);
			br = new BufferedReader(reader);
			String str = null;
			while ((str = br.readLine()) != null) {
				write(str,targetFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static void write(String content,File file) {
		FileWriter writer = null;
		BufferedWriter bw = null;
		try {
			writer = new FileWriter(file, true);
			bw = new BufferedWriter(writer);
			bw.write(content + "\r\n");
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
}
