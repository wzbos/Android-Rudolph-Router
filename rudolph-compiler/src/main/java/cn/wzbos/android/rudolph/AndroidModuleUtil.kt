package cn.wzbos.android.rudolph

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Create api module
 */
internal object AndroidModuleUtil {
    @JvmStatic
    fun createApiModule(moduleInfo: RudolphBuildInfo) {
        createGradle(moduleInfo)
        createAndroidManifest(moduleInfo)
    }

    /**
     * Create AndroidManifest.xml
     */
    private fun createAndroidManifest(info: RudolphBuildInfo?) {
        if (info != null && info.exportApi()) {
            val manifestFile = File(info.projectPath + "/" + info.exportProtocolName + "/src/main/AndroidManifest.xml")
            if (!manifestFile.exists()) {
                if (!manifestFile.parentFile.exists()) {
                    manifestFile.parentFile.mkdirs()
                }
                var fileWriter: FileWriter? = null
                try {
                    if (manifestFile.createNewFile()) {
                        fileWriter = FileWriter(manifestFile)
                        fileWriter.write("<manifest package=\"" + info.exportProtocolPackage + "\" />")
                        fileWriter.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }

    /**
     * Create build.gradle
     */
    private fun createGradle(info: RudolphBuildInfo) {
        val gradleFile = File(info.projectPath + "/" + info.exportProtocolName + "/build.gradle")
        if (!gradleFile.exists()) {
            if (!gradleFile.parentFile.exists()) {
                gradleFile.parentFile.mkdirs()
            }
            var fileWriter: FileWriter? = null
            try {
                if (gradleFile.createNewFile()) {
                    fileWriter = FileWriter(gradleFile)
                    fileWriter.write("""/**
* ${info.exportProtocolName}
* Generated code from Rudolph.
* date : $currentDate
*/

apply plugin: 'com.android.library'

android {
    compileSdkVersion ${info.compileSdkVersion}

    defaultConfig {
        minSdkVersion ${info.minSdkVersion}
        targetSdkVersion ${info.targetSdkVersion}
        versionName ${info.versionName}
        versionCode ${info.versionCode}
    }
}

dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation "cn.wzbos.android:rudolph:${"$"}rudolph_version"
}""")
                    fileWriter.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private val currentDate: String
        get() {
            val date = Date(System.currentTimeMillis())
            return SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date)
        }
}