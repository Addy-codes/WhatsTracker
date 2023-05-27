package com.godthablab.wtracker.shared;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.godthablab.wtracker.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.godthablab.wtracker.shared.AssetType.TYPE_GIF;
import static com.godthablab.wtracker.shared.AssetType.TYPE_IMAGE;
import static com.godthablab.wtracker.shared.AssetType.TYPE_STATUS_IMAGE;
import static com.godthablab.wtracker.shared.AssetType.TYPE_STATUS_VIDEO;
import static com.godthablab.wtracker.shared.AssetType.TYPE_VIDEO;

public class FileOperation {


public static boolean saveFileLocalDir(String filePath, int type) {
        File dst,src;
        src = new File(filePath);
        switch (type) {
                case TYPE_IMAGE :  dst = new File(C.WW_IMAGES,src.getName());
                                        break;
                case TYPE_VIDEO : dst = new File(C.WW_VIDEOS,src.getName());
                                        break;
                case TYPE_GIF : dst = new File(C.WW_GIF,src.getName());
                                        break;
                case TYPE_STATUS_IMAGE : dst = new File(C.WW_STATUS_IMAGES,src.getName());
                                        break;
                case TYPE_STATUS_VIDEO: dst = new File(C.WW_STATUS_VIDEOS,src.getName());
                                        break;
                default: return false;
        }

        try {
                copy(src,dst);
                return true;
        } catch (IOException e) {
                e.printStackTrace();
                return false;
        }
}

static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
                try (OutputStream out = new FileOutputStream(dst)) {
                        // Transfer bytes from in to out
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                        }
                }
        }
}

public static void move(File src, File dst) throws IOException {
        copy(src,dst);
        src.delete();
}


public static void rename(File originalFile, String newName) {
        originalFile.renameTo(new File(originalFile.getPath(),newName));
}

public static void delete(File file) {
        file.delete();
}

public static File[] getFiles(String folderName) {
        File folder = new File(folderName);
        if(!folder.exists()) {
                return new File[0];
        }

        File[] files =  folder.listFiles();
        if( files!=null )
                return files;

        return new File[0];
}

public static File[] findFileRecursive(File folderOrFile) {
        File[] files =  folderOrFile.listFiles();
        if( files == null )
                return new File[0];
        ArrayList<File> fileList = new ArrayList<>();
        for(File subFile : files) {
                // Ignore Sent folder since we are reading independently
                if(subFile.isDirectory() && !subFile.getName().equalsIgnoreCase("Sent") ) {
                      File[] subFileResult = findFileRecursive(subFile);
                      fileList.addAll(Arrays.asList(subFileResult));
                } else {
                        fileList.add(subFile);
                }
        }

        File[] content = new File[fileList.size()];
        return fileList.toArray(content);
}

public static void shareMultiple(List<File> files, Context context){
        ArrayList<Uri> uris = new ArrayList<>();
        for(File file: files){
               // uris.add(Uri.fromFile(file));
             // Due to Android P
             Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
             uris.add(uri);
        }
        final Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.complete_action) ));
}


public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "KMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format(Locale.US,"%.1f %sB", bytes / Math.pow(unit, exp), pre);
}

public static void openFile(File url, Context context) {

        try {

                // Uri uri = Uri.fromFile(url);
                Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", url);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                        // Word document
                        intent.setDataAndType(uri, "application/msword");
                } else if (url.toString().contains(".pdf")) {
                        // PDF file
                        intent.setDataAndType(uri, "application/pdf");
                } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                        // Powerpoint file
                        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                        // Excel file
                        intent.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                        // WAV audio file
                        intent.setDataAndType(uri, "application/x-wav");
                } else if (url.toString().contains(".rtf")) {
                        // RTF file
                        intent.setDataAndType(uri, "application/rtf");
                } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                        // WAV audio file
                        intent.setDataAndType(uri, "audio/x-wav");
                } else if (url.toString().contains(".gif")) {
                        // GIF file
                        intent.setDataAndType(uri, "image/gif");
                } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                        // JPG file
                        intent.setDataAndType(uri, "image/jpeg");
                } else if (url.toString().contains(".txt")) {
                        // Text file
                        intent.setDataAndType(uri, "text/plain");
                } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                        url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                        // Video files
                        intent.setDataAndType(uri, "video/*");
                } else {
                        intent.setDataAndType(uri, "*/*");
                }

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_LONG).show();
        }
}


}
