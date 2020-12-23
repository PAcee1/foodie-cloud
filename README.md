## 天天吃货商城项目

**该工程为微服务实现，单体实现项目地址为**：https://github.com/PAcee1/foodie

开发中，未完待续

### 项目介绍

该项目为SpringCloud微服务版本天天吃货在线购物平台。包括首页门户、商品推荐、商品搜索、商品展示、商品评价、购物车、订单流程、用户中心等功能。SpringCloud Greenwich.SR1+SpringBoot 2.1.5.RELEASE版本。集成Redis缓存、消息队列(RabbitMq、RocketMQ)、分布式搜索ElasticSearch、分布式文件系统（FastDFS、OSS）、分布式锁、分库分表（MyCat、Sharding-Jdbc）、分布式全局ID、分布式事务、分布式限流、监控、ELK日志搜索等

### 项目功能

![输入图片说明](/resource/image/151140_08bddc92_1185227.jpeg)

### 项目架构

![输入图片说明](/resource/image/144526_dff4f76c_1185227.jpeg)

### 服务

| **组件名称** | **application name** | **端口** |
| ------------ | -------------------- | -------- |
| 注册中心     | registry-center      | 22222    |
| 配置中心     | config-server        | 20003    |
| 服务网关     | platform-gateway     | 20004    |
| Hystrix大盘  | hystrix-dashboard    | 20002    |
| Turbine      | hystrix-turbine      | 20001    |
|              |                      |          |
|              |                      |          |

