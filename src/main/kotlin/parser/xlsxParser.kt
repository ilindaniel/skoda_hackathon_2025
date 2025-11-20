package org.example.parser

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream

fun xlsxParser(path: String): List<Map<String, String?>> {
    val workbook = XSSFWorkbook(FileInputStream(path))
    val sheet = workbook.getSheetAt(0)

    val header = sheet.getRow(0).map { it.stringCellValue.trim() }

    return sheet.drop(1).map { row ->
        row.map { it.toString() }.let { values ->
            header.zip(values).toMap()
        }
    }
}
