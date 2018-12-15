package cn.wzbos.rudolph.utils;

public class Consts {
    // Generate
    private static final String PROJECT = "Rudolph";
    public static final String WARNING_TIPS = "Generated code from rudolph. Do not modify!\n";

    public static final String PACKAGE = "cn.wzbos.rudolph";
    public static final String PACKAGE_NAME = PACKAGE + ".routes";
    public static final String ROUTER_BINDER = PACKAGE + ".IRouteBinder";
    public static final String ROUTE_TABLE = PACKAGE + ".IRouteTable";
    public static final String ROUTE_SERVICE = PACKAGE + ".IRouteService";

    public static final String ACTIVITY_INTENT_BUILDER = PACKAGE + ".router.ActivityRouter.Builder";
    public static final String FRAGMENT_INTENT_BUILDER = PACKAGE + ".router.FragmentRouter.Builder";

    public static final String PROVIDER_ROUTER = PACKAGE + ".router.ServiceRouter";
    public static final String PROVIDER_ROUTER_BUILDER = PROVIDER_ROUTER + ".Builder";

    public static final String ANNOTATION_TYPE_ROUTE = PACKAGE + ".annotations.Route";
    public static final String ANNOTATION_TYPE_COMPONENT = PACKAGE + ".annotations.Component";
    public static final String TYPE_RAW_URI = PACKAGE + ".Consts.RAW_URI";

    // Log
    static final String PREFIX_OF_LOGGER = "[" + PROJECT + "] ";
}

