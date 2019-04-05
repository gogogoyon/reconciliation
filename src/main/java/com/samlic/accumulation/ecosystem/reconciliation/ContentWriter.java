package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;

public interface ContentWriter {
	void writeContent(OutputStream os, String auditTime, String delimiter, String lineSeparator, Charset charset,
			int totalSize, Iterator<AuditIterator> data) throws IOException;
}
