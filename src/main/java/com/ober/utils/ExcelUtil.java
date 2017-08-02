package com.ober.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcelUtil {
	private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

	/**
	 * 读取一个excel文件并返回map集合
	 * 
	 * @param excelPath
	 *            文件路径
	 * @param sheetName
	 * @param readIndex
	 *            从第几行读取数据
	 * @param columnNumMap
	 *            属性对应的列号
	 * @return
	 */
	public static List<Map<String, String>> readExcel(String excelPath, String sheetName, int readIndex, Map<String, Integer> columnNumMap) {
		List<Map<String, String>> list = new ArrayList<>();
		InputStream input = null;
		HSSFWorkbook workbook = null;
		try {
			// 获取输入流
			input = new FileInputStream(new File(excelPath));
			// 获取excel对象
			workbook = new HSSFWorkbook(input);
			HSSFSheet sheet = null;
			if (sheetName != null && !"".equals(sheetName)) {
				// 如果指定sheet名,则取指定sheet中的内容.
				sheet = workbook.getSheet(sheetName);
				if (sheet == null) {
					// 如果传入的sheet名不存在则默认指向第1个sheet.
					sheet = workbook.getSheetAt(0);
				}
			} else {
				// 如果不指定sheet名，默认获取第一个sheet
				sheet = workbook.getSheetAt(0);
			}
			int rows = sheet.getPhysicalNumberOfRows();
			// 只有有数据时才读取
			if (rows > readIndex) {
				HSSFRow row = null;
				HSSFCell cell = null;
				for (int i = readIndex; i < rows; i++) {
					row = sheet.getRow(i);
					if (row != null) {
						Map<String, String> map = new HashMap<>();
						// 遍历属性名对应的列号
						for (Map.Entry<String, Integer> entry : columnNumMap.entrySet()) {
							int columnNum = entry.getValue();
							cell = row.getCell(columnNum);
							if (cell != null) {
								// 统一变为字符串类型
								cell.setCellType(Cell.CELL_TYPE_STRING);
								String value = cell.getStringCellValue();
								map.put(entry.getKey(), value);
							}
						}
						list.add(map);
					}
				}
			}

		} catch (FileNotFoundException e) {
			logger.error("文件不存在！");
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IO异常");
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 
	 * @param excelPath
	 *            文件路径
	 * @param readIndex
	 *            从第几行读取数据
	 * @param columnNumMap
	 *            属性对应的列号
	 * @return
	 */
	public static List<Map<String, String>> readExcel(String excelPath, int readIndex, Map<String, Integer> columnNumMap) {
		return readExcel(excelPath, null, readIndex, columnNumMap);
	}
	
	
}
