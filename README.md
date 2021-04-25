##资料

[spring文档](https://spring.io/guides)

[Spring Web文档](https://spring.io/guides/gs/serving-web-content/)

[es参考样式](https://elasticsearch.cn)

[Github deploy key](https://developer.githun.com/v3/guides/managing-deploy-keys/#deploy-keys/)

[Github OAuth](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)

[Bootstrap](https://v3.bootcss.com/getting-started/)

[Markdown 下载](https://pandao.github.io/editor.md)

[Markdown Github](https://github.com/pandao/editor.md)

##工具

[Git](https：//git-scm.com/download)

[VP](https://www.visual-paradigm.com)

[Mvn](https://mvnrepository.com/)

[Mybatis](http://mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)

[flyway](https://flywaydb.org/documentation/getstarted/firststeps/maven)

[Lombok](https://projectlombok.org/setup/maven)

##脚本

`
 create table USER
 (
     ID           INT auto_increment,
     ACCOUNT_ID   VARCHAR(100),
     NAME         VARCHAR(50),
     TOKEN        CHAR(36),
     GMT_CREATE   BIGINT,
     GMT_MODIFIED BIGINT,
     constraint TABLE_NAME_PK
         primary key (ID)
 );`
 
`mvn flyway:migrate`

[Mybatis Generator]

部署命令：`mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate`

[部署步骤]

- 更新主机镜像信息:yum update
- 安装git:yum install git
- 安装maven:yum install maven
- 创建文件夹: mkdir App
- 进入该文件夹: cd App
- 克隆项目: git clone https://github.com/smallyang96/forum.mawen.git
- 进入项目文件夹:cd forum.mawen
- 打包: mvn compile package
- 创建新的配置文件application-production.properties: cp /src/main/resources/application.properties /src/main/resources/application-production.properties
- 修改新的服务器配置文件: vim /src/main/resources/application-production.properties,替换localhost为服务器ip