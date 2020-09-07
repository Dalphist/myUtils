package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2020-05-22 11:28
 * @since 0.1.0
 **/
public class CopyFile {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        String srcFolder = "D:\\data";
        String destFolder = "D:\\qq";
        final Path srcPath = Paths.get(srcFolder);
        final Path destPath = Paths.get(destFolder, srcPath.toFile().getName());
        if (Files.notExists(srcPath)) {
            System.err.println("源文件夹不存在");
            System.exit(1);
        }
        // 如果目标目录不存在，则创建
        if (Files.notExists(destPath)) {
            Files.createDirectories(destPath);
        }

        Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {
            @Override
            // 文件处理，将文件夹也一并处理，简洁些
            public FileVisitResult visitFile(Path file,BasicFileAttributes attrs) throws IOException {
                Path dest = destPath.resolve(srcPath.relativize(file));
                // 如果说父路径不存在，则创建
                if (Files.notExists(dest.getParent())) {
                    Files.createDirectories(dest.getParent());
                }
                Files.copy(file, dest);
                return FileVisitResult.CONTINUE;
            }
        });
        long endTime = System.currentTimeMillis();
        System.out.println("复制成功!耗时：" + (endTime - startTime) + "ms");
    }

    // 删除文件夹
    public static void deleteFolder(String folder) throws IOException {
        Path start = Paths.get(folder);
        if (Files.notExists(start)) {
            throw new IOException("文件夹不存在！");
        }

        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override //构成了一个内部类
            // 处理文件
            public FileVisitResult visitFile(Path file,BasicFileAttributes attrs) throws IOException {
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


}
