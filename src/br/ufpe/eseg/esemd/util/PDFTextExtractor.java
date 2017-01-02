package br.ufpe.eseg.esemd.util;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFTextExtractor {

	public static PDFContent getPDF(File pdf) throws IOException {
		PDDocument doc = PDDocument.load(pdf);
		PDFContent ret = null;
		try {
			String content = new PDFTextStripper().getText(doc);
			ret = new PDFContent(pdf.getName(), content);
		} catch (Exception ex) {
			System.out.println(pdf.getName() + ": " + ex.getMessage());
		}
		return ret;
	}
}
