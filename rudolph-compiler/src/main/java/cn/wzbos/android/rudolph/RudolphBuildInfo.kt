package cn.wzbos.android.rudolph

import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang3.StringUtils
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.StandardLocation

/**
 * Rudolph BuildInfo
 * Created by wuzongbo on 2018/12/20.
 */
internal class RudolphBuildInfo private constructor(evn: ProcessingEnvironment) {
    var compileSdk: String? = null
    var minSdkVersion: String? = null
    var targetSdkVersion: String? = null
    var versionName: String? = null
    var versionCode: String? = null

    @JvmField
    var projectPath = ""

    @JvmField
    var moduleName: String? = null
    var modulePath: String? = null

    @JvmField
    var exportProtocolName: String? = null

    @JvmField
    var exportProtocolPackage: String? = null
    fun exportApi(): Boolean {
        return StringUtils.isNotEmpty(exportProtocolName) && StringUtils.isNotEmpty(
            exportProtocolPackage
        )
    }

    private fun getModulePath(filer: Filer): Boolean {
        try {
            val resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "tmp")
            val url = resource.toUri().path
            val n = url.indexOf("/build/")
            if (n > -1) {
                val j = url.lastIndexOf("/", n - 1)
                if (j > -1) {
                    modulePath = url.substring(0, n)
                    moduleName = url.substring(j + 1, n)
                    projectPath = url.substring(0, j)
                }
            }
            resource.delete()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    companion object {
        private const val OPTION_EXPORT_API_NAME = "export_api_name"
        private const val OPTION_EXPORT_API_PACKAGE = "export_api_package"
        private var instance: RudolphBuildInfo? = null

        @JvmStatic
        fun load(evn: ProcessingEnvironment): RudolphBuildInfo? {
            if (instance == null) {
                instance = RudolphBuildInfo(evn)
            }
            return instance
        }
    }

    init {
        val options = evn.options
        if (MapUtils.isNotEmpty(options)) {
            exportProtocolName = options[OPTION_EXPORT_API_NAME]
            exportProtocolPackage = options[OPTION_EXPORT_API_PACKAGE]
        }
        getModulePath(evn.filer)
    }
}