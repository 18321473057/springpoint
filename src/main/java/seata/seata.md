# 						seata/GTS

>1. 高性能,简单的分布式事务; 提供 **AT**,TCC,SAGA,XA事务模式



>AT: seata 阿里分布式事务框架
>
>TCC: 消息队列
>
>SAGA:  saga
>
>XA :XA
>
>他们都有一个共同点 "二阶段提交"(2PC协议)



二阶段提交 2PC

>- prepare 阶段
>- commit 阶段
>- 这个和mysql 的二阶段事务 一样啊;   redo.log-innodb,bin.log-服务层 ,先redo.log 在bin.log



协调者   参与者

>- 协调者开始prepare阶段  通知各个参参与者进行预处理    -- prepare阶段
>- 各个参与者完成预处理后,(记录undo,redo) 向协调者发送ASK:yes;
>- 接受到所有参与者的ASK后, 开始commit阶段,向各个参与者发送提交指令; -- commit阶段
>- 各个参与者开始提交事务,并向协调者发送 ASK 确认提交完成;
>- 中间任何一阶段发送异常,  整体事务都将失败;  
>- 协调者必须等待所有参与者都回复ASK提交, 事务才算完成;
>- 如果部分参与者 回应了ASK提交, 部分则没有提交;(尝试重试提交)



问题

>- 同步阻塞:  所有的参与者都ASK之前 参与者就会一直等待
>- 单点故障:  协调者挂掉 就无法服务
>- 数据不一致:  commit/rollback 阶段  ,网络等原因导致部分参与者没有接收到 c/r 指令导致数据不一致,只能人工干预 -- 无法百分之百保证事务成功



AT模式      auto Transcation  无侵入  有锁

一阶段

 >- 拦截sql,解析语义, 查询出元数据, 保存defore image,
 >- 执行sql, 保存新快照 after image,  加行锁, 
 >- 将更新的数据查询出来

二阶段

>- 删除 before image
>- 删除after image
>- 删除行锁

>- 还原before image , 回滚原来数据
>
>- 删除行锁



TCC模式  无锁 侵入性强 自己实现业务逻辑  性能更强 (ByteTCC  TCC-Transcation )

>- try    --update sattus = '执行中'   /update status status = '锁定中'
>- confirm  -- update status =  '完成' /update status status = '完成'
>- cancel  -- update status = '初始化' /update status status = '初始化'



MQ模式 : 可靠消息最终一致性

>- try  -- 准备消息
>- comfirm  -- update status  = '已支付' , 确认并发送消息
>- concel  如果发生异常则删除预备消息;
>
>