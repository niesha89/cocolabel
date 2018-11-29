package ns.coco.cocolabel.label;

import ns.coco.cocolabel.exception.CocoRuntimeException;
import ns.coco.cocolabel.utils.FileUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public interface LabelParser {

    default Map<String, Object> parseFile(String filename) {
        String content = FileUtils.readFileByLines(filename);
        return parse(content);
    }

    Map<String, Object> parse(String content);

    String convert(Map<String, Object> label);

    default void save(String filename, Map<String, Object> label) {
        String content = convert(label);

        try {
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new CocoRuntimeException("Exception occured while writing file", ioe);
        }
    }
}
