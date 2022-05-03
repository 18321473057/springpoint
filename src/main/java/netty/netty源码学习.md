# Netty核心代码

>```java
>NioEventLoopGroup bossEvent = new NioEventLoopGroup(1);
>NioEventLoopGroup workerEvent = new NioEventLoopGroup();
>```
>
>BossGroup 负责Tcp连接建立,workerEvent 负责解码编码通讯等操作

>EventLoopGroup 是事件循环线程组,含有多个EventLoop,可以注册channel
>
>EventLoopGroup  可以指定线程数,若不指定,则创建2*核数个线程
>
>```
>private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));  //默认创建线程数量
>```

>```
>this.children = new EventExecutor[nThreads];  //线程由children 保存
>this.children[i] = this.newChild((Executor)executor, args);  //并赋予了执行器
>
>```



### ServerBootstrap //程序启动-配置类

>```
>group(bossEvent, workerEvent) //讲bossEvent 交由父类处理,workerEvent设置成自己的childGroup
>		super.group(parentGroup);
>        this.childGroup = childGroup;
>```

>```
>.channel(NioServerSocketChannel.class) //使用channelFactory.newChannel 反射创建获得
>.handler(new LoggingHandler(LogLevel.INFO)) loghandler交给bossEvent
>.childHandler() handler 交由workerEvent 处理
>```

>```
>.option(ChannelOption.SO_BACKLOG, 128) //设置线程队列连接个数
>```
>
>option 方法传入tcp 参数, 放入linkendHashMao中保存
>
>```
>private final Map<ChannelOption<?>, Object> options = new LinkedHashMap<ChannelOption<?>, Object>();
>options.put(option, value);
>```

###  serverBootstrap.bind(6789)

>- final ChannelFuture regFuture = initAndRegister();
>
>​     创建socketChannel , 设置tcp参数 ,  为pipeline双向链表添加handler拦截器
>
>- ```
> doBind0(regFuture, channel, localAddress, promise);
> ```
> ```
>
> ```



### 请求过程  

每次请求创建一个socketChannel  并拥有一个channelPipeline双向链表   多个channelHandlerContext  

channelPipeline channelHandlerContext  channelHandler

