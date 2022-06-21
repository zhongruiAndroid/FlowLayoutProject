
<img src="https://github.com/zhongruiAndroid/FlowLayoutProject/blob/master/screenshot/flowlayout.gif" alt="image"  width="auto" height="500">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="https://github.com/zhongruiAndroid/FlowLayoutProject/blob/master/screenshot/flowlayout2.gif" alt="image"  width="auto" height="500">  

## [aar下载](https://raw.githubusercontent.com/zhongruiAndroid/FlowLayoutProject/master/demo/FlowLayout.aar "aar文件")

## [Demo.apk下载](https://raw.githubusercontent.com/zhongruiAndroid/FlowLayoutProject/master/demo/demo.apk "apk文件")

| 属性    | 类型      | 说明                                                                         |
|---------|-----------|------------------------------------------------------------------------------|
| bothGap | dimension | 设置水平和垂直间距                                                           |
| vGap    | dimension | 设置垂直间距                                                                 |
| hGap    | dimension | 设置水平间距                                                                 |
| gravity |           | 子view水平排列方式<br/>left:水平居左<br/>right:水平居右<br/>center:水平居中 |
| gravity_vertical |           | 每行子view垂直排列方式<br/>top:垂直居上<br/>bottom:垂直居右<br/>center:垂直居下 |

| layout_params属性    | 类型      | 说明                                                                         |
|---------|-----------|------------------------------------------------------------------------------|
| layout_new_line | boolean | 是否换行           |                            
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
         <TextView
	         android:layout_width="wrap_content"
	         android:layout_height="24dp"
	         android:text="Android"
	         android:gravity="center"
		 <!--立即换行-->
	         app:layout_new_line="true"
         />
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
