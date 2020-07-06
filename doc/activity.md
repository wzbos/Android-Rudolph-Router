# Activity

定义一个Activity路由，如果不需要用url方式调用可以不写路由地址

```java
@Route("/activity/test")
public class TestActivity extends AppCompatActivity {
    @Extra("userId")
    int userId;
    
    @Extra("userName")
    String userName;
    
     @Override
     protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        //参数注入
        Rudolph.bind(this);
     }
            
}
```


## 调用方式

* 1.引用方式调用

```java
UserActivityRouter.builder().userId(11).userName("John").build().start(context);
```

* 2.URL方式调用

```java
Rudolph.builder("/user?userId=11&userName=John").build().open(context);
```


## 添加动画

```java
UserActivityRouter.builder().userId(11).userName("John").transition(R.anim.in_left,R.anim.out_right).build().start(context);
```

or

```java
Rudolph.builder("/user?userId=11&userName=John").transition(R.anim.in_left,R.anim.out_right).build().open(context);
```

## RequestCode

```java
UserActivityRouter.builder().userId(11).userName("John").transition(R.anim.in_left,R.anim.out_right).build().start(context);
```

or

```java
private static final int requestCode = 10001;
Rudolph.builder("/user?userId=11&userName=John").build().open(context,requestCode);
```

## 传递扩展参数

```java
UserActivityRouter.builder().userId(11).userName("John").putExtra("extra1","hello").build().start(context);
```

```java
Rudolph.builder("/user?userId=11&userName=John").putExtra("extra1","hello").build().open(context);
```


