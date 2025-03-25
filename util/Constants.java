package com.tus.uploadservice.util;


public class Constants {
   
 // public static final String TMP_DIR = "/mnt/adani_sra/video/";

//	public static final String TMP_DIR = System.getProperty("java.io.tmpdir") + "S_1001/U_1001/distometer_video/";

    public static final long PART_SIZE = 1054 * 1054 * 20;

    public static final String TEST_FILENAME = "testFilename";
    
    public static final String TEST_FILEEXT = ".pdf";

    public static final String TEST_FILEDIR = "testFileDirectory";

    public static final String TEST_USERNAME = "testuser";

    public static final Long TEST_UPLOADOFFSET = 0L;

    public static final Long TEST_UPLOADOFFSET_INC = 2L;

    public static final Long TEST_UPLOADOFFSET_INC_COMPLETE = 5L;

    public static final Long TEST_UPLOADLENGTH = 20L;

    public static final Long TEST_UPLOAD_PART_FILESIZE = 20L;

    public static final Long TEST_UPLOAD_PART_COUNT = 5L;
    private static String TMP_DIR;
    public static String getTmpDir() {
        return TMP_DIR;
    }

    public static void setTmpDir(String tmpDir) {
        TMP_DIR = tmpDir;
    }

}

