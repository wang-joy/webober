package com.ober.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
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
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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

	/**
	 * 导出excel文件
	 * 
	 * @param list
	 *            实体集合
	 * @param writeIndex
	 *            从第几行开始写入数据
	 * @param columnNumMap
	 *            属性和列号对应的map
	 * @param templatePath
	 *            模板路径
	 * @param clazz
	 *            类型
	 * @param filePath
	 *            目标文件
	 * @return true 表示写入成功，false表示写入失败
	 */
	public static boolean writeExcel(List<Object> list, int writeIndex, Map<String, Integer> columnNumMap, String templatePath, Class<?> clazz,
			String filePath) {
		InputStream is = null;
		OutputStream os = null;
		boolean flag = false;
		if (list != null && list.size() > 0) {
			try {
				is = new FileInputStream(templatePath);
				os = new FileOutputStream(filePath);
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				HSSFSheet sheet = workbook.getSheetAt(0);
				Field[] fields = clazz.getDeclaredFields();
				Row row = null;
				Cell cell = null;
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + writeIndex);
					for (Field field : fields) {
						field.setAccessible(true);
						String fieldName = field.getName();
						String value = field.get(list.get(i)).toString();
						int colNum = columnNumMap.get(fieldName);
						cell = row.createCell(colNum);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(value);
					}
				}
				workbook.write(os);
				os.flush();
				flag = true;

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 导出excel文件
	 * 
	 * @param list
	 *            实体集合
	 * @param writeIndex
	 *            从第几行开始写入数据
	 * @param columnNumMap
	 *            属性和列号对应的map
	 * @param templatePath
	 *            模板路径
	 * @param filePath
	 *            目标文件
	 * @return true 表示写入成功，false表示写入失败
	 */
	public static boolean writeExcel(List<Map<String, Object>> list, int writeIndex, Map<String, Integer> columnNumMap, String templatePath, String filePath) {
		InputStream is = null;
		OutputStream os = null;
		boolean flag = false;
		if (list != null && list.size() > 0) {
			try {
				is = new FileInputStream(templatePath);
				os = new FileOutputStream(filePath);
				HSSFWorkbook workbook = new HSSFWorkbook(is);
				HSSFSheet sheet = workbook.getSheetAt(0);
				Row row = null;
				Cell cell = null;
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + writeIndex);
					for (Map.Entry<String, Integer> entry : columnNumMap.entrySet()) {
						String value = list.get(i).get(entry.getKey()).toString();
						int colNum = entry.getValue();
						cell = row.createCell(colNum);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(value);
					}
				}
				workbook.write(os);
				os.flush();
				flag = true;

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		return flag;
	}

	/**
	 * 没有模板文件
	 * 
	 * @param list
	 *            实体集合
	 * @param columnNumMap
	 *            key:属性，value:列号
	 * @param titles
	 *            表头 key:属性，value:表头
	 * @param clazz
	 *            实体类型
	 * @param filePath
	 *            目标文件
	 * @return true 表示写入成功，false表示写入失败
	 */
	public static boolean writeExcel(List<Object> list, Map<String, Integer> columnNumMap, Map<String, Object> titles, Class<?> clazz, String filePath) {
		OutputStream os = null;
		boolean flag = false;
		if (list != null && list.size() > 0) {
			try {
				os = new FileOutputStream(filePath);
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet();
				Field[] fields = clazz.getDeclaredFields();
				Row row = sheet.createRow(0);
				Cell cell = null;
				// 设置表头
				for (Map.Entry<String, Integer> entry : columnNumMap.entrySet()) {
					String key = entry.getKey();
					Integer colNum = entry.getValue();
					cell = row.createCell(colNum);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(titles.get(key).toString());
				}
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + 1);
					for (Field field : fields) {
						field.setAccessible(true);
						String fieldName = field.getName();
						String value = field.get(list.get(i)).toString();
						int colNum = columnNumMap.get(fieldName);
						cell = row.createCell(colNum);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(value);
					}
				}
				workbook.write(os);
				os.flush();
				flag = true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}

	/**
	 * 没有模板文件
	 * 
	 * @param list
	 *            实体集合
	 * @param columnNumMap
	 *            key:属性，value:列号
	 * @param titles
	 *            表头 key:属性，value:表头
	 * @param clazz
	 *            实体类型
	 * @param filePath
	 *            目标文件
	 * @return true 表示写入成功，false表示写入失败
	 */
	public static boolean writeExcel(List<Map<String, Object>> list, Map<String, Integer> columnNumMap, Map<String, Object> titles, String filePath) {
		OutputStream os = null;
		boolean flag = false;
		if (list != null && list.size() > 0) {
			try {
				os = new FileOutputStream(filePath);
				HSSFWorkbook workbook = new HSSFWorkbook();
				HSSFSheet sheet = workbook.createSheet();
				Row row = sheet.createRow(0);
				Cell cell = null;
				// 设置表头
				for (Map.Entry<String, Integer> entry : columnNumMap.entrySet()) {
					String key = entry.getKey();
					Integer colNum = entry.getValue();
					cell = row.createCell(colNum);
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cell.setCellValue(titles.get(key).toString());
				}
				for (int i = 0; i < list.size(); i++) {
					row = sheet.createRow(i + 1);
					for (Map.Entry<String, Integer> entry : columnNumMap.entrySet()) {
						String value = list.get(i).get(entry.getKey()).toString();
						int colNum = entry.getValue();
						cell = row.createCell(colNum);
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cell.setCellValue(value);
					}
				}
				workbook.write(os);
				os.flush();
				flag = true;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return flag;
	}
}
