package com.ivnd.knowledgebase.helper.pdf;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.css.media.MediaDeviceDescription;
import com.itextpdf.styledxmlparser.css.media.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PDFHelper {

    private final TemplateEngine templateEngine;

    /* Create HTML using Thymeleaf template Engine */
    public void exportPDF(Map<String, Object> variables,
                          String template,
                          OutputStream os,
                          String orientation,
                          PageSize pageSize) {
        var context = new Context();
        context.setVariables(variables);
        var stockTradingHTML = templateEngine.process(template, context);
        var pdfWriter = new PdfWriter(os);
        var converterProperties = new ConverterProperties();
        var mediaDeviceDescription = new MediaDeviceDescription(MediaType.SCREEN);
        converterProperties.setMediaDeviceDescription(mediaDeviceDescription);
        mediaDeviceDescription.setOrientation(orientation);
        var pdfDocument = new PdfDocument(pdfWriter);
        //For setting the PAGE SIZE
        pdfDocument.setDefaultPageSize(new PageSize(pageSize));
        HtmlConverter.convertToPdf(stockTradingHTML, pdfDocument, converterProperties);
    }

    public byte[] getPDF(Map<String, Object> variables, String template, String orientation, PageSize pageSize) {
        var os = new ByteArrayOutputStream();
        this.exportPDF(variables, template, os, orientation, pageSize);

        return os.toByteArray();
    }

}
