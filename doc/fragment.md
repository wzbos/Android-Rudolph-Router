# Fragment

创建一个Fragment路由

```java
@Route("/fragment/test")
public class TestFragment extends Fragment {
    @Arg("userId")
    int userId;

    @Arg("userName")
    String userName;
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //参数注入
        Rudolph.bind(this);
    }
}
```

## 调用方式：

* 1.引用方式调用

```java
Fragment fragment = TestFragmentRouter.builder().userId(11).userName("John").build().open();
```

* 2.URL方式调用

```java
Rudolph.builder("/fragment/user?userId=11&userName=John").build().open();
```