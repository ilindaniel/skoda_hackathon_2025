package org.example.parser

import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream

fun xlsxParser(path: String, sheetIndex: Int = 0): List<Map<String, String?>> {
    val workbook = XSSFWorkbook(FileInputStream(path))
    val sheet = workbook.getSheetAt(sheetIndex)

    val header = sheet.getRow(0).map { it.stringCellValue.trim() }

    return sheet.drop(1).map { row ->
        val values = header.indices.map { colIndex ->
            val cell = row.getCell(colIndex)
            when (cell?.cellType) {
                CellType.STRING -> cell.stringCellValue.trim()
                CellType.BLANK, null -> null
                else -> cell.toString()
            }
        }
        header.zip(values).toMap()
    }
}