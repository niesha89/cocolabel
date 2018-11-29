package ns.coco.cocolabel.utils;

import ns.coco.cocolabel.exception.CocoRuntimeException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static List<String> getList(String path, FileFilter fileFilter) {
        File file = new File(path);

        File[] files = file.listFiles(fileFilter);
        List<String> list = new ArrayList<>(files.length);
        for (File f : files) {
            list.add(f.getName());
        }

        return list;
    }

    public static String getSuffix(File path) {
        String name = path.getName();
        int i = name.lastIndexOf(".");
        if (i != -1) {
            return name.substring(i + 1);
        } else {
            return "";
        }
    }
    public static String getSuffix(String path) {
        File file = new File(path);
        return getSuffix(file);
    }

    /**
     *
     */
    public static String readFileByLines(String fileName) {

        StringBuffer sb = new StringBuffer();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString).append("\n");
            }
            reader.close();

            return sb.toString();
        } catch (IOException e) {
            throw new CocoRuntimeException("Exception occurs while reading file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    public static String readFileByLines(InputStream is) {
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = null;
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString).append("\n");
            }
            reader.close();

            return sb.toString();
        } catch (IOException e) {
            throw new CocoRuntimeException("Exception occurs while reading file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public static void writeFile(String file, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes("utf-8"));
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new RuntimeException(ioe);
        }
    }
}
