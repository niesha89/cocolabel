package ns.coco.cocolabel;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication
@RestController
public class CocoLabelApplication implements InitializingBean, ApplicationContextAware {

	public static void main(String[] args) {
		SpringApplication.run(CocoLabelApplication.class, args);
	}

	protected ApplicationContext applicationContext;

	protected String uploadPath;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

	}
}
