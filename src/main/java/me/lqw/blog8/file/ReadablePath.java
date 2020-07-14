package me.lqw.blog8.file;

import java.io.IOException;
import java.io.InputStream;

public interface ReadablePath {
	InputStream getInputStream() throws IOException;

	String fileName();

	long size();

	long lastModified();

	String getExtension();
}