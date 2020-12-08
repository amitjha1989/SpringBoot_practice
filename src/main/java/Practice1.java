import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.TimeZone;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.blend.BlendMode;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;

/**
 * 
 */

/**
 * @author shyadamo
 *
 */
public class Practice1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long lStartTime = System.nanoTime();
		//addWaterMark("C:\\Shyam\\SCM\\SampleData\\Adobe Sign White Paper.txt",
		//		"C:\\Shyam\\SCM\\SampleData\\AdobeSignWhitePaperPdf.pdf");
		//addWaterMark("C:\\Shyam\\SCM\\SampleData\\Bus Schedule.txt",
		//		"C:\\Shyam\\SCM\\SampleData\\BusSchedulePdf.pdf");
		
		//addWaterMark("C:\\Shyam\\SCM\\SampleData\\Spring-5.txt",
		//		"C:\\Shyam\\SCM\\SampleData\\Spring-5pdf.pdf");
		
		//encode("C:\\Shyam\\SCM\\SampleData\\Spring-5.pdf",
		//		"C:\\Shyam\\SCM\\SampleData\\Spring-5.txt");
		encode("C:\\Shyam\\SCM\\SampleData\\Adobe Sign White Paper.pdf",
				"C:\\Shyam\\SCM\\SampleData\\Adobe Sign White Paper.txt");
		long lEndTime = System.nanoTime();
		System.out.println("Total time: =============="+(lEndTime-lStartTime)/1000000);
	}

	private static String printStackTrace(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String sStackTrace = sw.toString(); // stack trace as a string
		return sStackTrace;
	}
	
	private static  void addWaterMark(String inputFile, String outFile) {
		printCurrentTime();
		long lFunctionStartTime = 0;
		long lFunctionEndTime = 0;
		PDDocument document = null;
		byte[] convertedBytes = null;
		
		long lStartTime = 0;
		long lEndTime = 0;
		try {
			lFunctionStartTime = System.nanoTime();
			
			lStartTime = System.nanoTime();
				byte[] fileBytes = Files.readAllBytes(Paths.get(inputFile));
			lEndTime = System.nanoTime();
			System.out.println("Read file time: =============="+(lEndTime-lStartTime)/1000000);
			
			System.out.println("base64ToArray input length: ================ "+fileBytes.length);
			
			lStartTime = System.nanoTime();
				byte[] binaryString = Base64.getDecoder().decode(fileBytes);
			lEndTime = System.nanoTime();
			System.out.println("Base64 decoding time: =============="+(lEndTime-lStartTime)/1000000);
			
			lStartTime = System.nanoTime();
	        	int binaryLen = binaryString.length;
	        
	        	byte[] bytes = new byte[binaryLen];
	        
	        	for (int i = 0; i < binaryLen; i++) {
	        		byte ascii = binaryString[i];
	        		bytes[i] = ascii;
	        	}
	        lEndTime = System.nanoTime();
			System.out.println("Base64 array buffer time: =============="+(lEndTime-lStartTime)/1000000);
	        
	        System.out.println("base64ToArrayBuffer length: ================ "+bytes.length);
	        
			/*
			 * lStartTime = System.nanoTime(); File tempFile = File.createTempFile("Temp",
			 * null); FileOutputStream fos = new FileOutputStream(tempFile);
			 * fos.write(bytes); fos.flush(); fos.close(); lEndTime = System.nanoTime();
			 * System.out.println("Decoded bytes reading time: =============="+(lEndTime-
			 * lStartTime)/1000000);
			 */
	        
        	lStartTime = System.nanoTime();
            	//document = PDDocument.load( tempFile );
            	//document = PDDocument.load( tempFile,MemoryUsageSetting.setupTempFileOnly() );
        		document = PDDocument.load( bytes );
        		//document = PDDocument.load( bytes,null,null,null,MemoryUsageSetting.setupTempFileOnly() );
            	
            lEndTime = System.nanoTime();
            System.out.println("PDDocument load time: =============="+(lEndTime-lStartTime)/1000000);
        	
            PDFont font = PDType1Font.HELVETICA_BOLD;
            float fontsize = 14;
            
            lStartTime = System.nanoTime();
            	String strWaterMark = "Re-Print";
            
            	if( document.isEncrypted() )
            	{
            		throw new IOException( "Encrypted documents are not supported for this example" );
            	}
            lEndTime = System.nanoTime();
            System.out.println("Miscallaneous time: =============="+(lEndTime-lStartTime)/1000000);
            
            lStartTime = System.nanoTime();
	            PDPageTree allPages = document.getPages();
	            //log.error("No. of pages: "+allPages.getCount());
	        	for (int i = 0, len = allPages.getCount(); i < len; ++i)
	            {
	        		
	        		PDPage page = (PDPage)allPages.get(i);
	            	PDPageContentStream cs
	                = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND,false);
	            	
	            	float fontHeight = fontsize; // arbitrary for short text
	                float width = page.getMediaBox().getWidth();
	                float height = page.getMediaBox().getHeight();
	                float stringWidth = font.getStringWidth(strWaterMark) / 1000 * fontHeight;
	
	            	float x = width - (stringWidth*2);
	            	float y = height - (fontHeight*2);
	
	                cs.setFont(font, fontHeight);
					
					 PDExtendedGraphicsState gs = new PDExtendedGraphicsState();
					 
					 gs.setNonStrokingAlphaConstant(0.2f); gs.setStrokingAlphaConstant(0.2f);
					 gs.setBlendMode(BlendMode.MULTIPLY); gs.setLineWidth(3f);
					 cs.setGraphicsStateParameters(gs); 
	
					 cs.setNonStrokingColor(Color.red); cs.setStrokingColor(Color.red);
	
	                cs.beginText();
	                cs.newLineAtOffset(x, y);
	                cs.showText(strWaterMark);
	                cs.endText();
	                
	                cs.close();
	                
	            }
        	lEndTime = System.nanoTime();
        	System.out.println("Pages for loop time: =============="+(lEndTime-lStartTime)/1000000);
        	
        	lStartTime = System.nanoTime();
            	File newFile = new File(outFile);
            	lEndTime = System.nanoTime();
            System.out.println("Out file creation time: =============="+(lEndTime-lStartTime)/1000000);
        	
        	lStartTime = System.nanoTime();
            	document.save(newFile);
            lEndTime = System.nanoTime();
            System.out.println("Document save time: =============="+(lEndTime-lStartTime)/1000000);
        	
            
            lStartTime = System.nanoTime();
            	convertedBytes = Files.readAllBytes(Paths.get(newFile.getPath()));
            lEndTime = System.nanoTime();
            System.out.println("Saved file reading time: =============="+(lEndTime-lStartTime)/1000000);

            lStartTime = System.nanoTime();
	        	FileOutputStream fosout = new FileOutputStream(newFile);
	        	fosout.write(convertedBytes);
	        	fosout.flush();
	        	fosout.close();
        	lEndTime = System.nanoTime();
            System.out.println("Output file saving time: =============="+(lEndTime-lStartTime)/1000000);
            
            lFunctionEndTime = System.nanoTime();
            System.out.println("Try/Catch time: =============="+(lFunctionEndTime-lFunctionStartTime)/1000000);
		}catch(Exception e) {
			System.out.println(printStackTrace(e));
		}finally{
            if( document != null )
            {
            	try {
            		lStartTime = System.nanoTime();
            			document.close();
            		lEndTime = System.nanoTime();
                    System.out.println("Document closing time: =============="+(lEndTime-lStartTime)/1000000);
            	}catch(IOException ex) {
            		System.out.println(ex.getMessage());
            	}
            }
        }
		//long lFunctionEndTime = System.nanoTime();
		//System.out.println("Total time: =============="+(lFunctionEndTime-lFunctionStartTime)/1000000);
		printCurrentTime();
	}
	
	private static void printCurrentTime() {
		long epochmilli = System.currentTimeMillis();
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.sss");
        format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
        String formatted = format.format(epochmilli);
        System.out.println("Current time: ============"+formatted);
        
	}
	
	private static void encode(String inputFile, String outputFile) {
		try {
			long lStartTime = 0;
			long lEndTime = 0;
			
			lStartTime = System.nanoTime();
				byte[] fileBytes = Files.readAllBytes(Paths.get(inputFile));
			lEndTime = System.nanoTime();
			System.out.println("Read file time: =============="+(lEndTime-lStartTime)/1000000);
		
			System.out.println("base64ToArray input length: ================ "+fileBytes.length);
		
			lStartTime = System.nanoTime();
				byte[] binaryString = Base64.getEncoder().encode(fileBytes);;
			lEndTime = System.nanoTime();
			System.out.println("Base64 encoding time: =============="+(lEndTime-lStartTime)/1000000);
			
			System.out.println("base64ToArray output length: ================ "+binaryString.length);
			
			lStartTime = System.nanoTime();
	    		File newFile = new File(outputFile);
	    	lEndTime = System.nanoTime();
	    	System.out.println("Out file creation time: =============="+(lEndTime-lStartTime)/1000000);
	
	    	lStartTime = System.nanoTime();
	    		FileOutputStream fosout = new FileOutputStream(newFile);
	    		fosout.write(binaryString);
	    		fosout.flush();
	    		fosout.close();
	    		lEndTime = System.nanoTime();
	    	System.out.println("Output file saving time: =============="+(lEndTime-lStartTime)/1000000);
		
		}catch(Exception e) {
			System.out.println(printStackTrace(e));
		}
		
	}
}

