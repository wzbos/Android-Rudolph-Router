# Annotations

- [@Route](#route)
- [@Arg](#arg)
- [@Component](#component)
- [@Export](#export)

#### @Route

此注解为标识一个路由；

- 参数：
	- value：路由地址，可为空，例如@Route("/room")
	- export：是否导出API，一般组件化刚才才会用

```java
@Route(value = "/user",export = true)
public class UserActivity extends AppCompatActivity {
}
```

#### @Arg

此注解为标识路由的参数（注意如果注解到字段上，此字段不能为private）。

- 参数：
	- value：路由地址，可为空(默认取字段名)，例如@Arg("userId"),@Arg(RAW_URI)
	- base64：标识此参数是否为base64方式编码
	- json：标识此参数是否为json格式

```java
@Route(value = "/user",export = true)
public class UserActivity extends AppCompatActivity {
	@Arg("userId")
	int userId;
	@Arg
	String userName;
	@Arg(value="userInfo",base64=true, json=true)
	String userInfo;
}
```

#### @Component

此注解为组件化所需要的注解，主要为提供组件初始化操作；

- 参数：无

```java
@Component
public class TestComponent implements IRouteTable {

    @Override
    public void init(Application application) {
        Toast.makeText(application.getApplicationContext(), "组件xxx初始化啦！", Toast.LENGTH_SHORT).show();
    }
}
```
#### @Export

此注解标记当前路由调用类是否需要生成到协议层

- 参数：无

```java
@Route
public class TestService implements IRouteService{

    @Export
    public void showMessage(String message) {
       
    }
}
```