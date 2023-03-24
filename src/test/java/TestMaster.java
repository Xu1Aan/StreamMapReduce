import com.xuan.workers.Mapf;
import com.xuan.workers.impl.MapWorker;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Xu1Aan
 * @Date: 2023/03/21/10:36
 * @Description:
 */
public class TestMaster {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:consumer.xml");
        Mapf mapWorker = (Mapf) context.getBean("mapWorker");
    }
}
