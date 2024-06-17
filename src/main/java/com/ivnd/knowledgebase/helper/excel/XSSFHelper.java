package com.ivnd.knowledgebase.helper.excel;

import com.ivnd.knowledgebase.helper.SpringExprHelper;
import com.ivnd.knowledgebase.helper.excel.model.SingleSheetWbDef;
import com.ivnd.knowledgebase.utils.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Exporter Excel From Json Config
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XSSFHelper {

    private final SpringExprHelper exprHelper;

    public XSSFWorkbook initWorkBook(String templateDir) throws IOException {
        var inputStream = new FileInputStream(templateDir);
        return (XSSFWorkbook) WorkbookFactory.create(inputStream);
    }


    public void closeWorkbook(Workbook workbook) throws IOException {
        if (workbook != null) workbook.close();
    }

    public Font getFont(Workbook workbook, SingleSheetWbDef singleSheetWbDef) {
        var font = (XSSFFont) workbook.createFont();
        if (StringUtils.hasLength(singleSheetWbDef.getFontFamily())) {
            font.setFontName(singleSheetWbDef.getFontFamily());
        }
        if (singleSheetWbDef.getFontSize() != 0) {
            font.setFontHeightInPoints(singleSheetWbDef.getFontSize());
        }

        return font;
    }


    public void buildTemplateHeader(Sheet sheet, Object object) {
        for (var row : sheet) {
            for (var cell : row) {
                DataFormatter dataFormatter = new DataFormatter();
                dataFormatter.formatCellValue(cell);
                var cellVal = dataFormatter.formatCellValue(cell);
                if (StringUtils.hasLength(cellVal) && cellVal.contains("#")) {
                    var parsedVal = exprHelper.getValueExpr(cellVal, object);
                    String value = Objects.isNull(parsedVal) ? "" : String.valueOf(parsedVal);
                    this.setValue(cell, cellVal, value);
                }
            }
        }
    }

    private void setValue(Cell cell, String cellVal, String value) {
        if (cellVal.contains("cashAmtPercent") || cellVal.contains("growthPercent")
                || cellVal.contains("wealthPercent") || cellVal.contains("healthPercent")
                || cellVal.contains("tdAmtPercent") || cellVal.contains("bondValuePercent")
                || cellVal.contains("fundCertificatePercent") || cellVal.contains("stockNRightPercent")
                || cellVal.contains("coveredWarrantDPercent") || cellVal.contains("derivativeDtradePercent")) {
            String[] number = value.split("%");
            cell.setCellValue(Double.parseDouble(number[0]) / 100);
        } else {
            cell.setCellValue(value);
        }
    }

    public int buildBodyRow(SingleSheetWbDef.RowTemplate rowTemplate,
                            Object rowData,
                            Sheet sheet,
                            int currentRow,
                            int startRow,
                            Font font) {

        if (rowData instanceof Iterable) {
            var rowIndex = currentRow;
            for (var item : (Iterable<Object>) rowData) {
                rowIndex++;
                rowIndex = buildBodyRow(rowTemplate, item, sheet, rowIndex, startRow, font);
            }
            return rowIndex;
        }

        this.createOneRow(rowTemplate.getCellTemplates(), rowData, sheet, currentRow, startRow, font);

        if (Objects.nonNull(rowTemplate.getChildren())) {
            var subObject = exprHelper.getValue(rowTemplate.getChildren().getObject(), rowData);
            return buildBodyRow(rowTemplate.getChildren(), subObject, sheet, currentRow, startRow, font);
        }

        return currentRow;
    }

    private void createOneRow(List<SingleSheetWbDef.CellTemplate> cellTemplates,
                              Object rowData,
                              Sheet sheet,
                              int indexRow,
                              int startRow,
                              Font font) {

        int lastRow = sheet.getLastRowNum();
        if (lastRow > indexRow) {
            sheet.shiftRows(indexRow + 1, lastRow, 1, true, true);
        }

        Row row = indexRow > startRow ? sheet.createRow(indexRow) : sheet.getRow(indexRow);

        if (Objects.isNull(row)) {
            row = sheet.createRow(indexRow);
        }

        Row formatRow = sheet.getRow(startRow);

        for (var cellTemplate : cellTemplates) {
            Cell cell = indexRow > startRow ? row.createCell(cellTemplate.getColumnIndex() - 1) : row.getCell(cellTemplate.getColumnIndex() - 1);

            if (Objects.isNull(cell)) {
                cell = row.createCell(cellTemplate.getColumnIndex() - 1);
            }

            if (Objects.nonNull(cellTemplate.getCellStyle())) {
                applyCellStyle(sheet.getWorkbook(), cell, cellTemplate.getCellStyle(), font);
            } else {
                if (Objects.nonNull(formatRow)) {
                    cell.setCellStyle(formatRow.getCell(cellTemplate.getColumnIndex() - 1).getCellStyle());
                }
            }
            this.setCellValueInBodyRow(cell, rowData, cellTemplate);
        }
    }

    public int buildSummaryRow(SingleSheetWbDef.RowTemplate rowTemplate,
                               Sheet sheet,
                               Object data,
                               int currentRow, Font font) {
        if (Objects.isNull(rowTemplate)) {
            return currentRow;
        }

        return buildSummaryRow(Collections.singletonList(rowTemplate), sheet, data, currentRow, font);
    }

    public int buildSummaryRow(List<SingleSheetWbDef.RowTemplate> rowTemplates,
                               Sheet sheet,
                               Object data,
                               int currentRow,
                               Font font) {
        if (CollectionUtils.isEmpty(rowTemplates)) {
            return currentRow;
        }

        for (SingleSheetWbDef.RowTemplate r : rowTemplates) {
            Row row = sheet.createRow(currentRow);
            for (var cellTemplate : r.getCellTemplates()) {
                Cell cell = row.createCell(cellTemplate.getColumnIndex() - 1);
                if (Objects.nonNull(cellTemplate.getCellStyle())) {
                    applyCellStyle(sheet.getWorkbook(), cell, cellTemplate.getCellStyle(), font);
                }
                if (Objects.nonNull(r.getMergeCells())) {
                    applyMergeCell(r.getMergeCells(), row, sheet);
                }
                var cellValue = exprHelper.getValueExpr(cellTemplate.getValue(), data);
                if (Objects.nonNull(cellValue)) {
                    cell.setCellValue(String.valueOf(cellValue));//null -> "null"
                } else {
                    cell.setCellValue(String.valueOf(cellTemplate.getValue()));
                }
            }
            currentRow++;
        }
        return currentRow;
    }

    public void applyCellStyle(Workbook workbook,
                               Cell cell,
                               SingleSheetWbDef.CellStyleDef cellStyleDef,
                               Font font) {
        var cellStyle = workbook.createCellStyle();

        if (Objects.nonNull(cellStyleDef.getBorder())) {
            var border = cellStyleDef.getBorder();
            setIfHasValue(border.getTop(), cellStyle::setBorderTop);
            setIfHasValue(border.getLeft(), cellStyle::setBorderLeft);
            setIfHasValue(border.getBottom(), cellStyle::setBorderBottom);
            setIfHasValue(border.getRight(), cellStyle::setBorderRight);
        }
        if (Objects.nonNull(cellStyleDef.getAlignment())) {
            setIfHasValue(cellStyleDef.getAlignment().getHorizontal(), cellStyle::setAlignment);
            setIfHasValue(cellStyleDef.getAlignment().getVertical(), cellStyle::setVerticalAlignment);
        }
        if (Objects.nonNull(cellStyleDef.getFont()) && (cellStyleDef.getFont().isBold())) {
                font.setBold(true);

        }
        if (Objects.nonNull(cellStyleDef.getIsWrapped())) {
            setIfHasValue(cellStyleDef.getIsWrapped(), cellStyle::setWrapText);
        }
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
    }

    public void applyMergeCell(List<SingleSheetWbDef.MergeCell> mergeCells,
                               Row row,
                               Sheet sheet) {
        if (!CollectionUtils.isEmpty(mergeCells)) {
            mergeCells.forEach(m -> sheet.addMergedRegion(new CellRangeAddress(
                    row.getRowNum(),
                    row.getRowNum(),
                    m.getFromColIndex() - 1,
                    m.getToColIndex() - 1
            )));
        }
    }

    public <T> void setIfHasValue(T obj, Consumer<T> consumer) {
        if (Objects.nonNull(obj)) consumer.accept(obj);
    }

    private void setCellValueInBodyRow(Cell cell,
                                       Object data,
                                       SingleSheetWbDef.CellTemplate cellTemplate) {
        var cellValue = exprHelper.getValue(cellTemplate.getValue(), data);
        if (Objects.nonNull(cellTemplate.getDisplayCondition())) {
            var nonDisplay = !exprHelper.getValueAsBool(cellTemplate.getDisplayCondition(), data);
            if (nonDisplay) {
                return;
            }
        }
        if (cellValue instanceof Double) {
            cell.setCellValue((Double) cellValue);
        } else {
            cell.setCellValue(Objects.isNull(cellValue) ? "" : String.valueOf(cellValue));//null -> "null"
        }

    }

}