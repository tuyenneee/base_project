package com.ivnd.knowledgebase.export;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivnd.knowledgebase.helper.excel.WorkBookTemplate;
import com.ivnd.knowledgebase.helper.excel.model.SingleSheetWbDef;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Log4j2
public abstract class AbstractExportExcel<T> implements Exporter {
    private final String pathOfTemplate;
    private ObjectMapper mapper;
    private WorkBookTemplate template;
    private SingleSheetWbDef singleSheetWbDef;

    protected AbstractExportExcel(String pathOfTemplate) {
        this.pathOfTemplate = pathOfTemplate;
    }

    public void export(String fileName, T data, HttpServletResponse httpResponse) {
        SingleSheetWbDef def = this.getSingleSheetWbDef();
        def.setFileName(fileName);

        this.export(def, data, httpResponse);
    }

    public void export(SingleSheetWbDef def, T data, HttpServletResponse httpResponse) {
        try {
            template.readExcelFile(def, data, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private SingleSheetWbDef loadSingleSheetWbDef(String pathOfTemplate) {
        try {
            return mapper.readValue(Files.readAllBytes(Paths.get(pathOfTemplate)), SingleSheetWbDef.class);
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
            return null;
        }
    }

    protected SingleSheetWbDef getSingleSheetWbDef() {
        if (Objects.isNull(this.singleSheetWbDef)) {
            this.singleSheetWbDef = loadSingleSheetWbDef(pathOfTemplate);
        }
        return this.singleSheetWbDef;
    }

    public void exportMultipleSheet(String fileName, T data, HttpServletResponse httpResponse) {
        List<SingleSheetWbDef> def = this.loadListSingleSheetWbDef(pathOfTemplate);
        this.exportMultipleSheet(fileName, def, data, httpResponse);
    }

    public void exportMultipleSheet(String fileName, List<SingleSheetWbDef> def, T data, HttpServletResponse httpResponse) {
        try {
            template.readMultiSheetExcelFile(fileName, def, data, httpResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<SingleSheetWbDef> loadListSingleSheetWbDef(String pathOfTemplate) {
        try {
            return mapper.readValue(Files.readAllBytes(Paths.get(pathOfTemplate)), new TypeReference<List<SingleSheetWbDef>>() {
            });
        } catch (IOException ioException) {
            log.error(ioException.getMessage(), ioException);
            return null;
        }
    }

//    @Autowired
//    public void setTemplate(WorkBookTemplate template) {
//        this.template = template;
//    }
//
//    @Autowired
//    public void setMapper(ObjectMapper mapper) {
//        this.mapper = mapper;
//    }
}
