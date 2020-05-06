package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class ExportFileList {
	private static String sourcePath;
	private static String targetPath;
	private static String listFilePath;

	public static void main(String[] args) throws Exception {
		sourcePath = "D:\\temp\\CDM";
		targetPath = "D:\\temp\\CDM1";
		listFilePath = "D:\\1.txt";
		
//		sourcePath = System.getProperty("sourcePath");
//		targetPath = System.getProperty("targetPath");
//		listFilePath = System.getProperty("listFilePath");
		delAllFile(targetPath); 
		moveChangedFiles();
	}

	// 删除路径下所有文件
	private static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);
				delFolder(path + "/" + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); 
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private static void moveChangedFiles()	throws Exception {
		File file = new File(listFilePath);
	    BufferedReader reader = null;
	    String tempString = null;
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        int count = 0;
	        while ((tempString = reader.readLine()) != null) {
	        	//获取相对路径
	        	String relativePath = tempString.substring(sourcePath.length());
	        	File tempFile = new File(tempString);
	        	if(tempFile.isDirectory()) {
	        		continue;
	        	}
	        	copyFile(relativePath, targetPath);
	        	System.out.println("导出文件：" + sourcePath + relativePath + "\t 至 " + targetPath);
	        	count ++;
	        }
	        System.out.println("共计导出 " + count + "个文件。");
	        reader.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally{
	        if(reader != null){
	            try {
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	private static void copyFile(String relativePath, String targetPath) throws Exception {
		File sourceFile = new File(sourcePath + relativePath);
		if (!sourceFile.exists()) {
			throw new RuntimeException(sourcePath + relativePath + " 文件不存在！");
		}
		FileInputStream input = new FileInputStream(sourceFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);

		File targetFile = new File(targetPath + relativePath);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		FileOutputStream output = new FileOutputStream(targetFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);

		byte[] b = new byte[1024 * 5];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		outBuff.flush();

		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
	}
}
