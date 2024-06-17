package com.ivnd.knowledgebase.helper.excel.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SingleSheetWbDef {

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("sheetName")
    private String sheetName;

    @JsonProperty("templateFile")
    private String templateFile;

    @JsonProperty("startRow")
    private int startRow;

    @JsonProperty("rowFooter")
    private List<RowTemplate> rowFooter;

    @JsonProperty("rowTemplate")
    private RowTemplate rowTemplate;

    @JsonProperty("subStartRow")
    private int subStartRow;

    @JsonProperty("subRowTemplate")
    private RowTemplate subRowTemplate;

    @JsonProperty("rowHeader")
    private RowTemplate rowHeader;

    @JsonProperty("fontFamily")
    private String fontFamily;

    @JsonProperty("fontSize")
    private short fontSize;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RowTemplate {

        @JsonProperty("object")
        private String object;

        @JsonProperty("cellTemplates")
        private List<CellTemplate> cellTemplates;

        @JsonIgnoreProperties
        private List<MergeCell> mergeCells;

        @JsonProperty("children")
        private RowTemplate children;
    }

    @Data
    public static class CellTemplate {

        @JsonProperty("value")
        private String value;

        @JsonProperty("colIndex")
        private int columnIndex;

        @JsonProperty("displayCondition")
        private String displayCondition;

        private CellStyleDef cellStyle;
    }

    @Data
    public static class Border {
        BorderStyle top;
        BorderStyle left;
        BorderStyle bottom;
        BorderStyle right;

    }

    @Data
    public static class Alignment {
        VerticalAlignment vertical;
        HorizontalAlignment horizontal;
    }

    @Data
    public static class Font {
        boolean bold;
    }

    @Data
    public static class CellStyleDef {
        Border border;
        Alignment alignment;
        Font font;
        Boolean isWrapped;
    }
    @Data
    public static class MergeCell {
        int fromColIndex;
        int toColIndex;
    }

}
