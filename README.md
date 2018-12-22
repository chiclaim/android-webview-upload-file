## Android WebView文件上传

### 经过测试的机型

| 厂商        | 机型    |  系统版本  |
| --------   | -----:   | :----: |
| 小米        | 红米4     |   Android6.0.1    |
| 华为        | 荣耀7I     |   Android6.0.1    |
| 华为        | 荣耀V8      |   Android8.0     |
| 华为        | Mate20      |   Android9.0  |


本工程为大家演示在H5中如何上传文件，同时也提供了服务端代码，有兴趣的读者可以部署起来，测试文件上传功能。

如果只对客户端部分感兴趣，只需要关注WebView文件选择部分即可

[服务端代码下载地址](https://github.com/chiclaim/android_mvvm_server)， 客户端可以用如下url来上传文件
http://`your ip address`:`port`/AndroidMvvmServer/upload，如 http://192.168.1.200:8080/AndroidMvvmServer/upload

在华为荣耀某台机器上(Android5.0)上，文件选择效果如下所示：

![Android5.1](https://github.com/chiclaim/android-webview-upload-file/blob/master/assets/www/emulator5.0.gif)

在红米手机上(Android6.0)和华为荣耀7I(Android6.0)，WebView文件选择效果如下所示：

![红米手机上 Android6.0](https://github.com/chiclaim/android-webview-upload-file/blob/master/assets/www/xiaomi-vs-huawei.gif)

从中可以发现，不同的手机定制厂商对WebView文件选择框不管是在样式上还是按钮个数上都不尽相同。

如果需要统一WebView文件选择样式，需要自定义WebView文件选择框，如下图所示：

![自定义选择器](https://github.com/chiclaim/android-webview-upload-file/blob/master/assets/www/custom_chooser.gif)



## 3. Bug
In the android4.4 version , the web page canot get the right file name if you choose file from `Photo` not `file manager` . However it works in lolipop.

![android4.4](https://github.com/chiclaim/android-webview-upload-file/blob/master/assets/www/android4.4.gif)



## 4. Referenced document
1. [chromium-webview-samples](https://github.com/GoogleChrome/chromium-webview-samples)
2. [file-upload-in-webview](http://stackoverflow.com/questions/5907369/file-upload-in-webview)



