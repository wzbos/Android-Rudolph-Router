# Service

创建一个服务(适用于跨module调用)

``注意：服务类必须实现IRouteService接口，并且加上 clazz=xxx 标记对外提供的接口``

提示：

在@Route注解内加上singleton = true属性可以标记为当前服务是单例

#### 定义接口

```java
public interface ITestService extends IRouteService {
    void showMessage(Context context, String msg);
}
```

### 定义服务
```java
@Route(value = "/service/test", clazz = ITestService.class, singleton = true)
public class TestService implements ITestService{

    @Extra
    int userId;

    @Extra
    String userName;
    
    @Override
    public void init(Bundle bundle) {
        //参数注入
        Rudolph.bind(TestService.this, bundle);
    }

    @Export
    public void showMessage(Context context, String msg) {
        Toast.makeText(context, msg + "\nuserId:" + userId + ",userName:" + userName, Toast.LENGTH_SHORT).show();
    }
}
```


## 调用方式

* 1.引用方式调用

```java
ITestService service = TestServiceRouter.builder().userId(1).userName("Tom").execute();
service.showMessage(MainActivity.this, "Hello Provider!");
```

* 2.URL方式调用

```java
ITestService service = (ITestService)Rudolph.builder("/service/test?userId=11&userName=John").execute();
service.showMessage(MainActivity.this, "Hello Provider!");
```
