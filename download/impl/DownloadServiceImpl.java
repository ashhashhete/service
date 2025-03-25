package com.tus.uploadservice.download.impl;

import java.io.IOException;
import java.nio.file.Files;


import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import com.tus.uploadservice.download.api.DownloadService;
import com.tus.uploadservice.util.PathFactory;



@Service
public class DownloadServiceImpl implements DownloadService {
    public ByteArrayResource stream( String fileName, String fileExt ) throws IOException {
        return new ByteArrayResource ( Files.readAllBytes( PathFactory.createFinalPath( fileName, fileExt ) ) );
    }
}
