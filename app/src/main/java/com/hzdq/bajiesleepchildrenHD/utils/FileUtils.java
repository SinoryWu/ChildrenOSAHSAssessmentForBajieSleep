package com.hzdq.bajiesleepchildrenHD.utils;

import java.util.HashMap;
import java.util.Iterator;

public class FileUtils {
    public static String sFileExtensions;

    // Audio file types
    public static final int FILE_TYPE_MP3 = 1;
    public static final int FILE_TYPE_M4A = 2;
    public static final int FILE_TYPE_WAV = 3;
    public static final int FILE_TYPE_AMR = 4;
    public static final int FILE_TYPE_AWB = 5;
    public static final int FILE_TYPE_WMA = 6;
    public static final int FILE_TYPE_OGG = 7;
    public static final int FILE_TYPE_RMVB = 8;
    private static final int FIRST_AUDIO_FILE_TYPE = FILE_TYPE_MP3;
    private static final int LAST_AUDIO_FILE_TYPE = FILE_TYPE_RMVB;

    // MIDI file types
    public static final int FILE_TYPE_MID = 11;
    public static final int FILE_TYPE_SMF = 12;
    public static final int FILE_TYPE_IMY = 13;
    private static final int FIRST_MIDI_FILE_TYPE = FILE_TYPE_MID;
    private static final int LAST_MIDI_FILE_TYPE = FILE_TYPE_IMY;

    // Video file types
    public static final int FILE_TYPE_TS = 15;
    public static final int FILE_TYPE_MOV = 16;
    public static final int FILE_TYPE_H264 = 17;
    public static final int FILE_TYPE_H263 = 18;
    public static final int FILE_TYPE_MKV = 19;
    public static final int FILE_TYPE_ASF = 20;
    public static final int FILE_TYPE_MP4 = 21;
    public static final int FILE_TYPE_M4V = 22;
    public static final int FILE_TYPE_3GPP = 23;
    public static final int FILE_TYPE_3GPP2 = 24;
    public static final int FILE_TYPE_WMV = 25;
    public static final int FILE_TYPE_AVI = 26;
    public static final int FILE_TYPE_MPEG = 27;
    public static final int FILE_TYPE_FLV = 28;
    public static final int FILE_TYPE_OGV = 29;
    public static final int FILE_TYPE_WEBM = 30;
    private static final int FIRST_VIDEO_FILE_TYPE = FILE_TYPE_TS;
    private static final int LAST_VIDEO_FILE_TYPE = FILE_TYPE_WEBM;

    // Image file types
    public static final int FILE_TYPE_JPEG = 31;
    public static final int FILE_TYPE_GIF = 32;
    public static final int FILE_TYPE_PNG = 33;
    public static final int FILE_TYPE_BMP = 34;
    public static final int FILE_TYPE_WBMP = 35;
    private static final int FIRST_IMAGE_FILE_TYPE = FILE_TYPE_JPEG;
    private static final int LAST_IMAGE_FILE_TYPE = FILE_TYPE_WBMP;

    // Playlist file types
    public static final int FILE_TYPE_M3U = 41;
    public static final int FILE_TYPE_PLS = 42;
    public static final int FILE_TYPE_WPL = 43;
    private static final int FIRST_PLAYLIST_FILE_TYPE = FILE_TYPE_M3U;
    private static final int LAST_PLAYLIST_FILE_TYPE = FILE_TYPE_WPL;

    public static final int FILE_TYPE_DOC = 51;
    public static final int FILE_TYPE_DOCX = 52;
    public static final int FILE_TYPE_XLS = 53;
    public static final int FILE_TYPE_XLSX = 54;
    public static final int FILE_TYPE_TXT = 55;
    public static final int FILE_TYPE_PPT = 56;
    public static final int FILE_TYPE_PPTX = 57;
    public static final int FILE_TYPE_PDF = 58;

    //静态内部类
    static class MediaFileType {

        int fileType;
        String mimeType;

        MediaFileType(int fileType, String mimeType) {
            this.fileType = fileType;
            this.mimeType = mimeType;
        }
    }

    private static HashMap<String, MediaFileType> sFileTypeMap
            = new HashMap<String, MediaFileType>();
    private static HashMap<String, Integer> sMimeTypeMap
            = new HashMap<String, Integer>();

    static void addFileType(String extension, int fileType, String mimeType) {
        sFileTypeMap.put(extension, new MediaFileType(fileType, mimeType));
        sMimeTypeMap.put(mimeType, new Integer(fileType));
    }

    static {
        addFileType("MP3", FILE_TYPE_MP3, "audio/x-mpeg");
        addFileType("MP2", FILE_TYPE_MP3, "audio/x-mpeg");
        addFileType("M4A", FILE_TYPE_M4A, "audio/mp4a-latm");
        addFileType("M4B", FILE_TYPE_M4A, "audio/mp4a-latm");
        addFileType("M4P", FILE_TYPE_M4A, "audio/mp4a-latm");
        addFileType("WAV", FILE_TYPE_WAV, "audio/x-wav");
        addFileType("AMR", FILE_TYPE_AMR, "audio/amr");
        addFileType("AWB", FILE_TYPE_AWB, "audio/amr-wb");
        addFileType("WMA", FILE_TYPE_WMA, "audio/x-ms-wma");
        addFileType("OGG", FILE_TYPE_OGG, "application/ogg");
        addFileType("M3U", FILE_TYPE_M3U, "audio/x-mpegurl");
        addFileType("PLS", FILE_TYPE_PLS, "audio/x-scpls");
        addFileType("MID", FILE_TYPE_MID, "audio/midi");
        addFileType("XMF", FILE_TYPE_MID, "audio/midi");
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi");
        addFileType("SMF", FILE_TYPE_SMF, "audio/sp-midi");
        addFileType("IMY", FILE_TYPE_IMY, "audio/imelody");
        addFileType("RMVB", FILE_TYPE_RMVB, "audio/x-pn-realaudio");

        addFileType("MP4", FILE_TYPE_MP4, "video/mp4");
        addFileType("M4V", FILE_TYPE_M4V, "video/mp4");
        addFileType("3GP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3GPP", FILE_TYPE_3GPP, "video/3gpp");
        addFileType("3G2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("3GPP2", FILE_TYPE_3GPP2, "video/3gpp2");
        addFileType("WMV", FILE_TYPE_WMV, "video/x-ms-wmv");
        addFileType("ASF", FILE_TYPE_ASF, "video/x-ms-asf");
        addFileType("AVI", FILE_TYPE_AVI, "video/video/x-msvideo");
        addFileType("MPEG", FILE_TYPE_MPEG, "video/mpeg");
        addFileType("MPG", FILE_TYPE_MPEG, "video/mpeg");
        addFileType("MPE", FILE_TYPE_MPEG, "video/mpeg");
        addFileType("FLV", FILE_TYPE_FLV, "video/x-flv");
        addFileType("F4V", FILE_TYPE_FLV, "video/x-flv");
        addFileType("OGV", FILE_TYPE_OGV, "video/ogg");
        addFileType("WEBM", FILE_TYPE_WEBM, "video/webm");
        addFileType("TS", FILE_TYPE_TS, "video/MP2T");
        addFileType("MOV", FILE_TYPE_MOV, "video/quicktime");
        addFileType("H264", FILE_TYPE_H264, "video/h264");
        addFileType("H263", FILE_TYPE_H263, "video/h263");
        addFileType("MKV", FILE_TYPE_MKV, "video/x-matroska");

        addFileType("JPG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("JPEG", FILE_TYPE_JPEG, "image/jpeg");
        addFileType("GIF", FILE_TYPE_GIF, "image/gif");
        addFileType("PNG", FILE_TYPE_PNG, "image/png");
        addFileType("BMP", FILE_TYPE_BMP, "image/x-ms-bmp");
        addFileType("WBMP", FILE_TYPE_WBMP, "image/vnd.wap.wbmp");

        addFileType("WPL", FILE_TYPE_WPL, "application/vnd.ms-wpl");
        addFileType("DOC", FILE_TYPE_DOC, "application/msword");
        addFileType("DOCX", FILE_TYPE_DOCX, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        addFileType("XLS", FILE_TYPE_XLS, "application/vnd.ms-excel");
        addFileType("XLSX", FILE_TYPE_XLSX, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        addFileType("TXT", FILE_TYPE_TXT, "text/plain");
        addFileType("PPT", FILE_TYPE_PPT, "application/vnd.ms-powerpoint");
        addFileType("PPTX", FILE_TYPE_PPTX, "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        addFileType("PDF", FILE_TYPE_PDF, "application/pdf");
        // compute file extensions list for native Media Scanner
        StringBuilder builder = new StringBuilder();
        Iterator<String> iterator = sFileTypeMap.keySet().iterator();

        while (iterator.hasNext()) {
            if (builder.length() > 0) {
                builder.append(',');
            }
            builder.append(iterator.next());
        }
        sFileExtensions = builder.toString();
    }

    public static final String UNKNOWN_STRING = "<unknown>";

    public static boolean isDocFileType(int fileType) {
        return ((fileType >= FILE_TYPE_DOC &&
                fileType <= FILE_TYPE_PDF));
    }

    public static boolean isAudioFileType(int fileType) {
        return ((fileType >= FIRST_AUDIO_FILE_TYPE &&
                fileType <= LAST_AUDIO_FILE_TYPE) ||
                (fileType >= FIRST_MIDI_FILE_TYPE &&
                        fileType <= LAST_MIDI_FILE_TYPE));
    }

    public static boolean isVideoFileType(int fileType) {
        return (fileType >= FIRST_VIDEO_FILE_TYPE &&
                fileType <= LAST_VIDEO_FILE_TYPE);
    }

    public static boolean isImageFileType(int fileType) {
        return (fileType >= FIRST_IMAGE_FILE_TYPE &&
                fileType <= LAST_IMAGE_FILE_TYPE);
    }

    public static boolean isPlayListFileType(int fileType) {
        return (fileType >= FIRST_PLAYLIST_FILE_TYPE &&
                fileType <= LAST_PLAYLIST_FILE_TYPE);
    }

    public static MediaFileType getFileType(String path) {
        int lastDot = path.lastIndexOf(".");
        if (lastDot < 0)
            return null;
        return sFileTypeMap.get(path.substring(lastDot + 1).toUpperCase());
    }

    //根据视频文件路径判断文件类型
    public boolean isVideoFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isVideoFileType(type.fileType);
        }
        return false;
    }

    //根据音频文件路径判断文件类型
    public boolean isAudioFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isAudioFileType(type.fileType);
        }
        return false;
    }

    //根据图片文件路径判断文件类型
    public static boolean isImageFileType1(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isImageFileType(type.fileType);
        }
        return false;
    }

    //根据图片文件路径判断文件类型
    public boolean isDocFileType(String path) {
        MediaFileType type = getFileType(path);
        if (null != type) {
            return isDocFileType(type.fileType);
        }
        return false;
    }

    //根据mime类型查看文件类型
    public static int getFileTypeForMimeType(String mimeType) {
        Integer value = sMimeTypeMap.get(mimeType);
        return (value == null ? 0 : value.intValue());
    }


}
