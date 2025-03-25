package com.tus.uploadservice.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.tus.uploadservice.upload.api.FileInfo;
import com.tus.uploadservice.upload.impl.PartInfo;

public class PathFactory {

    public static Path createDirectoryPath(String fileName) {

    	//System.out.println(TMP_DIR+fileName);
        return Paths.get(Constants.getTmpDir());
    }

    public static Path createPartPath(FileInfo partInfo) {
    	
        return Paths.get(
        		Constants.getTmpDir(),
                partInfo.getFileName() 
        );
    }

    public static Path createFinalPath(String fileName, String fileExt) {
        return Paths.get(Constants.getTmpDir(), fileName + fileExt);
    }

    protected PathFactory() {
    }
}
