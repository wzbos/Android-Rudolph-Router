# @Arg
 
@Arg 注解所支持的数据类型以及使用方式


如：

```java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Rudolph.bind(this);
    }
}
```

* 获取当前Activity、fragment、Service、Method的URL原始调用地址

```java
    @Arg(RAW_URI)
    String routeUri;
```

* Serialized

```java
    @Arg(value = "serialized")
    Broker serializedParam;
```

* Json

```java
    //json
    @Arg(value = "json", json = true)
    User<Broker> jsonParam;
```

* Json & base64


```java
    //encode：json->base64，decode：base64->json
    @Arg(value = "base64json", json = true, base64 = true)
    User<Broker> base64jsonParam;
```


* String

```java
    @Arg("stringArg")
    String stringArg;
```

* base64String

```java
    @Arg(value = "string64", base64 = true)
    String string64Arg;
```

* String[]

```java
    @Arg("stringArray")
    String[] stringArrayArg;

```

* boolean

```java
    @Arg("boolArg")
    boolean boolArg;
```

* boolean[]

```java
    @Arg("booleanArray")
    boolean[] booleanArrayArg;
```

* byte

```java
    @Arg("byteArg")
    byte byteArg;
```

* byte[]

```java
    @Arg("byteArray")
    byte[] byteArrayArg;
```

* short

```java
    @Arg("shortArg")
    short shortArg;
```

* short[]

```java
    @Arg("shortArray")
    short[] shortArrayArg;
```

* int

```java
    @Arg("intArg")
    int intArg;
```

* int[]

```java
    @Arg("intArrayArg")
    int[] intArrayArg;
```

* long

```java
    @Arg("longArg")
    long longArg;
```

* long[]

```java
    @Arg("longArray")
    long[] longArrayArg;
```

* char

```java
    @Arg("charArg")
    char charArg;
```

* char[]

```java
    @Arg("charArray")
    char[] charArrayArg;
```

* float

```java
    @Arg("floatArg")
    float floatArg;
```

* float[]

```java
    @Arg("floatArray")
    float[] floatArrayArg;
```

* double

```java
    @Arg("doubleArg")
    double doubleArg;
```

* double[]

```java
    @Arg("doubleArray")
    double[] doubleArrayArg;
```

* Character

```java
    @Arg("characterArg")
    Character characterArg;
```

* ArrayList<String> 

```java
    //ArrayList
    @Arg
    ArrayList<String> stringArrayListArg;
```

* ArrayList<Integer> 

```java
    @Arg
    ArrayList<Integer> integerArrayListArg;
```

* ArrayList<CharSequence> 

```java
    @Arg(value = "charSequenceArrayList")
    ArrayList<CharSequence> charSequenceArrayListArg;
```

* ArrayList<? extend Parcelable> 

```java
    @Arg(value = "parcelableArrayList")
    ArrayList<Broker> parcelableArrayListArg;
```

