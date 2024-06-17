package com.ivnd.knowledgebase.service;

import com.itextpdf.kernel.geom.PageSize;
import com.ivnd.knowledgebase.helper.pdf.PDFHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.itextpdf.html2pdf.css.CssConstants.LANDSCAPE;
import static com.itextpdf.html2pdf.css.CssConstants.PORTRAIT;

/**
 * Created by: tuyen.nguyenvan
 * Email: tuyen.nguyen3@ivnd.com.vn
 * Date: 11/06/2024
 */

@Service
@RequiredArgsConstructor
public class ExportPDFService {
    private final PDFHelper pdfHelper;

    public byte[] getUDToPDF(Object resp) {
        Map<String, Object> variable = new HashMap<>();
        variable.put("bs", resp);
        return pdfHelper.getPDF(variable, "underlying_product_template", LANDSCAPE, PageSize.A4.rotate());
    }

    public void writeUPToPDF(String fileName, Object entryData, HttpServletResponse response) throws IOException {
        Map<String, Object> variable = new HashMap<>();
        //root object
        variable.put("root", entryData);
        //children object
        variable.put("children", entryData);
        this.initPDFHeader(fileName, response);
        pdfHelper.exportPDF(variable, "underlying_product_template", response.getOutputStream(), PORTRAIT, PageSize.A4.rotate());
    }

    private void initPDFHeader(String fileName, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;  filename=" + fileName);
    }
}
