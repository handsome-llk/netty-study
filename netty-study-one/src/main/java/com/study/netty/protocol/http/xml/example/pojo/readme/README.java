package com.study.netty.protocol.http.xml.example.pojo.readme;

public class README {

	/**
	 * ant其实就是构建项目用的，类似于maven。所以不需要拘泥于ant
	 * 直接看项目就好了
	 * 
	 * 
	 * 记录下又参考价值的链接：
	 * https://blog.csdn.net/Givemefive555/article/details/84712425
	 * https://dpb-bobokaoya-sm.blog.csdn.net/article/details/89290994
	 * 
	 * cmd下主要命令：
	 * 
	 * java -cp c:\tools\工具\jibx_1_3_1\jibx\lib\jibx-tools.jar org.jibx.binding.generator.BindGen -t C:\tools\workspace\workspace-csdn\NettyLearn\src\main\resources\jibx -v  com.dpb.netty.xml.pojo.Customer com.dpb.netty.xml.pojo.Shipping com.dpb.netty.xml.pojo.Address com.dpb.netty.xml.pojo.Order com.dpb.netty.xml.pojo.OrderFactory
	 * 
	 * java -cp c:\tools\工具\jibx_1_3_1\jibx\lib\jibx-tools.jar org.jibx.binding.generator.BindGen 
	 * 	-t C:\tools\workspace\workspace-csdn\NettyLearn\src\main\resources\jibx 
	 * 	-v  com.dpb.netty.xml.pojo.Customer 
	 * 		com.dpb.netty.xml.pojo.Shipping 
	 * 		com.dpb.netty.xml.pojo.Address 
	 * 		com.dpb.netty.xml.pojo.Order 
	 * 		com.dpb.netty.xml.pojo.OrderFactory
	 * 
	 * 说明：
	 * java -cp ..libx-tools.jar ..BindGen 
	 * 	-t 生成文件保存地址 
	 * 	-v 需要绑定文件的class文件 完整包名.类名
	 * 
	 * 
	 * 
	 * java -cp D:\software\jar\jibx_1_3_1\jibx\lib\jibx-tools.jar org.jibx.binding.generator.BindGen -t D:\myProjects\netty-study\netty-study\netty-study-one\src\main\java\com\study\netty\protocol\http\xml\example\pojo\bind -v com.study.netty.protocol.http.xml.example.pojo.Customer com.study.netty.protocol.http.xml.example.pojo.Shipping com.study.netty.protocol.http.xml.example.pojo.Address com.study.netty.protocol.http.xml.example.pojo.Order
	 * 
	 * 
	 * 这里捣鼓了好久。。。哎。。。得看仔细点。命令就是上面的命令，咱们用cmd来运行。
	 * 1、 -v 后面的类名用英文空格隔开
	 * 2、 cmd要再classes文件下打开，是classes文件夹下，不是pojo的class文件下
	 * 
	 * 是这个样子的文件夹哦	D:\myProjects\netty-study\netty-study\netty-study-one\target\classes
	 * 以上我主要是classes文件夹的位置理解错了，导致一直找不到对应的pojo编译文件
	 * 
	 * 
	 * 以上是生成绑定文件binding.xml和pojo.xsd。接下来还有一步是生成强化的class文件。
	 * 这一步使用maven动态构建。maven代码编写在pom.xml的 <!-- 生成jibx class信息 --> 标签下。
	 * 
	 * 我还是复制过来吧
	 * <build>
	    <plugins>
			<plugin>
				<groupId>org.jibx</groupId>
				<artifactId>jibx-maven-plugin</artifactId>
				<version>1.3.1</version>
				<configuration>
					<schemaBindingDirectory>${basedir}/src/main/java/com/study/netty/protocol/http/xml/example/pojo/bind</schemaBindingDirectory>
					<includeSchemaBindings>
						<includeSchemaBindings>*binding.xml</includeSchemaBindings>
					</includeSchemaBindings>
					<verbose>true</verbose>
				</configuration>
				<executions>
					<execution>
					<id>jibx-bind</id>
					<phase>compile</phase><!--把jibx绑定到了comile编译阶段 -->
						<goals>
							<goal>bind</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	  </build>
	 * 
	 * 
	 * 然后maven install，可以pojo对应的classes文件夹下生成类似  JiBX_bindingAddress_access.class  这样的文件
	 * 
	 * 
	 */
	
}
