package ns.coco.cocolabel.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class RestUtils {

    public static void returnError(HttpServletResponse response, String errmsg, int status) {
        try {
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            response.setStatus(status);
            PrintWriter pw = response.getWriter();
            pw.write(errmsg);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
