package cn.wzbos.android.rudolph.utils

import cn.wzbos.android.rudolph.RouteInfo
import cn.wzbos.android.rudolph.Rudolph
import cn.wzbos.android.rudolph.annotations.Route
import java.net.URI


fun String.toURI(): URI {
    return if (this.startsWith("/")) {
        URI.create("//${this}")
    } else {
        URI.create(this)
    }
}

/**
 * 匹配URL
 */
fun Route.match(
    url: String?,
): Boolean {
    val supportUrls = if (value.isNotBlank()) {
        arrayOf(*urls).plus(value)
    } else {
        urls
    }
    return match(supportUrls, url)
}

/**
 * 比较路由地址
 */
fun RouteInfo.match(
    openUrl: String?,
): Boolean {
    val supportUrl = this.url?.toTypedArray() ?: return false
    return match(supportUrl, openUrl)
}

private fun match(
    supportUrls: Array<String>,
    openUrl: String?,
): Boolean {
    if (openUrl.isNullOrBlank())
        return false

    val uri = openUrl.toURI()
    val scheme = if (uri.scheme.isNullOrBlank()) Rudolph.scheme else uri.scheme
    val host = if (uri.host.isNullOrBlank()) Rudolph.scheme else uri.scheme
    val path = uri.path

    supportUrls.forEach {
        if (it.startsWith("^")) {
            if (Regex(it).matches(openUrl)) {
                return true
            }
        } else {
            val supportUri = it.toURI()
            if (!supportUri.scheme.isNullOrEmpty()
                && !supportUri.scheme.equals(scheme, true)
            ) {
                return@forEach
            }

            if (!supportUri.host.isNullOrEmpty()
                && !supportUri.host.equals(host, true)
            ) {
                return@forEach
            }

            if (!supportUri.path.isNullOrEmpty()
                && !supportUri.path.equals(path, true)
            ) {
                return@forEach
            }
            return true
        }
    }
    return false
}

