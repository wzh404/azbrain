package com.blueocean.azbrain.util;

import com.blueocean.azbrain.model.EventLog;
import com.blueocean.azbrain.vo.UserVo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExcelUtil {
    private static ExcelUtil eu = new ExcelUtil();
    private ExcelUtil(){}

    public static ExcelUtil getInstance() {
        return eu;
    }

    /**
     * 导入用户信息
     *
     * @param path
     * @return
     */
    public static List<UserVo> read(String path) {
        try (
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
                String mobile;
                if (row.getCell(2).getCellType() == CellType.STRING.getCode()) {
                    mobile = row.getCell(2).getStringCellValue();
                } else if (row.getCell(2).getCellType() == CellType.STRING.getCode()) {
                    mobile = Long.toString(Math.round(row.getCell(2).getNumericCellValue()));
                } else {
                    mobile = row.getCell(2).getRawValue();
                }

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return null;
    }

    private Workbook handleObj2Excel(List objs, Class clz) {
        Workbook wb = new HSSFWorkbook();
        try {
            Sheet sheet = wb.createSheet();
            Row r = sheet.createRow(0);
            List<ExcelHeader> headers = getHeaderList(clz);
            Collections.sort(headers);
            //写标题
            for (int i = 0; i < headers.size(); i++) {
                r.createCell(i).setCellValue(headers.get(i).getTitle());
            }
            //写数据
            Object obj = null;
            for (int i = 0; i < objs.size(); i++) {
                r = sheet.createRow(i + 1);
                obj = objs.get(i);
                for (int j = 0; j < headers.size(); j++) {
                    Object value = obj.getClass().getMethod(headers.get(j).getMethodName()).invoke(obj);
                    if (value == null) {
                        value = "";
                    }
                    Cell cell = r.createCell(j);
                    if (value instanceof Integer || value instanceof Double) {
                        cell.setCellValue(Double.valueOf(value.toString()));
                    } else if (value instanceof LocalDateTime) {
                        String cellValue = ((LocalDateTime)value).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        cell.setCellValue(cellValue);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public void exportObj2Excel(OutputStream os, List objs, Class clz) {
        try {
            Workbook wb = handleObj2Excel(objs, clz);
            wb.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<ExcelHeader> getHeaderList(Class clz) {
        List<ExcelHeader> headers = Arrays.stream(clz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(ExcelResources.class))
                .map(f -> {
                    String methodName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    ExcelResources er = f.getAnnotation(ExcelResources.class);
                    return new ExcelHeader(er.title(), er.order(), methodName);
                })
                .collect(Collectors.toList());
        return headers;
    }

    public static void main(String[] args) {
        // read("d:/azbrain-e.xlsx");
        ExcelUtil u = new ExcelUtil();
        for (ExcelHeader h : u.getHeaderList(EventLog.class)){
            System.out.println(h.getMethodName() + " - " + h.getTitle() + " - " + h.getOrder());
        }
    }
}
