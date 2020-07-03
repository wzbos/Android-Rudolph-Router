# @Extra
 
@Extra 注解所支持的数据类型以及使用方式


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
    @Extra(RAW_URI)
    String routeUri;
```

* Serialized

```java
    @Extra(value = "serialized")
    Broker serializedParam;
```

* Json

```java
    //json
    @Extra(value = "json", json = true)
    User<Broker> jsonParam;
```

* Json & base64


```java
    //encode：json->base64，decode：base64->json
    @Extra(value = "base64json", json = true, base64 = true)
    User<Broker> base64jsonParam;
```


* String

```java
    @Extra("stringArg")
    String stringArg;
```

* base64String

```java
    @Extra(value = "string64", base64 = true)
    String string64Arg;
```

* String[]

```java
    @Extra("stringArray")
    String[] stringArrayArg;

```

* boolean

```java
    @Extra("boolArg")
    boolean boolArg;
```

* boolean[]

```java
    @Extra("booleanArray")
    boolean[] booleanArrayArg;
```

* byte

```java
    @Extra("byteArg")
    byte byteArg;
```

* byte[]

```java
    @Extra("byteArray")
    byte[] byteArrayArg;
```

* short

```java
    @Extra("shortArg")
    short shortArg;
```

* short[]

```java
    @Extra("shortArray")
    short[] shortArrayArg;
```

* int

```java
    @Extra("intArg")
    int intArg;
```

* int[]

```java
    @Extra("intArrayArg")
    int[] intArrayArg;
```

* long

```java
    @Extra("longArg")
    long longArg;
```

* long[]

```java
    @Extra("longArray")
    long[] longArrayArg;
```

* char

```java
    @Extra("charArg")
    char charArg;
```

* char[]

```java
    @Extra("charArray")
    char[] charArrayArg;
```

* float

```java
    @Extra("floatArg")
    float floatArg;
```

* float[]

```java
    @Extra("floatArray")
    float[] floatArrayArg;
```

* double

```java
    @Extra("doubleArg")
    double doubleArg;
```

* double[]

```java
    @Extra("doubleArray")
    double[] doubleArrayArg;
```

* Character

```java
    @Extra("characterArg")
    Character characterArg;
```

* ArrayList<String> 

```java
    //ArrayList
    @Extra
    ArrayList<String> stringArrayListArg;
```

* ArrayList<Integer> 

```java
    @Extra
    ArrayList<Integer> integerArrayListArg;
```

* ArrayList<CharSequence> 

```java
    @Extra(value = "charSequenceArrayList")
    ArrayList<CharSequence> charSequenceArrayListArg;
```

* ArrayList<? extend Parcelable> 

```java
    @Extra(value = "parcelableArrayList")
    ArrayList<Broker> parcelableArrayListArg;
```

