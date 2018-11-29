package ns.coco.cocolabel;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAware;
import com.alibaba.fastjson.JSON;
import ns.coco.cocolabel.label.LabelParser;
import ns.coco.cocolabel.utils.FileUtils;
import ns.coco.cocolabel.utils.RestUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class LabelController implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @RequestMapping(value = "/saveLabel",method= RequestMethod.POST)
    public String saveLabel(@RequestParam("workpath") String workpath,
                            @RequestParam(value = "type", defaultValue = "PascalVOC") String saveType,
                            @RequestParam("labeldata") String labeldata,
                            HttpServletResponse response) {
        if (!FileUtils.exists(workpath)) {
            RestUtils.returnError(response, "workpath not found", 400);
            return null;
        }

        Map<String, Object> map = (Map<String, Object>)JSON.parse(labeldata);
        String imageName = (String)map.get("imagename");

//        try {
//            FileUtils.writeFile(workpath + imageName + "_" + saveType + ".xml", JSON.toJSONString(map));
//        } catch (RuntimeException re) {
//            re.printStackTrace();
//            RestUtils.returnError(response, "exception occurs while writing file", 400);
//        }
        LabelParser lp = (LabelParser)applicationContext.getBean(saveType + "Parser");
        lp.save(workpath + imageName + "_" + saveType + ".xml", map);

        return "saved";
    }

    @RequestMapping(value = "/loadLabel",method= RequestMethod.POST)
    public String loadLabel(@RequestParam("workpath") String workpath,
                            @RequestParam(value = "type", defaultValue = "PascalVOC") String saveType,
                            @RequestParam("imgname") String imgname,
                            HttpServletResponse response) {
        if (!FileUtils.exists(workpath)) {
            RestUtils.returnError(response, "workpath not found", 400);
            return null;
        }

        String labelFileName = workpath + imgname + "_" + saveType + ".xml";
        if (!new File(labelFileName).exists()) {
            return "{}";
        }

//        String labelstr = FileUtils.readFileByLines(labelFileName);
        LabelParser lp = (LabelParser)applicationContext.getBean(saveType + "Parser");
        return JSON.toJSONString(lp.parseFile(labelFileName));
    }

    @RequestMapping(value = "/saveDefaultSettings",method= RequestMethod.GET)
    public String saveDefaultSettings(@RequestParam("workpath") String workpath,
                                      @RequestParam("settings") String settings,
                                      @RequestParam(value = "settingsFilename", defaultValue = "workspace.json") String settingsFilename,
                                      HttpServletResponse response) {
        if (!FileUtils.exists(workpath)) {
            RestUtils.returnError(response, "workpath not found", 400);
            return null;
        }

        try {
            FileUtils.writeFile(workpath + settingsFilename, settings);
        } catch (RuntimeException re) {
            re.printStackTrace();
            RestUtils.returnError(response, "exception occurs while writing file", 400);
        }

        return "saved";
    }

    @RequestMapping(value = "/loadDefaultSettings",method= RequestMethod.GET)
    public String loadDefaultSettings(@RequestParam("workpath") String workpath,
                                      @RequestParam(value = "settingsFilename", defaultValue = "workspace.json") String settingsFilename,
                                      HttpServletResponse response) {
        if (!FileUtils.exists(workpath)) {
            RestUtils.returnError(response, "workpath not found", 400);
            return null;
        }

        if (!FileUtils.exists(workpath + settingsFilename)) {
            return "{}";
        }

        String content = null;
        try {
            content = FileUtils.readFileByLines(workpath + settingsFilename);
        } catch (RuntimeException re) {
            re.printStackTrace();
            RestUtils.returnError(response, "exception occurs while writing file", 400);
        }
        return content;
    }
}
