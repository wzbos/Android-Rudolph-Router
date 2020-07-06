package cn.wzbos.android.rudolph

import org.apache.commons.collections4.MapUtils
import org.apache.commons.lang3.StringUtils
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.tools.StandardLocation

/**
 * Rudolph BuildInfo
 * Created by wuzongbo on 2018/12/20.
 */
internal class RudolphBuildInfo private constructor(evn: ProcessingEnvironment) {
    var compileSdkVersion: String? = null
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
        return StringUtils.isNotEmpty(exportProtocolName) && StringUtils.isNotEmpty(exportProtocolPackage)
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

    private val gradleInfo: Unit
        get() {
            val gradle = File("$modulePath/build.gradle")
            if (gradle.exists()) {
                var fileReader: FileReader? = null
                val bufferedReader: BufferedReader
                try {
                    fileReader = FileReader(gradle)
                    bufferedReader = BufferedReader(fileReader)
                    var findDefaultConfig = false

                    bufferedReader.lines().forEach {
                        if (it.contains("compileSdkVersion")) {
                            compileSdkVersion = getGradleValue(it, "compileSdkVersion")
                        } else if (it.contains("defaultConfig")) {
                            findDefaultConfig = true
                        } else if (findDefaultConfig && it.contains("minSdkVersion")) {
                            minSdkVersion = getGradleValue(it, "minSdkVersion")
                        } else if (findDefaultConfig && it.contains("targetSdkVersion")) {
                            targetSdkVersion = getGradleValue(it, "targetSdkVersion")
                        } else if (findDefaultConfig && it.contains("versionName")) {
                            versionName = getGradleValue(it, "versionName")
                        } else if (findDefaultConfig && it.contains("versionCode")) {
                            versionCode = getGradleValue(it, "versionCode")
                        }
                    }
                    fileReader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (fileReader != null) {
                        try {
                            fileReader.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }

    private fun getGradleValue(str: String, key: String): String? {
        var gradleVersion: String? = null
        var n: Int
        if (str.indexOf(key).also { n = it } > -1) {
            gradleVersion = str.substring(n + key.length + 1).trim { it <= ' ' }
        }
        return gradleVersion
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
        if (getModulePath(evn.filer)) {
            gradleInfo
        }
    }
}