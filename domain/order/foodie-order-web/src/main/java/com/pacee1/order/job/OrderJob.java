package com.pacee1.order.job;

import com.pacee1.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Created by pace
 * @Date 2020/6/12 16:55
 * @Classname OrderJob
 */
@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     * 使用定时任务关闭，缺点较多
     * 1.时间差
     *  10：50下单，会在12点才能检查出来，超出关闭时间
     * 2.不支持集群
     *  只能使用一个单独节点，来运行所有定时任务
     * 3.全表查询，影响数据库性能
     * 定时任务只适合小型项目
     *
     * 后期使用MQ来优化自动关闭任务（延时队列）
     */
    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoCloseOrder(){
        orderService.closeOrder();
        System.out.println(new Date());
    }
}
