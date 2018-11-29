package ns.coco.cocolabel.label;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("PrimaryJsonParser")
public class PrimaryJsonParser implements LabelParser {

    @Override
    public Map<String, Object> parse(String content) {
        return (Map<String, Object>)JSON.parse(content);
    }

    @Override
    public String convert(Map<String, Object> label) {

        return JSON.toJSONString(label);
    }
}
