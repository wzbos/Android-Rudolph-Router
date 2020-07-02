# Rudolph

Rudolph Android Router Framework（鲁道夫安卓路由框架组件）

[![Download](https://api.bintray.com/packages/wuzongbo/maven/rudolph/images/download.svg)](https://bintray.com/wuzongbo/maven/rudolph/_latestVersion)
[![Maven](https://img.shields.io/badge/Jcenter-Android--Rudolph--Router-green.svg?style=true)](http://jcenter.bintray.com/cn/wzbos/android)

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

### 组件引入

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

### 3.代码混淆

所需混淆配置都已经打包到组件内，无需新增额外混淆配置


