# 1、核心技术

- 前端技术栈 ES6、vue、vuex、vue-router、vue-cli、axios、element-ui
- 后端技术栈 SpringBoot、MyBatis、Spring Security、Jwt

# 2、环境部署

## 1、准备工作

```text
JDK >= 1.8 (推荐1.8版本)
Mysql >= 5.7.0 (推荐5.7版本)
Redis >= 3.0
Maven >= 3.0
Node >= 10
```

## 2、必要配置

- 修改数据库连接，编辑`resources`目录下的`application-druid.yml`

```yml
# 数据源配置
spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        driverClassName: com.mysql.cj.jdbc.Driver
        druid:
            # 主库数据源
            master:
                url: 数据库地址
                username: 数据库账号
                password: 数据库密码
```

- redis的连接，默认安装是没有密码的，如果有设置密码，编辑`resources`目录下的`application.yml`

```yaml
# Spring配置
spring:
  # redis 配置
  redis:
    # 地址 127.0.0.1
    host: 地址
    # 端口，默认为6379
    port: 6379
    # 密码
    password: 密码
    # 连接超时时间
    timeout: 10s
    lettuce:
      pool:
        # 连接池中的最小空闲连接
        min-idle: 0
        # 连接池中的最大空闲连接
        max-idle: 8
        # 连接池的最大数据库连接数
        max-active: 8
        # #连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1ms
```



## 3、后端运行

- Eclipse

  > 1、导入到`Eclipse`，菜单 `File` -> `Import`，然后选择 `Maven` -> `Existing Maven Projects`，点击 `Next`> 按钮，选择工作目录，然后点击 `Finish` 按钮，即可成功导入。
  > `Eclipse`会自动加载`Maven`依赖包，初次加载会比较慢（根据自身网络情况而定）
  >
  > 2、创建数据库`ims`并导入数据脚本`ry`
  >
  > 3、打开项目运行`com.internship.internshipApplication.java`，出现如下图表示启动成功。
  >
  > ![image-20221108221411973](部署文档Images/image-20221108221411973.png)

- IDEA

  > 1、导入到`IDEA`，菜单 `File` -> `OPen`，选择工作目录，然后点击 `Finish` 按钮，即可成功导入。
  >
  > 2、创建数据库`ims`并导入数据脚本`ry`
  >
  > 3、打开项目运行`com.internship.internshipApplication.java`，出现如下图表示启动成功。
  >
  > ![image-20221108221408352](部署文档Images/image-20221108221408352.png)

## 4、前端运行

```bash
# 进入项目目录
cd internship-ui

# 安装依赖
npm install

# 强烈建议不要用直接使用 cnpm 安装，会有各种诡异的 bug，可以通过重新指定 registry 来解决 npm 安装速度慢的问题。
npm install --registry=https://registry.npm.taobao.org

# 本地开发 启动项目
npm run dev
```

打开浏览器，输入：([http://localhost:80 (opens new window)](http://localhost/)) 默认账户/密码 `admin/admin123`）
若能正确展示登录页面，并能成功登录，菜单及页面展示正常，则表明环境搭建成功

