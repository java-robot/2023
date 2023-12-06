package org.nju.demo.utils;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.nd4j.common.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nju.demo.entity.IssueBasic;
import org.nju.demo.service.IssueService;
import org.nju.demo.service.impl.IssueServiceImpl;

import static org.nju.demo.utils.CNNUtil.extractFeatures;

public class CodeToVector {

//    static IssueService issueService = new IssueServiceImpl();
    public static double[] averageWordVectors(List<double[]> wordVectors) {
        if (wordVectors.isEmpty()) {
            // 如果没有单词向量可用，则返回空向量或者null，视情况而定
            return new double[0]; // 空向量示例
        }

        int vectorSize = wordVectors.get(0).length;
        double[] averageVector = new double[vectorSize];

        for (double[] vector : wordVectors) {
            for (int i = 0; i < vectorSize; i++) {
                averageVector[i] += vector[i];
            }
        }

        // 计算平均向量
        for (int i = 0; i < vectorSize; i++) {
            averageVector[i] /= wordVectors.size();
        }

        return averageVector;
    }


    public static List<String> tokenizeCodeSnippet(String codeSnippet) {
        // 这里可以使用不同的分词技术，比如简单的按空格分割或更复杂的词法分析器
        // 以下是一个简单的示例，根据空格分割代码段
        return Arrays.asList(codeSnippet.split("\\s+"));
    }

    public static void tarinModel(String txtPath,String modelPath) throws FileNotFoundException {
        // 读取代码段数据，将每个代码段作为一行

        File file = new File(txtPath);

        SentenceIterator iter = new BasicLineIterator(file);
        // 接下来可以使用 iter 进行文件内容的迭代处理

        // 配置分词器工厂
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();

        // 用于存储分词后的文本行
        StringBuilder allLines = new StringBuilder();

        // 对每一行文本进行分词处理
        while (iter.hasNext()) {
            String line = iter.nextSentence();
            allLines.append(line).append(" "); // 可能需要根据具体情况调整连接方式
        }

        // 将分词后的文本转换为字符串
        String allText = allLines.toString();

        System.out.println(allText);

        // 对整个文本进行分词处理
        tokenizerFactory.create(allText);

        // 设置 Word2Vec 模型参数
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(5)
                .epochs(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(tokenizerFactory)
                .build();



        // 训练 Word2Vec 模型
        vec.fit();

        // 保存训练好的模型
        WordVectorSerializer.writeWord2VecModel(vec, modelPath);

    }

    public static double[] readDoublesFromFile(String filePath) {
        List<Double> doublesList;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);

            // 将每一行的字符串转换为double，并存储在List<Double>中
            doublesList = lines.stream()
                    .map(Double::parseDouble)
                    .collect(Collectors.toList());

            // 将List<Double>转换为double[]
            double[] doublesArray = new double[doublesList.size()];
            for (int i = 0; i < doublesList.size(); i++) {
                doublesArray[i] = doublesList.get(i);
            }

            return doublesArray;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            // 出现异常时返回空数组或者可以进行适当的异常处理
            return new double[0];
        }
    }

    public static double[] getVec(String text,String modelPath) throws IOException {
        File file = new File(modelPath);

        Word2Vec vec1 = WordVectorSerializer.readWord2Vec(file);

        // 将新的代码段转换为向量表示
        List<String> words = tokenizeCodeSnippet(text);
        List<double[]> wordVectors = new ArrayList<>();
        for (String word : words) {
            if (vec1.hasWord(word)) {
                wordVectors.add(vec1.getWordVector(word));
            }
        }
        // 对代码段的向量进行平均（或其他方式）
        double[] codeVector = averageWordVectors(wordVectors);
        System.out.println(Arrays.toString(codeVector));
        return codeVector;
    }

    public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
        RealVector a = new ArrayRealVector(vectorA);
        RealVector b = new ArrayRealVector(vectorB);

        // 计算余弦相似度
        double similarity = (a.dotProduct(b)) / (a.getNorm() * b.getNorm());

        return similarity;
    }

    public static double[] getVecWithModel(String text) throws IOException {
        return getVec(text,"deepLearning/model.zip");
    }

    public static double[][] reshapeVectorToMatrix(double[] vector) {

        double[][] matrix = new double[10][10];
        int row = 0, col = 0;

        for (double value : vector) {
            matrix[row][col] = value;
            col++;
            if (col == 10) {
                col = 0;
                row++;
            }
        }

        return matrix;
    }





//    public static void main(String[] args) throws IOException {
//        System.out.println(Arrays.toString(readDoublesFromFile("deepLearning/feature.txt")));
//    }



}
