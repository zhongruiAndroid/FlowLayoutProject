

![github](https://github.com/zhongruiAndroid/FlowLayoutProject/blob/master/screenshot/flowlayout.gif "github")  

## [Demo.apk下载](https://raw.githubusercontent.com/zhongruiAndroid/FlowLayoutProject/master/demo/demo.apk "apk文件")

| 属性    | 类型      | 说明                                                                         |
|---------|-----------|------------------------------------------------------------------------------|
| bothGap | dimension | 设置水平和垂直间距                                                           |
| vGap    | dimension | 设置垂直间距                                                                 |
| hGap    | dimension | 设置水平间距                                                                 |
| gravity |           | 子view水平排列方式<br/>left:水平居左<br/>right:水平居右<br/>center_horizontal:水平居中 |


```xml
<com.github.flowview.FlowLayout
        android:id="@+id/flView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:background="#D9DEDF"
        app:gravity="left"
        app:bothGap="10dp"
        app:vGap="10dp"
        app:hGap="10dp"
        >
         <!--yourview-->
</com.github.flowview.FlowLayout>
```

<br/>

### 如果本库对您有帮助,还希望支付宝扫一扫下面二维码,你我同时免费获取奖励金(非常感谢 Y(^-^)Y)
![github](https://github.com/zhongruiAndroid/SomeImage/blob/master/image/small_ali.jpg?raw=true "github")  


| 最新版本号 | [ ![Download](https://api.bintray.com/packages/zhongrui/mylibrary/FlowLayout/images/download.svg) ](https://bintray.com/zhongrui/mylibrary/FlowLayout/_latestVersion) |
|--------|----|
  



```gradle
implementation 'com.github:FlowLayout:版本号看上面'
```  
