# meituan-android
安卓仿美团-课程设计 (包含 商家列表、店铺详情、购物车结算页面) 只包含交互逻辑无业务逻辑

## 目录结构
 - app 为 Android 项目, 使用`AndroidStudio` 打开即可
 - server 为服务端数据, 部署后方可使用

## 使用方法
- clone 项目
```shell
git clone https://github.com/ufec/meituan-android.git
```
- 部署后端
先需要安装node环境，[点击下载node](https://nodejs.org)
```shell
cd meituan-android\server\
npm install

node app.js
```
看到 `server run on 34565 port http://127.0.0.1:34565` 便代表部署成功
- 修改app配置

打开安卓项目, 修改 `app\meituan\app\src\main\java\cn\ufec\meituan\config\MeiTuanConfig.java`文件，将 `BASE_URL` 值改为 `http://10.0.2.2:34565`

运行即可，如需部署公网访问，将后端服务部署后依旧修改上述 `BASE_URL` 即可

## 声明
数据爬取自美团外卖，如侵权告知删除，仅供学习使用