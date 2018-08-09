package com.blueocean.azbrain.util;

import com.blueocean.azbrain.vo.UserVo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    public static List<UserVo> read(String path){
        try(
            FileInputStream input = new FileInputStream(new File(path));
            InputStream stream = new BufferedInputStream(input);
        ) {
            XSSFWorkbook workbook = new XSSFWorkbook(stream);
            XSSFSheet st = workbook.getSheetAt(0);
            List<UserVo> list = new ArrayList<>();
            for (int rowIndex = 1; rowIndex <= st.getLastRowNum(); rowIndex++) {
                XSSFRow row = st.getRow(rowIndex);
                if (row == null) continue;
                String kcode = row.getCell(0).getStringCellValue();
                String name = row.getCell(1).getStringCellValue();
                String mobile = Long.toString(Math.round(row.getCell(2).getNumericCellValue()));
                String bu = row.getCell(3).getStringCellValue();
                String gender = row.getCell(4).getStringCellValue();
                System.out.println(kcode + ":" + name + ":" + mobile + ":" + bu + ":" + gender);
                UserVo vo = new UserVo();
                vo.setJobNumber(kcode);
                vo.setName(name);
                vo.setBusinessUnit(bu);
                vo.setMobile(mobile);
                vo.setGender("男".equalsIgnoreCase(gender) ? "M" : "F");
                list.add(vo);
            }

            return list;
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args){
        read("d:/azbrain-e.xlsx");
    }
}
