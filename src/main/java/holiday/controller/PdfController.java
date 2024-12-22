package holiday.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lowagie.text.DocumentException;

import holiday.services.PdfService;

@Controller
public class PdfController {
	
	
	 private PdfService pdfService;
	 
	 
	 
	 @Autowired
	 public void setPdfService(PdfService pdfService) {
		this.pdfService = pdfService;
	}


	@GetMapping("/downloadpdf")
	    public void downloadPDFResource(HttpServletResponse response) {
	        try {
	            Path file = Path.of(pdfService.generatePdf().getAbsolutePath());
	            if (Files.exists(file)) {
	                response.setContentType(MediaType.APPLICATION_PDF_VALUE);
	                response.addHeader(HttpHeaders.CONTENT_DISPOSITION,
	                        "attachment; filename=" + file.getFileName());
	                Files.copy(file, response.getOutputStream());
	                response.getOutputStream().flush();
	            }
	        } catch (DocumentException | IOException ex) {
	            ex.printStackTrace();
	        }
	    }

}
