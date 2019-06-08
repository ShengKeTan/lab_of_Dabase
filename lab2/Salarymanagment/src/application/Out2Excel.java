package application;

import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class Out2Excel {
	 /** 
     *  导出Excel
     * @param sheetName 表格 sheet 的名称
     * @param headers  标题名称
     * @param dataList 需要显示的数据集合
     * @param exportExcelName 导出excel文件的名字
     */ 
	public  static void exportExcel(String sheetName, List<Monthdate> dataList,
				String[] headers,String exportExcelName) {

		// 声明一个工作薄
		XSSFWorkbook  workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(sheetName);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(15);

		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			//cell.setCellStyle(titleStyle);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		
        // 遍历集合数据，产生数据行
		for(int i = 0; i < dataList.size(); i++) {
			row = sheet.createRow(i + 1);
			//值写入
			row.createCell(0).setCellValue(dataList.get(i).getEid());
			row.createCell(1).setCellValue(dataList.get(i).getEname());
			row.createCell(2).setCellValue(dataList.get(i).getDname());
			row.createCell(3).setCellValue(dataList.get(i).getPname());
			row.createCell(4).setCellValue(dataList.get(i).getRest());
			row.createCell(5).setCellValue(dataList.get(i).getDuty());
			row.createCell(6).setCellValue(dataList.get(i).getExtra());
			row.createCell(7).setCellValue(dataList.get(i).getBpay());
			row.createCell(8).setCellValue(dataList.get(i).getSalary());
			row.createCell(9).setCellValue(dataList.get(i).getTime());
		}
		
		OutputStream out = null;
		try {
            	
			String tmpPath = "/Users/Doris/Documents/lab_of_Database/lab2/Salarymanagment" + exportExcelName + ".xlsx";
			out = new FileOutputStream(tmpPath);
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(workbook != null){
				try {
					workbook.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
