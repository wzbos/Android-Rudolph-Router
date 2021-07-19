package cn.wzbos.android.rudolph

object Constant {
    // Generate
    private const val PROJECT = "Rudolph"
    const val WARNING_TIPS ="Generated code from Rudolph. Do not modify!\n"
    const val PACKAGE = "cn.wzbos.android.rudolph"
    const val PACKAGE_NAME = "$PACKAGE.routes"
    const val ROUTER_BINDER = "$PACKAGE.IRouteBinder"
    const val ROUTE_TABLE = "$PACKAGE.IRouteTable"
    const val ROUTE_SERVICE = "$PACKAGE.IRouteService"
    const val ACTIVITY_INTENT_BUILDER = "$PACKAGE.router.ActivityRouter.Builder"
    const val FRAGMENT_INTENT_BUILDER = "$PACKAGE.router.FragmentRouter.Builder"
    private const val PROVIDER_ROUTER = "$PACKAGE.router.ServiceRouter"
    const val PROVIDER_ROUTER_BUILDER = "$PROVIDER_ROUTER.Builder"
    const val ANNOTATION_TYPE_ROUTE = "$PACKAGE.annotations.Route"
    const val ANNOTATION_TYPE_COMPONENT = "$PACKAGE.annotations.Component"
    const val TYPE_RAW_URI = "$PACKAGE.Consts.RAW_URI"

    // Log
    const val PREFIX_OF_LOGGER = "[$PROJECT] "
}