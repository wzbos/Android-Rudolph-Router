# Rudolph

Rudolph Android Router Framework（鲁道夫安卓路由框架组件）

[![License](https://img.shields.io/badge/License%20-Apache%202-337ab7.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Maven-Central-Release](https://img.shields.io/maven-central/v/cn.wzbos.android/Android--Rudolph--Router)](https://s01.oss.sonatype.org/content/repositories/releases/cn/wzbos/android/rudolph/)
[![Maven-Central-Snapshot](https://img.shields.io/nexus/s/cn.wzbos.android/Android--Rudolph--Router?server=https%3A%2F%2Fs01.oss.sonatype.org)](https://s01.oss.sonatype.org/content/repositories/snapshots/cn/wzbos/android/rudolph/)

[版本更新记录](https://github.com/wzbos/Android-Rudolph-Router/releases/)

### 框架特性

- 支持组件API模块自动生成
- 自动生成路由Builder类与服务类的接口层；
- 加载更快，更稳定，无需插桩与dex扫描；
- 无需指定模块名，接入更简单；
- 显式跳转与URL路由地址跳转融为一体，更方便快捷；
- 通过Builder方式传参，无需手动写参数名，从而减少参数传错和修改带来的Bug隐患；
- 支持所有Intent的参数类型；
- 支持Activity 、Fragment、Service、Method四种路由类型
- 支持Instant Run
- 支持AndroidX
- 支持Kotlin

### 支持路由类型

- [Activity](./doc/activity.md)
- [Fragment](./doc/fragment.md)
- [Service](./doc/service.md)
- [Method](./doc/method.md)
- [组件化设计](doc/design.md)

### 1、组件引入

1.添加依赖(1.x.x为[版本号](https://github.com/wzbos/Android-Rudolph-Router/releases))

Java:

``` groovy
repositories {
   jcenter()
}

dependencies {
  implementation "cn.wzbos.android:rudolph:1.x.x"
  annotationProcessor "cn.wzbos.android:rudolph-compiler:1.x.x"
}
```

Kotlin:

``` groovy
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-kapt'

repositories {
   jcenter()
}

dependencies {
  implementation "cn.wzbos.android:rudolph:1.x.x"
  kapt "cn.wzbos.android:rudolph-compiler:1.x.x"
}
```

### 2、代码混淆

所需混淆配置都已经打包到组件内，无需新增额外混淆配置

### 3、初始化

建议放到application中

```java
Rudolph.init(this);
```

### 4、拦截器

* 添加拦截器

```java
Rudolph.addInterceptor(new Interceptor() {
    @Override
    public boolean intercept(Context context, Router routeInfo) {
        if ("/test4".equalsIgnoreCase(routeInfo.getRawUrl())) {
            Toast.makeText(MyApplication.this, "intercept,path:" + routeInfo.getRawUrl(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
});
```

* 移除拦截器

```java
Rudolph.removeInterceptor(interceptor);
```

### 5、设置scheme

设置当前APP的scheme用于支持其他APP程序跳转到当前APP

```java
Rudolph.setScheme("myApp")
```

> scheme值需要于AndroidManifest.xml文件中的值相同，例如下面的scheme为"myApp"

```java
<activity
    android:name=".MainActivity"
    android:configChanges="keyboardHidden|orientation"
    android:screenOrientation="portrait">
    <intent-filter>
        <data android:scheme="myApp" />
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
    </intent-filter>

    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

### 6、bind

给@Extra注解的变量赋值，一般用在Activity的create 或者 Service的init方法内

```java
Rudolph.bind(this)
```

### 7、onNewIntent

用于在Activity的onNewIntent生命周期内重新赋值

```java
Rudolph.onNewIntent("myApp")
```

### 8、addRoute

添加一条路由记录，一般场景下不需要用，此方法主要是路由表类自行调用

```java
Rudolph.addRoute(new RouteInfo.Builder().routeType(RouteType.ACTIVITY)
    .target(KotlinActivity.class)
    .path("/kotlin/test")
    .tag("")
    .extra("userId",int.class)
    .extra("userName",String.class).build());
```