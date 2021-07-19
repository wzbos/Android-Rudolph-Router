# Method

将 @Route 注解到方法上，即可直接调用此方法

`注意：`
`1.方法必须为静态方法`
`2.context是获取execute(context)传的上下文，如果调用的时候没context值则接收的值为ApplicationContext`
`3.加上@Extra(RAW_URI) String rawUri 可以获取调用的URL字符串`

如：

```java
public class TestMethod {
    @Route("/method/test")
    public static void Test(@Extra Context context, @Extra(RAW_URI) String rawUri, @Extra int userId, @Extra String userName) {
        Toast.makeText(context, "Hello Method!\nuserId:" + userId + ",userName:" + userName, Toast.LENGTH_SHORT).show();
    }
}
```

## 调用方式：

* 1.引用方式调用

不支持

* 2.URL方式调用

```java
Rudolph.builder("/method/test?userId=11&userName=John").build().execute(context);
```
