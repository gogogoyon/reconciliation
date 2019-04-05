package com.samlic.accumulation.ecosystem.reconciliation;

import java.io.File;
import java.io.IOException;

public interface Downloader {
	File download(String fileName) throws IOException;
	
	File[] downloadFiles(String suffix) throws IOException;
}
