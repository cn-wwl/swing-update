# swing-update
一款简单的 java swing 客户端更新工具

### 架构

- 服务端
  - Eureka
  - spring clould config
  - spring clould gateway
  - spring boot
- 客户端
  - swing
  - spring boot
- 更新程序
  - swing 
  - spring boot

**流程：**

spring clould config 存放 每个版本的客户端程序 及其最新的版本信息，客户端启动时会去通过spring clould config的版本与本地版本比较，如果与服务端版本不一致，则提示更新，启动对应的更新程序，与此同时，关闭当前客户端。应用程序启动后 再次比较版本信息，确认需要更新，则下载对应的文件资源，解压后，替换原有的客户端，并修改本地版本信息，然后给出启动客户端提示，确认后启动客户端。



**注：**

需要注意的是，我们写的swing 程序 生成后仅仅是个jar包，需要打包成exe，这里用到的是`exe4j`,打包配置在publish1中，也可以自定义打包配置，当然了打包的时候需要把`application.properties`放在与exe的同级目录中，方便配置相应的信息。



