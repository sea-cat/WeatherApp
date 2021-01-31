package ro.seacat.weatherapp.common;

import android.content.Context;
import android.content.ContextWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class ImageHelper {

  public static final String IMAGE_DIRECTORY = "imageDir";
  public static final String IMAGE_EXTENSION_PNG = ".png";

  public static boolean writeResponseBody(Context context, ResponseBody body, String path) {
    File file = getFile(context, path);
    byte[] fileReader = new byte[4096];

    try (InputStream inputStream = body.byteStream(); OutputStream outputStream = new FileOutputStream(file)) {
      while (true) {
        int read = inputStream.read(fileReader);

        if (read == -1) {
          break;
        }
        outputStream.write(fileReader, 0, read);
      }

      outputStream.flush();
      return true;
    } catch (IOException e) {
      file.delete();
      e.printStackTrace();
      return false;
    }
  }

  public static File getFile(Context context, String fileName) {
    return new File(new ContextWrapper(context).getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE), fileName);
  }
}
