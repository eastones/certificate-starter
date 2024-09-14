# 许可证颁发组件
certificate-starter
foundation relies on services that provide checks for software usage cycles

#### 介绍
Lincense许可证颁发项目,包括服务端和客户端(Jar依赖).基于spring-boot-starter-parent.2.1.8.RELEAS.

#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明，更新证书到期时间

1.  在主目录=C:\workspace\public\certificate-issuance中，使用IDEA打开项目
2.  Model_1=主目录.cloud-license-serve // 用于生成证书
3.  Model_2=C:\workspace\public\certificate-starter //（spring-certificate-starter） 自动运行客户端，检查&校验证书有效性
4.  把Model_1启动，再到C:\license\Apache-2.0.lic,reName为Apache-2.0.lic.bak
5.  用IDEA打开主目录项目，找到http-requests-log.http文件（由HTTP-client工具生成）访问Model_1以生成证书，访问前修改参数"expiryTime"的日期
6.  注意，http-requests-log.http请求参数的说明和作用，不要配置错误，主目录中有参数和说明《SpringBoot License方案.md》
7.  把Model_1生成的最新证书（C:\license\Apache-2.0.lic）复制到Model_2/resources/carts/之下
8.	推送spring-certificate-starter的成品jar包到Maven中央仓库 https://s01.oss.sonatype.org
9.	结束。
10. 后记：本项目中涉及公钥和私钥，都已在本项目及相关系统中默认配置完成，按以上步骤生成证书。