
This sample demonstrate android webview choose file to upload.
I just implement the client code ,the server code of receiver the file is not implemented. 

First of all, you should build the server code by your eclipse(server code wrote by java). 

Then update the ip of url(get ip mac command `ifconfig` windows command `ipconfig`). 
http://`192.168.1.109`:8080/AndroidMvvmServer/upload

Eventually, Run the android code on your device or emulator.


![Android5.1](http://img.blog.csdn.net/20160325161714006)


## 1，Function :
android webview choose file to upload

## 2，Android Version
This sample has test in Android3.0、Android4.0、Android 5.0、Android6.0

## 3，Bug
In the android4.4 version , the web page canot get the right file name if you choose file from `Photo` not `file manager` . However it works in lolipop.

![android4.4](http://img.blog.csdn.net/20160325160240125)



