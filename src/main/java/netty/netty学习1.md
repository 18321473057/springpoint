### netty是什么

>- 异步
>- 事件驱动
>- 网络框架
>- tcp/ip  -->   jdk原生   --> NIO  --> netty



### netty 能做什么

>RPC异步高性能框架的基石  double
>
>网络游戏 
>
>大数据  AVRO  通讯   Flink , spark





## IO模型---BIO, NIO, AIO



#### BIO

>- 一个连接需要一个线程来处理, 连接过多就会导致过多的线程;  连接如果不做事情,还是会占用一个线程;
>- 并且会阻塞



![image-20220412143618983](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20220412143618983.png)



#### NIO  连接较短 聊天 弹幕

>-  服务端一个线程处理多个连接
>-  连接请求注册在多路复用器上  既selector
>-  轮训有IO请求的连接,再对请求处理
>
>BIO 以流的形式进行处理-- 阻塞         基于字节和字符流处理
>
>NIO以块的形式进行处理-- 非阻塞      基于通道和缓冲区处理

![image-20220412144138713](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20220412144138713.png)



### AIO  连接时间长的  相册



### NIO 组件; 事件驱动 面向缓冲区编程;

>- selector : 选择器
>- channel: 双向通道
>- buffer: 双向缓冲区



>- 每一个channel 都对应一个buffer
>- 一个selector对应多个channel(通道或连接)
>- 一个线程对应一个selector
>- selector根据Event事件 在channel上切换
>
>



![image-20220412164353141](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20220412164353141.png)





### Buffer  : 内部拥有一个数组 

>capacity  容量
>
>limit    当前存储的终点
>
>position  当前指针
>
>mark    标记



### MappedByteBuffer :  

>文件直接在内存(堆外内存)修改,操作系统不需要拷贝一次



### Channel

>- fileChannel   : 文件读写
>- datagramChannel  : UDP 数据读写
>- serverSockerChannel  : TCP 数据读写
>- sockerChannel   :  TCP 数据读写
>
>serverSockerChannel 生成sockerChannel,再由sockerChannel 去处理连接请求
>
>java 流中都内置了通道



### Scattering 分散 /   Gathering聚合

>- 将数据写入buffer时, 第一个写满后,可以依次写入;
>- 从buffer中读取数据时, 第一个读完后, 依次读取第二个;



### selector  选择器

>- 多个通道以事件的方式注册到selector上
>- selector可以检测到通道上是否有事件发生
>- 只有通道正真有读写事件时, 才会进行读写

>protected Set<SelectionKey> selectedKeys = new HashSet();  # 可以拿到channel
>
>select() 阻塞, 必须有一个事件通道才会返回
>
>select(1000)  阻塞1000毫秒,1000毫秒后返回
>
>wakeup()     唤醒 selector 立刻返回
>
>selectNow  非阻塞, 直接返回





![image-20220413110857188](C:\Users\Lenovo\AppData\Roaming\Typora\typora-user-images\image-20220413110857188.png)

###  telnet



### 零拷贝 : 指的的没有CPU copy;;;; DMA  copy     cpu copy

>传统IO: 4次拷贝, 3次切换
>
>mmap:   3次拷贝,3次切换  内核缓存与用户缓存共享
>
>sendfile:  3次拷贝,2次切换    不涉及用户态,直接在内核态拷贝
>
>linux2.4   sendFIle:  2 次拷贝 ,2次切换 





### 为什么要用netty

>- NIO编程复杂
>- 需要多线程和网络编程知识
>- 工作量大
>
>异步 事件驱动 网络框架 快速开发高性能服务器端,客户端



### netty架构设计

>- 传统IO 模式: 一线程对应一连接, 所有线程都会阻塞;
>
>- reactor 模式(反应器模式)-   一个线程对应多个连接, 只有当前这个线程阻塞,数据由后面的线程池处理  ;
>
>  IO复用+线程池



###  监听连接事件->监听读写事件->handler处理真正业务 

>- 单reactor  单线程 ::        监听连接事件/监听读写事件/handler处理真正业务 都由mian线程去执行
>
>- 单reactor  多线程::         监听连接事件/监听读写事件 由main线程去执行,  handler业务由线程池去处理并返回处理结果.    将耗时最多的业务处理分级独立出来;
>
>- 主从reactor 多线程::   监听连接事件由mian线程去执行 , 大量的监听读写事件 由一个线程池去处理;  handler业务由另外一个线程池去处理并返回处理结果; 这样就将模型分离成三级;





### BossGroup     WoekerGroup

>- BossGroup ::  维护一个selector选择器 只关注accecpt 连接事件, 获取对应的socketChannel ,封装成NIOSocketChannel并注册到WockerGroup;
>
>- WoekerGroup:: 监听selector中自己更关心的事件,并交由handler 处理
>- BossGroup     和 WoekerGroup  线程组默认是cpu核数*2



管道与通道

管道包含处理器 拦截,过滤机制



### 异步模型: IO处理都是异步处理, 直接返回channelFuture对象

> - Future-Listener 机制:  主动获取 或者通过通知机制



### netty  重要对象

>NioEventLoopGroup  // 工作组
>
>serverBootstrap  //服务端启动类
>Bootstrap    //客户端启动类
>
>ChannelInitializer  // 管道初始化对象
>
>SocketChannel    //管道
>
>ChannelPipeline  // 通道
>
>ChannelHandlerContext  //
>
>HttpServerCodec   //服务编码器
>
>ChannelInboundHandlerAdapter  //入栈消息处理
>ChannelOutboundHandlerAdapter  //出站消息处理
>
>ChannelFuture  异步结果



### ChannelInitializer->SocketChannel (管道)-> <-ChannelPipeline (通道)->ChannelHandler

ChannelPipeline  :  因为请求时一个读写过程, 数据读的时候是正向拦截处理流程, 写得时候是反向拦截处理流程; 所以他维护的是一个双向链表

ChannelPipeline  : 维护了handler集合;,是一组高级形式的拦截器; handler 处理channel 的入栈和出栈

ChannelPipeline 和 SocketChannel 相互拥有

### ChannelPipeline 

>- head : 头指针
>- tail    : 尾指针
>
>

### ChannelHandlerContext

>- 封装了 ChannelHandler
>-  拥有next 和perv   本生也是一个链表
>- 拥有pipeline 对象
>
>

### NioEventLoopGroup

>- 维护了一个selector,selector中注册着serverSockerChanne;
>- 默认创建cpu核数*2的线程, 循环使用
>- SO_BACKLOG   当线程不够的时候, 允许排队的请求数量
>
>- SO_KEEPALIVE  boolean 保持连接

### UnPooled

>- 不需要flip反转
>
>- ReadIndex ,writerIndex ,Capacity    ; 空间被分成三等份, 维护着  已读,可读,可写 三段空间
>- 容量是实际容量的三倍,



### 业务数据->encoder编码器-> 传输二进制字节码->decoder解码-> 业务数据

>- 无法跨语言, java的服务端,就需要java的客户端
>- 序列化后体积是二进制编码的5倍

### Protobuf

>- 轻便高效的结构化数据存储, 可以实现序列换,适合做RPC(远程过程调用)

### **http+json 逐渐转换成 tcp+protobuf**

>protoType
>
>- protobuf以message的方式管理数据;
>- 支持跨平台,跨语言 ,支持 C++,C#,Java python
>
>- 以.proto文件描述编写
>- 通过protoc.exe 编译器 将.proto生成.java文件

xxx.proto->protoc.exe->xxx.java ->protobufEncoder ->二进制传输-> protobufDecoder->业务对象



>```xml
><dependency>
>    <groupId>com.google.protobuf</groupId>
>    <artifactId>protobuf-java</artifactId>
>    <version>3.6.1</version>
></dependency>
>```

>- 编译工具下载 https://github.com/protocolbuffers/protobuf/releases/tag/v3.6.1
>- 命令     cd C:\Users\Lenovo\Desktop\protoc-3.6.1-win32\bin
>- .proto文件拷贝过来后 protoc.exe --java_out=. msg.proto   生成.java文件
>



### ChannelHandler  入栈/出栈的入口  包括编解码器

>- ChannelInboundHandler      入栈    字节码 -> 其他类型
>- ChannelOutboundHandler  出栈   其他类型 -> 字节码
>
>

### 自定义转码Encoder  粘包/拆包   

>- 二进制传输 转换明文时, 可能会多,可能会少, 所以要指定多少位 转换一次;
>- netty 自定义了许多类型的encoder/decoder, 父类messageToByteEncoder 会判断当前数据类型是否属于自己处理的类型; 是: 直接处理; 不是传递给下一个;
>- 入栈解码, 出栈编码

### TCP 粘包 的原因

>- 面向流的通信是无消息保护边界的
>- Nagle算法: 将多次,间隔小,数据量小的数据, 合并成一个数据块.   以数据块的方式发送;
