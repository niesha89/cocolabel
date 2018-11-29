package ns.coco.cocolabel;

import com.alibaba.fastjson.JSON;
import ns.coco.cocolabel.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ImageController implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

//		uploadPath = configUtils.getByKey("appUploadPath");
        supportedSuffix = new HashSet<>();
        supportedSuffix.add("JPG");
        supportedSuffix.add("JPEG");
        supportedSuffix.add("BMP");
        supportedSuffix.add("PNG");
        supportedSuffix.add("GIF");
    }

    protected Set<String> supportedSuffix;

    @RequestMapping(value = "/img",method= RequestMethod.GET)
    public String getImg(@RequestParam("imgfile") String imgfile,
                         @RequestParam(value="h", required = false) String height,
                         @RequestParam(value="w", required = false) String width,
                         @RequestParam(value="mode", required = false) String outputMode,
                         HttpServletResponse response) {

//		String suffix = imgfile.substring(imgfile.lastIndexOf(".") + 1).toUpperCase();
        String suffix = FileUtils.getSuffix(imgfile).toUpperCase();
        if (!supportedSuffix.contains(suffix)) {
            RestUtils.returnError(response, "suffix forbidden", 400);
            return "";
        }
        if (!FileUtils.exists(imgfile)) {
            RestUtils.returnError(response, "file not found", 404);
            return null;
        }

        ChartHelper ch = new ChartHelper(imgfile);
        ch.init();

        int w = 0;
        int h = 0;
        if (StringUtils.isNotBlank(width)) {
            int pxindex = width.indexOf("px");
            if (pxindex != -1) {
                width = width.substring(0, pxindex);
            }
            w = Integer.parseInt(width);
        }
        if (StringUtils.isNotBlank(height)) {
            int pxindex = height.indexOf("px");
            if (pxindex != -1) {
                height = height.substring(0, pxindex);
            }
            w = Integer.parseInt(height);
        }

        int oriw = ch.getWidth();
        int orih = ch.getHeight();
        if (w > 0 || h > 0) {
            ch.scala(w, h);
        }

        if (outputMode == null) {
            response.setContentType("image/" + ch.getType());
            OutputStream os = null;
            try {
                os = response.getOutputStream();
                ch.output(os);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                RestUtils.returnError(response, "bytes writing failed", 400);
                return null;
            }
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ch.output(bos);
                String imgStr = ByteUtils.bytesToBase64(bos.toByteArray());
                return "{\"type\":\"" + ch.getType().toLowerCase() + "\",\"ow\":" + oriw +  ",\"oh\":" + orih +
                        ",\"data\":\"" + imgStr + "\"}";
            } catch (IOException e) {
                e.printStackTrace();
                RestUtils.returnError(response, "bytes writing failed", 400);
                return null;
            }
        }
        return null;
    }

    @RequestMapping(value = "/imglist",method=RequestMethod.GET)
    public String getImgList(@RequestParam("imgpath") String imgpath,
                             HttpServletResponse response) {

        if (!FileUtils.exists(imgpath)) {
            RestUtils.returnError(response, "file not found", 404);
            return null;
        }
        List<String> resultList = FileUtils.getList(imgpath, (File pathname) -> {
                    String suffix = FileUtils.getSuffix(pathname).toUpperCase();
                    return supportedSuffix.contains(suffix);
                }
        );
        return JSON.toJSONString(resultList);
    }


    @RequestMapping(value = "/pbase64",method=RequestMethod.POST)
    public String doPredict(HttpServletRequest request) {

        String input0 = request.getParameter("img");
        BufferedImage img = ImgUtils.loadImgFromBASE64(input0);

        return "";
    }
}
