package org.nju.demo.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.*;
import com.google.gson.Gson;
import org.nju.demo.struct.ASTNode;

public class ClangASTConverter {

    private static String UPLOADED_FOLDER = System.getProperty("user.dir");
    
    // 根据cppPath路径下的cpp文件
    public static ASTNode loadASTNodes(String cppPath,String savePath) throws IOException {
        loadAST(cppPath,savePath);
        String json = readASTFromFile(savePath);
        Gson gson = new Gson();
        ASTNode astNode = gson.fromJson(json,ASTNode.class);
//        System.out.println(astNode.getId());
        return astNode;
    }
    
    //把本地位于cppPath的cpp文件转换为json形式的AST，存储在本地的savePath处
    public static void loadAST(String cppPath,String savePath){
        String clangCommand = "clang -Xclang -ast-dump=json -fsyntax-only " + cppPath;

        try {
            // 执行Clang命令
            Process process = Runtime.getRuntime().exec(clangCommand);

            // 创建输出文件
            File outputFile = new File(savePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            // 读取Clang输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // 将每行内容写入文件
                writer.write(line);
                writer.newLine();  // 换行
            }

            // 关闭文件写入
            writer.close();

            System.out.println("AST output saved to " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //把位于filePath的json形式的AST 读取为string
    public static String readASTFromFile(String filePath) throws IOException {
        StringBuilder astText = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            astText.append(line).append("\n");
        }
        reader.close();
        return astText.toString();
    }
    
    //根据node形式的AST，寻找起始行和终止行
    public static int[] findNodeByLineNumber(ASTNode node, int targetLine) {
        if (node == null) {
            return null; // 或者返回一个特殊的标志来表示未找到
        }

        for(ASTNode astNode:node.getInner()){
            if (astNode.getLoc() != null && astNode.getRange() != null) {
                Number startLine = (Number) astNode.getLoc().get("line");
                Map<String, Object> endRange = (Map<String, Object>) astNode.getRange().get("end");

                if (startLine != null && endRange != null) {
                    Number endLine = (Number) endRange.get("line");

                    if (endLine != null && targetLine >= startLine.intValue() && targetLine <= endLine.intValue()) {
                        int[] result = new int[] { startLine.intValue(), endLine.intValue() };
                        System.out.println(Arrays.toString(result));
                        // 如果目标行在当前节点的范围内，返回起始行和终止行
                        return result;
                    }
                }
            }
        }
        return null; // 如果未找到，返回 null 或者一个特殊的标志
    }

//    public static void main(String[] args) throws IOException {
//        ASTNode node = loadASTNodes("D:\\zhuyifan\\MyAICode\\ASTs\\cppPath.c","ASTs//A.txt");
//
//        findNodeByLineNumber(node,175);
//
//    }
}
