package me.lqw.blog8.file;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Optional;


final class ReadablePathSource implements Resource {

    private static final MediaType IMAGE_WEBP = MediaType.valueOf("image/webp");

    private final ReadablePath path;

    public ReadablePathSource(ReadablePath path) {
        super();
        this.path = path;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return path.getInputStream();
    }

    @Override
    public long contentLength() throws IOException {
        return path.size();
    }

    @Override
    public long lastModified() throws IOException {
        return path.lastModified();
    }

    @Override
    public String getFilename() {
        return path.fileName();
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public URL getURL() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public URI getURI() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public File getFile() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Optional<MediaType> getMediaType() {
        MediaType type = null;
        String ext = path.getExtension();
        if ("gif".equalsIgnoreCase(ext)) {
            type = MediaType.IMAGE_GIF;
        }
        if ("jpeg".equalsIgnoreCase(ext)) {
            type = MediaType.IMAGE_JPEG;
        }
        if ("png".equalsIgnoreCase(ext)) {
            type = MediaType.IMAGE_PNG;
        }
        if ("webp".equalsIgnoreCase(ext)) {
            type = IMAGE_WEBP;
        }
        return Optional.ofNullable(type);
    }
}
