1、为什么引入MQ？
 	应用解耦（上游系统调用下游系统，下游系统有改动不能让上游系统有感知，新的模块进来代码改动最小）
 	异步模型（rpc调用为同步，长链路调用有性能风险）
 	流量削峰（上游系统步骤少处理快，下游系统步骤多处理慢，保证业务系统不被冲垮）

2、Queue和Topic的差异？
			“负载均衡”模式，即使没有消费者消息也不会丢弃，一个消息只被一个消费者消费
	工作模式	
			“发布订阅”模式，没有订阅者消息会被丢弃，一个消息被多个订阅者消费
	
			Queue数据默认会在服务器上以文件形式保存，如ActiveMQ在$AMQ_HOME\data\kahadb下，也可配置DB存储
	有无状态
			无状态
			
			消息不会被丢弃
	传递完整性	
			没有订阅者，消息被丢弃
	
			由于一个消息只被一个消费者消费，消费者增多，性能也不会明显降低，不通的消息协议之间性能有差异
	处理效率
			由于消息按照订阅者数量复制，消费者增多，性能明显降低，不通的消息协议之间性能有差异
	
3、什么是JMS？
	java消息服务，指的是两个应用系统之间异步通讯的API，应用系统之间不互相直接相连而是通过消息中间件进行通讯	
	
4、JMS四大组成元素？
	Provider（MQ服务器）、Producer（生产者）、Consumer（消费者）、Message（消息）

5、消息头组成元素？
	消息头、消息体、消息属性
	消息头属性：
			JMSDestination	目的地
			JMSDeliveryMode 是否持久化，队列默认是，主题消息模式（非持久发布订阅）默认不是，主题发布订阅模式（持久发布订阅）默认是
			JMSExpiration	过期时间，默认长期有效
			JMSPriority		优先级，默认4
			JMSMessageID	唯一标识

6、消息的可靠性
	事务、持久化、签收（MQ服务自带功能，但是MQ宕机则无法工作）
		
	事务和签收的关系：消费者未开启事务，签收模式为手动的话，消息应告知已签收；消费者开启事务，提交事务，消息全部自动签收
	
7、指定配置文件启动
	/home/activeMQ/apache-activemq-5.15.9/bin/activemq start xbean:file:/home/activeMQ/apache-activemq-5.15.9/conf/activemq.xml

8、ActiveMQ的持久化机制？
	持久化机制有JDBC、AMQ、KahaDB、LevelDB
	
	KahaDB的存储原理：使用事务日志和索引文件来存储所有的消息数据
		db-{num}.log 存储消息数据，默认大小32mb，超出部分另创建一个日志文件，日志文件不再需要后删除或归档
		db.data 该文件包含了BTree索引，索引指向db-{num}.log中的消息
		db.free 当前db.data文件里哪些页面是空闲的，文件具体内容是所有空闲页面的id
		db.redo 用来消息恢复，如果KahaDB消息存储时被强制退出时产生，MQ重启后用于恢复BTree索引
		lock 表示当前获得KahaDB读写权限的Broker，读共享写独占

	LevelDB的存储原理：是基于文件的持久性数据库，它不使用自定义B-Tree实现来索引预写日志，而是使用基于LevelDB的索引
	
	JDBC配置：
		Mysql版本5.5，将mysql-connector-java-5.1.38.jar放入apache-activemq-5.15.9/lib目录下
		
		<persistenceAdapter> 
			<!-- createTablesOnStartup（ MQ启动时自动创建表，默认为true ） -->
			<jdbcPersistenceAdapter dataSource="#mysql-ds"/> 
		</persistenceAdapter>
		
		<bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" destroy-method="close"> 
			<property name="driverClassName" value="com.mysql.jdbc.Driver"/> 
			<property name="url" value="jdbc:mysql://127.0.0.1/activemq?relaxAutoCommit=true"/> 
			<property name="username" value="activemq"/> 
			<property name="password" value="activemq"/> 
			<property name="poolPreparedStatements" value="true"/> 
		</bean>
	
		Queue模型中：
			当DeliveryMode为NON_PERSISTENT时，消息存储在内存中
			当DeliveryMode为PERSISTENT时，消息存储在MQ文件或者是数据库中
			消息一旦被消费将从Broker中删除（内存/文件/数据库）

		Topic模型中：
			消息模式，消息存储在内存中
			发布订阅模式，消息存储在MQ文件或者是数据库中（acks为订阅者信息，msgs为消息信息）
			消息被消费不会删除订阅者信息和消息信息
			
9、journal是什么？
	MQ向Mysql持久化消息，这期间涉及到读写数据库，速度很慢，journal是一种高速缓存日志，处于MQ和Mysql之间
	MQ生产者生产消息到journal，消费者从journal取消息，生产者和消费都很快则几乎没有消息持久化到Mysql
	生产者太快或者消费者太慢，journal则可以以批量的方式持久化到Mysql
	
	<persistenceFactory>
		<journalPersistenceAdapterFactory 
			journalLogFiles="5" 
			journalLogFileSize="32768" 
			dataDirectory="activemq-data" 
			dataSource="#mysql-ds"
			useJournal="true"
			useQuickJournal="true"/>
	</persistenceFactory>

10、ActiveMQ高可用方式MQ集群？
	Zookeeper集群注册所有的ActiveMQ Broker，其中一个被推举为Master，其他被视为Salve
	如果Master宕机，Zookeeper将从Salve中推举出一个Master
	各个Salve连接Master，同步Master状态，只有Master接收客户端的连接
	1主2从模式，Master将会存储并更新然后等待（3/2+1-1=1）1个Slave存储和更新完成后才通知Success
	有一个节点需要作为观察节点，当Master宕机后，至少有一个法定节点在线以能够找到拥有最新状态的节点，才可推举出Master
	推荐至少运行3个节点，以防止一个节点宕机后服务中断

	1、Zookeeper集群（1主2从）
	2、ActiveMQ集群（1主2从）
		管理端端口不相同（jetty.xml）
		brokerName相同（activemq.xml）
		vim /etc/hosts
			192.168.188.138 www.activemq.com
		LevelDB持久化配置

11、异步投递消息？
	ActiveMQ支持同步、异步发送消息到Broker，默认使用异步，除非明确指定使用同步或未使用事务且要求持久化职则为同步
	优点：最大化生产者发送消息的效率
	缺点：消息可能积压，消耗消费者端内存，降低Broker性能；不能有效确保消息发送成功
	
12、如何确保异步投递消息成功发送至MQ？
	异步消息发送时需要回调
	
13、延时发送或定时发送？
	MQ服务器修改配置文件，添加schedulerSupport="true"
	<broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}" schedulerSupport="true">

14、哪些情况会导致消息重发？
	消费者端开启了事务但是未能提交事务
	消费者端签收类型为手动签收但是拒绝签收（session.recover()）
	
	间隔重发：1秒间隔，重发次数6次
	方式：RedeliveryPolicy可以通过ActiveMQConnectionFactory或ActiveMQConnection设置
		也可以为每一个Destination设置一个，RedeliveryPolicyMap保存信息

15、死信队列？
	使用MQ时设计两个队列，一个业务队列，一个死信队列（处理异常情况）
	
	共享队列（SharedDeadLetterStrategy）默认策略，所有失败的消息都加入到这里
	私有队列（IndividualDeadLetterStrategy），默认的无论是Topic还是Queue，Broker都将使用Queue保存DeadLetter
	<policyEntry queue="order" >
		<deadLetterStrategy>
			<individualDeadLetterStrategy queuePrefix="ActiveMQ.DLQ.Queue." useQueueForTopicMessages="false" />
		</deadLetterStrategy>
	</policyEntry>
	
	两种队列：
		自动删除过期消息
			<policyEntry queue=">" >
				<deadLetterStrategy>
					<sharedDeadLetterStrategy processExpired="false" />
				</deadLetterStrategy>
			</policyEntry>
		非持久消息入死信队列
			<policyEntry queue=">" >
				<deadLetterStrategy>
					<sharedDeadLetterStrategy processNonPersistent="true" />
				</deadLetterStrategy>
			</policyEntry>
	
16、消息重复消费？
	为消息设置一个全局id，消费过该消息，将<id, message>以K-V形式写入Redis中，消费者消费前，先去Redis中查询有没有消费记录即可
	