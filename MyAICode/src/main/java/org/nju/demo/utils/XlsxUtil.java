package org.nju.demo.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.nju.demo.pojo.dto.IssueInfoDTO;
import org.nju.demo.pojo.dto.PatternInfoDTO;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class XlsxUtil {

    //从file中获取第sheetNum页第rowNum行第cellNum列的内容，返回值为String
    public static String getCellContext(FileInputStream file, int sheetNum,int rowNum,int cellNum) {
        String cellContext = "";
        try {
//            FileInputStream file = new FileInputStream("data/xlsx/c_1.0.2详细报告.xlsx");
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(1);

            // 指定行和列（注意：Excel中的行和列索引都是从0开始的）
//            int rowNum = 2;
//            int cellNum = 5;
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(cellNum);
                cellContext = cell.toString();
            } else {
                cellContext = "";
            }

            workbook.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cellContext;
    }

    public static Map<String, List> getXlsxInfo(InputStream file) throws IOException, InvalidFormatException {
        List<IssueInfoDTO> issueInfoList = new ArrayList<>();
        List<PatternInfoDTO> patternInfoList = new ArrayList<>();
        Map<String,List> res = new HashMap<>();
        Workbook workbook = WorkbookFactory.create(file);
        for (int sheetNum=0; sheetNum<workbook.getNumberOfSheets();sheetNum++){
            Sheet sheet = workbook.getSheetAt(sheetNum);
            int lastRowNum = sheet.getPhysicalNumberOfRows();
            for (int rowNum = 1;rowNum < lastRowNum;rowNum++){
                Row row = sheet.getRow(rowNum);
                PatternInfoDTO patternInfoDTO = new PatternInfoDTO();
                IssueInfoDTO issueInfoDTO = new IssueInfoDTO();
                String pattenName = "";
                issueInfoDTO.setPriority(row.getCell(1).getStringCellValue());
                issueInfoDTO.setKingdom(row.getCell(2).getStringCellValue());
                pattenName = row.getCell(3).getStringCellValue();
                patternInfoDTO.setPatternName(pattenName);
                issueInfoDTO.setPatternName(pattenName);
                String filePathValue = row.getCell(4).getStringCellValue();
                String fileNameValue = filePathValue.substring(filePathValue.lastIndexOf("/") + 1);
                issueInfoDTO.setFilePath(filePathValue);
                issueInfoDTO.setFileName(fileNameValue);
                issueInfoDTO.setStartLine(Integer.valueOf(row.getCell(5).getStringCellValue()));
                patternInfoDTO.setExplanation(row.getCell(6).getStringCellValue());
                patternInfoDTO.setRecommendation(row.getCell(7).getStringCellValue());
                issueInfoDTO.setSnippet("");
                patternInfoList.add(patternInfoDTO);
                issueInfoList.add(issueInfoDTO);
            }
        }
        res.put("issueInfoList",issueInfoList);
        res.put("patternInfoList",patternInfoList);
        return res;
    }
}

