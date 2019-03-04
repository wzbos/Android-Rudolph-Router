package cn.wzbos.android.rudolph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Create api module
 */
class AndroidModuleUtil {

    static void createApiModule(RudolphBuildInfo moduleInfo) {
        createGradle(moduleInfo);
        createAndroidManifest(moduleInfo);
    }

    /**
     * Create AndroidManifest.xml
     */
    private static void createAndroidManifest(RudolphBuildInfo info) {
        if (info != null && info.exportApi()) {
            File manifestFile = new File(info.projectPath + "/" + info.export_api_name + "/src/main/AndroidManifest.xml");
            if (!manifestFile.exists()) {
                if (!manifestFile.getParentFile().exists()) {
                    manifestFile.getParentFile().mkdirs();
                }
                FileWriter fileWriter = null;
                try {
                    if (manifestFile.createNewFile()) {
                        fileWriter = new FileWriter(manifestFile);
                        fileWriter.write("<manifest package=\"" + info.export_api_package + "\" />");
                        fileWriter.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fileWriter != null) {
                        try {
                            fileWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Create build.gradle
     */
    private static void createGradle(RudolphBuildInfo info) {
        File gradle_file = new File(info.projectPath + "/" + info.export_api_name + "/build.gradle");
        if (!gradle_file.exists()) {
            if (!gradle_file.getParentFile().exists()) {
                gradle_file.getParentFile().mkdirs();
            }
            FileWriter fileWriter = null;
            try {
                if (gradle_file.createNewFile()) {
                    fileWriter = new FileWriter(gradle_file);
                    fileWriter.write("/**\n" +
                            "* " + info.export_api_name + "\n" +
                            "* Generated code from Rudolph.\n" +
                            "* date : " + getCurrentDate() + "\n" +
                            "*/\n\n" +
                            "apply plugin: 'com.android.library'\n\n" +
                            "android {\n" +
                            "    compileSdkVersion " + info.compileSdkVersion + "\n\n" +
                            "    defaultConfig {\n" +
                            "        minSdkVersion " + info.minSdkVersion + "\n" +
                            "        targetSdkVersion " + info.targetSdkVersion + "\n" +
                            "        versionName " + info.versionName + "\n" +
                            "        versionCode " + info.versionCode + "\n" +
                            "    }\n" +
                            "}\n\n" +
                            "dependencies {\n" +
                            "    implementation fileTree(dir: 'libs', include: ['*.jar'])\n" +
                            "    implementation \"cn.wzbos.android:rudolph:$rudolph_version\"\n" +
                            "}");
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private static String getCurrentDate() {
        Date date = new Date(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(date);
    }
}
