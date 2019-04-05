package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class DefaultContentWriter implements ContentWriter {

	@Override
	public void writeContent(OutputStream os, String auditTime, String delimiter, String lineSeparator, Charset charset,
			int totalSize, Iterator<AuditIterator> data)  throws IOException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		StringBuilder firstLine =  new StringBuilder();
		firstLine.append(auditTime).append(delimiter)
			.append(totalSize).append(delimiter)
			.append(dateFormat.format(new Date())).append(lineSeparator);
		os.write(firstLine.toString().getBytes(charset));
		int count = 0;
		while(data.hasNext()) {
			AuditIterator auditIterator = data.next();
			StringBuilder line =  new StringBuilder();
			while(auditIterator.hasNext()) {				
				line.append(auditIterator.next());
				if(auditIterator.hasNext()) {					
					line.append(delimiter);
				}
			}
			line.append(lineSeparator);
			os.write(line.toString().getBytes(charset));
			
			count ++;
		}
		
		if(count != totalSize) {
			throw new IllegalStateException("Size of content is incorrect.");
		}
	}

}
