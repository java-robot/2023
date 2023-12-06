package org.nju.demo.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Properties;

public class OssUtil {

    public static String readLinesInRange(String filePath, int startLine, int endLine) throws IOException {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int currentLine = 0;

            while ((line = br.readLine()) != null) {
                currentLine++;
                if (currentLine >= startLine && currentLine <= endLine) {
                    result.append(line).append("\n");
                }
                if (currentLine > endLine) {
                    break;  // 读取完指定范围的行后退出循环
                }
            }
        }

        return result.toString();
    }
    public static void downloadFileFromOSS(String objectKey, String localFilePath) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = null;
        fis = new FileInputStream("src/main/resources/oss-config.properties");
        properties.load(fis);
        String endpoint = properties.getProperty("endpoint");
        String accessKeyId = properties.getProperty("accessKeyId");
        String accessKeySecret = properties.getProperty("accessKeySecret");
        String bucketName = properties.getProperty("bucketName");

        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 指定要下载的文件所在的 Bucket 名称和文件路径
        OSSObject ossObject = ossClient.getObject(bucketName, objectKey);

        // 获取文件流
        ObjectMetadata meta = ossObject.getObjectMetadata();

        // 下载文件
        ossClient.getObject(new com.aliyun.oss.model.GetObjectRequest(bucketName, objectKey), new File(localFilePath));

        // 关闭OSSClient
        ossClient.shutdown();
    }


    public static InputStream getFileContentFromOSS(String bucketName, String objectKey) throws IOException {
        // 创建 OSS 客户端
        OSS ossClient = createOss();
        try {
            // 获取文件的元数据
            SimplifiedObjectMeta objectMeta = ossClient.getSimplifiedObjectMeta(bucketName, objectKey);

            if (objectMeta != null) {
                // 获取文件内容
                OSSObject ossObject = ossClient.getObject(bucketName, objectKey);
                return ossObject.getObjectContent();
            } else {
                System.out.println("Object not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return null;
    }


    public static void upload(String codeName,MultipartFile[] files) throws IOException {
        OSS ossClient = createOss();
        String bucketName = getBucketName();
        try {
            for (MultipartFile file : files) {
                String objectName =  codeName + "/"+file.getOriginalFilename(); // 定义每个文件在 OSS 中的对象名，这里使用了一个前缀
                InputStream inputStream = file.getInputStream();

                // 创建 PutObjectRequest
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);

                // 执行上传
                ossClient.putObject(putObjectRequest);
            }
        } finally {
            ossClient.shutdown();
        }
    }

    public static OSS createOss() throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = null;
        fis = new FileInputStream("src/main/resources/oss-config.properties");
        properties.load(fis);

        String endpoint = properties.getProperty("endpoint");
        String accessKeyId = properties.getProperty("accessKeyId");
        String accessKeySecret = properties.getProperty("accessKeySecret");
        String bucketName = properties.getProperty("bucketName");

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        return ossClient;
    }
    public static String getBucketName() throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = null;
        fis = new FileInputStream("src/main/resources/oss-config.properties");
        properties.load(fis);
        String bucketName = properties.getProperty("bucketName");
        return bucketName;
    }

    public static void main(String[] args) throws IOException {
        String objectKey = "bb8f4562ab9d4284bde3c59a00c67693/xxl-job/xxl-job-1.3.1.xml";   // 替换成你的文件在 OSS 中的路径
        downloadFileFromOSS(objectKey,"ASTs/temp.txt");
    }
}
