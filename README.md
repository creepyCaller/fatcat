# 橘猫Web容器
2019/10/23 立项，完成HTTP协议最基本响应  
2019/10/24 完成按请求地址读取对应网页，完成指定发送数据流的类型功能，现在可以正确加载html、css、js文件  
2019/10/24 重构，完成处理请求参数功能，完成错误页功能，完成转发功能  
2019/10/25 修改了使用缓冲流来I/O的错误路线，改用I/O流来完成文件的读取，修正了显示图片功能  
2019/10/27 重构，现在先生成响应头再读取响应体,完善了注释，现在代码可读性更佳  
2019/10/28 添加Log4j2依赖作为日志记录器，添加通过外部yml文件配置服务器的方法，添加打包项目为jar包并运行在非IDE环境的方法  
2019/10/28 决定取消支持jsp，转而强制要求使用异步加载页面内容代替模板  
2019/10/28 添加"mock"及实例方案，现在网页中的Vue实例可以通过访问其模拟Ajax请求获取数据以渲染页面  
2019/10/31 添加通过外部传参来设置监听端口的方法，使用"-port {port}"配置  
2019/11/01 变更部署运行结构，现在fatcat.jar需要放在bin文件夹下  
