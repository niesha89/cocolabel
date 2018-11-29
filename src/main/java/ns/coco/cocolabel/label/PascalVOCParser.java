package ns.coco.cocolabel.label;

import com.alibaba.fastjson.JSON;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import ns.coco.cocolabel.exception.CocoRuntimeException;
import ns.coco.cocolabel.utils.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("PascalVOCParser")
public class PascalVOCParser implements LabelParser, InitializingBean {

    protected Configuration config = new Configuration();

    protected StringTemplateLoader stringLoader = new StringTemplateLoader();

    @Override
    public Map<String, Object> parse(String content) {

        content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + content;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("attributes", new ArrayList<String>(0));
        map.put("featurePointSize", 3);
        map.put("pointIndex", 0);
        map.put("shapeIndex", 1);
        map.put("tags", new ArrayList<String>(0));

        SAXBuilder saxBuilder = new SAXBuilder();
        InputStream in;
        try {
            Document document = saxBuilder.build(new StringReader(content));
            Element root = document.getRootElement();

            //String folder
            String filename = root.getChild("filename").getValue();
            map.put("imagename", filename);
            //String path
            //source.database

            Element size = root.getChild("size");
            Integer width = Integer.parseInt(size.getChild("width").getValue());
            Integer height = Integer.parseInt(size.getChild("height").getValue());
            Map<String, Object> sizemap = new HashMap<String, Object>();
            sizemap.put("height", height);
            sizemap.put("width", width);
            map.put("size", sizemap);
            //String segmented

            List<Element> objectlist = root.getChildren("object");

            List<Map<String, Object>> shapeslist = new ArrayList<>(objectlist.size());
            map.put("shapes", shapeslist);

            int count = 1011;
            for (Element item : objectlist) {
                Map<String, Object> shape = new HashMap<String, Object>();
                shape.put("attributes", new ArrayList<String>(0));
                shape.put("featurePoints", new ArrayList<String>(0));
                shape.put("tags", new ArrayList<String>(0));
                shape.put("defaultZoomScale", 1);
                shape.put("type", "rect");
                shape.put("zoomScale", "1");
                shape.put("id", "CocoRect" + (count++));

                String name = item.getChild("name").getValue();
                String pose = item.getChild("pose").getValue();
                String truncated = item.getChild("truncated").getValue();
                String difficult = item.getChild("difficult").getValue();
                Element bndbox = item.getChild("bndbox");

                shape.put("category", name);
                shape.put("label", pose);

                int xmin = Integer.parseInt(bndbox.getChild("xmin").getValue());
                int ymin = Integer.parseInt(bndbox.getChild("ymin").getValue());
                int xmax = Integer.parseInt(bndbox.getChild("xmax").getValue());
                int ymax = Integer.parseInt(bndbox.getChild("ymax").getValue());
                int itemheight = ymax-ymin;
                int itemwidth = xmax-xmin;

                List<Integer> points = new ArrayList<>(4);
                points.add(xmin);
                points.add(ymin);
                points.add(itemwidth);
                points.add(itemheight);
                shape.put("points", points);

                Map<String, Object> bbox = new HashMap<String, Object>();
                bbox.put("x",xmin);
                bbox.put("y",ymin);
                bbox.put("h",itemheight);
                bbox.put("height",itemheight);
                bbox.put("w",itemwidth);
                bbox.put("width",itemwidth);
                //bbox.put("cx",ymin);
                //bbox.put("cy",ymin);

                shape.put("bbox", bbox);
                shapeslist.add(shape);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        config.setDefaultEncoding("UTF-8");

        InputStream is = new ClassPathResource("templates/PascalVOC.ftl").getInputStream();
        String templateContent = FileUtils.readFileByLines(is);
        is.close();
        stringLoader.putTemplate("PascalVOC",templateContent);
        config.setTemplateLoader(stringLoader);
    }

    @Override
    public String convert(Map<String, Object> label) {

//        List<String> attributeList = (List<String>)label.get("attributes");
//
//        Map<String, Object> size = (Map<String, Object>)label.get("size");
//        int height = (Integer)size.get("height");
//        int width = (Integer)size.get("width");
//        int depth = 3;
        try {
            Template template = config.getTemplate("PascalVOC");
            StringWriter sw = new StringWriter();
            template.process(label, sw);
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CocoRuntimeException("IOException occured while writing", e);
        } catch (TemplateException e) {
            e.printStackTrace();
            throw new CocoRuntimeException("TemplateException occured while writing", e);
        }
    }


    public static void main(String[] args) throws Exception{
        PascalVOCParser p = new PascalVOCParser();
        p.afterPropertiesSet();
        System.out.println(JSON.toJSONString(p.parseFile("E:\\aidata\\imgclass\\person\\1.xml")));
    }
}
