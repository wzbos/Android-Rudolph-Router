package cn.wzbos.android.rudolph;

public class Constant {
    // Generate
    static final String PROJECT = "Rudolph";
    static final String WARNING_TIPS = "Generated code from Rudolph. Do not modify!\n";

    static final String PACKAGE = "cn.wzbos.android.rudolph";
    static final String PACKAGE_NAME = PACKAGE + ".routes";
    static final String ROUTER_BINDER = PACKAGE + ".IRouteBinder";
    static final String ROUTE_TABLE = PACKAGE + ".IRouteTable";
    static final String ROUTE_SERVICE = PACKAGE + ".IRouteService";

    static final String ACTIVITY_INTENT_BUILDER = PACKAGE + ".router.ActivityRouter.Builder";
    static final String FRAGMENT_INTENT_BUILDER = PACKAGE + ".router.FragmentRouter.Builder";

   private static final String PROVIDER_ROUTER = PACKAGE + ".router.ServiceRouter";
    static final String PROVIDER_ROUTER_BUILDER = PROVIDER_ROUTER + ".Builder";

    static final String ANNOTATION_TYPE_ROUTE = PACKAGE + ".annotations.Route";
    static final String ANNOTATION_TYPE_COMPONENT = PACKAGE + ".annotations.Component";
    static final String TYPE_RAW_URI = PACKAGE + ".Consts.RAW_URI";

    // Log
    static final String PREFIX_OF_LOGGER = "[" + PROJECT + "] ";
}

