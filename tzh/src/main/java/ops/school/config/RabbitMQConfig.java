package ops.school.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public final static String QUEUE_WX_USER_BELL = "wx_user_bell";
    public final static String QUEUE_SENDER_BELL = "sender_bell";
    public final static String QUEUE_SCHOOL_BELL = "school_bell";
    public final static String QUEUE_PRODUCT_ADD = "product_add";
    public final static String QUEUE_ORDERS_COMPLETE = "order_complete";
    public final static String QUEUE_MIN_PROGRAM_MESSAGE = "min_program_message";


    @Bean
    public Queue queue1() {
        return new Queue(QUEUE_SENDER_BELL);
    }

    @Bean
    public Queue queue3() {
        return new Queue(QUEUE_SCHOOL_BELL);
    }

    @Bean
    public Queue queue4() {
        return new Queue(QUEUE_PRODUCT_ADD);
    }

    @Bean
    public Queue queue5() {
        return new Queue(QUEUE_WX_USER_BELL);
    }

    @Bean
    public Queue queue2() {
        return new Queue(QUEUE_MIN_PROGRAM_MESSAGE);
    }

    @Bean
    public Queue queue6() {
        return new Queue(QUEUE_ORDERS_COMPLETE);
    }

}
