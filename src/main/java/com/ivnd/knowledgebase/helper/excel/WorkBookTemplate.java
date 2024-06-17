package com.ivnd.knowledgebase.helper.excel;

import com.ivnd.knowledgebase.helper.SpringExprHelper;
import com.ivnd.knowledgebase.helper.excel.model.SingleSheetWbDef;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkBookTemplate {

    private final XSSFHelper helper;
    private final SpringExprHelper exprHelper;

    public void readExcelFile(SingleSheetWbDef def,
                              Object data,
                              HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename=" + def.getFileName());

        this.writeToHttpResponse(def, data, response);
    }

    public void writeToHttpResponse(SingleSheetWbDef def,
                                    Object data,
                                    HttpServletResponse response) throws IOException {
        this.writeToOutputStream(def, data, response.getOutputStream());
    }

    @SuppressWarnings("java:S2093")
    public void writeToOutputStream(SingleSheetWbDef def,
                                    Object data,
                                    OutputStream os) throws IOException {
        Workbook workbook = helper.initWorkBook(def.getTemplateFile());
        try {
            this.readExcelFile(def, workbook, data);
            workbook.write(os);
        } catch (ResourceAccessException | NoSuchFieldException e) {
            log.error("[Exporter] Write report error: {}", e.getMessage());
        } finally {
            helper.closeWorkbook(workbook);
        }
    }

    @SneakyThrows
    public void readExcelFile(SingleSheetWbDef def,
                              Workbook workbook,
                              Object data) throws NoSuchFieldException {
        var sheet = workbook.getSheet(workbook.getSheetName(0));
        // build template header
        helper.buildTemplateHeader(sheet, data);
        // build summary header
        var font = helper.getFont(workbook, def);
        var rowHeaderIndex = helper.buildSummaryRow(def.getRowHeader(), sheet, data, def.getStartRow(), font);
        // build body
        var rowTemplate = def.getRowTemplate();
        if (Objects.nonNull(rowTemplate)) {
            var rowData = exprHelper.getValue(rowTemplate.getObject(), data);
            var rowIndex = helper.buildBodyRow(rowTemplate, rowData, sheet, rowHeaderIndex - 1, rowHeaderIndex, font);
            // build template footer
            helper.buildSummaryRow(def.getRowFooter(), sheet, data, rowIndex + 1, font);
        }

        if (Objects.nonNull(def.getSubRowTemplate())) {
            var originSubStartRow = def.getSubStartRow();
            var subStartRowField = data.getClass().getDeclaredField("mainContentSize");
            def.setSubStartRow((Integer) subStartRowField.get(data) + def.getSubStartRow());
            var subRowHeaderIndex = helper.buildSummaryRow(def.getRowHeader(), sheet, data, def.getSubStartRow(), font);
            // build body
            var subRowTemplate = def.getSubRowTemplate();
            var subRowData = exprHelper.getValue(subRowTemplate.getObject(), data);
            var subRowIndex = helper.buildBodyRow(subRowTemplate, subRowData, sheet, subRowHeaderIndex - 1, subRowHeaderIndex, font);
            // build template footer
            helper.buildSummaryRow(def.getRowFooter(), sheet, data, subRowIndex + 1, font);
            def.setSubStartRow(originSubStartRow);
        }
    }

    public void readMultiSheetExcelFile(String fileName,
                                        List<SingleSheetWbDef> def,
                                        Object data,
                                        HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename=" + fileName);

        this.writeToOutputStream(def, data, response.getOutputStream());
    }

    @SuppressWarnings("java:S2093")
    public void writeToOutputStream(List<SingleSheetWbDef> def,
                                    Object data,
                                    OutputStream os) throws IOException {
        var templateFile = def.get(0).getTemplateFile();
        Workbook workbook = helper.initWorkBook(templateFile);

        try {
            this.readMultipleSheetExcelFile(def, workbook, data);
            workbook.write(os);
        } catch (ResourceAccessException e) {
            log.error("[Exporter] Write report error: {}", e.getMessage());
        } finally {
            helper.closeWorkbook(workbook);
        }
    }

    public void readMultipleSheetExcelFile(List<SingleSheetWbDef> def,
                                           Workbook workbook,
                                           Object data) {
        for (int i = 0; i < def.size(); i++) {
            var sheet = workbook.getSheet(workbook.getSheetName(i));
            // build template header
            helper.buildTemplateHeader(sheet, data);
            // build summary header
            var font = helper.getFont(workbook, def.get(i));
            var rowHeaderIndex = helper.buildSummaryRow(def.get(i).getRowHeader(), sheet, data, def.get(i).getStartRow(), font);
            // build body
            var rowTemplate = def.get(i).getRowTemplate();
            var rowData = exprHelper.getValue(rowTemplate.getObject(), data);
            var rowIndex = helper.buildBodyRow(rowTemplate, rowData, sheet, rowHeaderIndex - 1, rowHeaderIndex, font);
            // build template footer
            helper.buildSummaryRow(def.get(i).getRowFooter(), sheet, data, rowIndex + 1, font);
        }
    }

}
