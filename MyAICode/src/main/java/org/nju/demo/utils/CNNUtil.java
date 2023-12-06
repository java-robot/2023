package org.nju.demo.utils;

import org.deeplearning4j.datasets.iterator.utilty.ListDataSetIterator;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nju.demo.dao.IssueBasicMapper;
import org.nju.demo.entity.IssueBasic;
import org.nju.demo.entity.IssueBasicExample;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CNNUtil {

//    public static void tarinYourModel(String modelPath,double[][] wordVectors) throws IOException {
//
//        // 准备数据集（这里仅为示例，你需要根据自己的数据集来加载和准备数据）
//        DataSet yourDataSet = prepareYourDataSet(wordVectors);
//
//        int batchSize = 32;
//
//        // 将数据集转换为迭代器
//        DataSetIterator dataSetIterator = new ListDataSetIterator<>(yourDataSet.asList(), batchSize);
//
//        // 指定模型文件的路径
//        File locationToLoad = new File(modelPath);
//
//        // 加载模型
//        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(locationToLoad);
//
//        // 设置模型的监听器，用于输出训练过程中的评估指标
//        model.setListeners(new ScoreIterationListener(10)); // 每迭代10次输出一次评估结果
//
//        // 进行模型的训练
//        int numEpochs = 10; // 设定训练的轮数
//        for (int i = 0; i < numEpochs; i++) {
//            model.fit(dataSetIterator); // 使用数据集进行模型训练
//        }
//    }
//
//    public static DataSet prepareYourDataSet(double[][] wordVectors) {
//
//
//        // 将 wordVectors 转换为 INDArray 对象
//        org.nd4j.linalg.api.ndarray.INDArray features = Nd4j.create(wordVectors);
//
//        // 创建 DataSet 对象并返回
//        return new DataSet(features, null);
//    }

    // 使用 CNN 模型提取特征
    public static double[] extractFeatures(double[][][] wordVectors) throws IOException {
        int batchSize =1;
        int channels = wordVectors[0][0].length;
        // 将 n 个 100 维的词向量转换为 INDArray 格式
        INDArray input = Nd4j.create(wordVectors).reshape(batchSize, channels, 10, 10);


        // 加载模型
        MultiLayerNetwork restoredModel = buildYourModel(channels);


        // 将 input 输入到 CNN 模型中进行前向传播，得到特征
        INDArray features = restoredModel.output(input);

        double[] res = new double[100];
        for(int i=0;i<100;i++){
            res[i] = features.getDouble(i);
        }

        // 打印特征向量的形状（示例）
//        System.out.println("Features shape: " + features.shapeInfoToString());
        return res;
    }


    public static MultiLayerNetwork buildYourModel(int channels) throws IOException {

        int minibatch = 2;
        int height = 10;
        int width = 10;

        InputType inputType = InputType.convolutional(height, width, channels);


        // 构建 CNN 模型
        NeuralNetConfiguration.ListBuilder builder = new NeuralNetConfiguration.Builder()
                .seed(123)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(0.001))
                .list()
                .setInputType(inputType)
                // 添加多个卷积层和池化层
                .layer(new ConvolutionLayer.Builder()
                        .kernelSize(3, 3)
                        .stride(1, 1)
//                        .nIn(wordVectorSize)
                        .nOut(16)
                        .activation(Activation.RELU)
                        .convolutionMode(ConvolutionMode.Same)
                        .build())
                .layer(new SubsamplingLayer.Builder()
                        .kernelSize(2, 1)
                        .stride(2, 1)
                        .poolingType(SubsamplingLayer.PoolingType.MAX)
                        .build())
                .layer(new ConvolutionLayer.Builder()
                        .kernelSize(3, 1) // 新卷积层的大小和输入维度
                        .stride(1, 1)
                        .nIn(16) // 将第二个卷积层的输入维度设置为前一个卷积层的输出维度
                        .nOut(32) // 新卷积层输出的特征图数目
                        .activation(Activation.RELU)
                        .convolutionMode(ConvolutionMode.Same)
                        .build())
                .layer(new SubsamplingLayer.Builder()
                        .kernelSize(2, 1)
                        .stride(2, 1)
                        .poolingType(SubsamplingLayer.PoolingType.MAX)
                        .build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .nIn(32)
                        .nOut(100) // 根据任务设定输出的类别数量
                        .activation(Activation.SOFTMAX)
                        .build());

        MultiLayerNetwork model = new MultiLayerNetwork(builder.build());
        model.init();
//        boolean saveUpdater = true; // 如果需要保存模型更新器的状态，设置为 true
//
//        ModelSerializer.writeModel(model, modelPath, saveUpdater);

        return model;
    }

}
