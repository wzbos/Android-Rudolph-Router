package cn.wzbos.android.rudolph;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Rudolph BuildInfo
 * Created by wuzongbo on 2018/12/20.
 */
class RudolphBuildInfo {
    private static final String OPTION_EXPORT_API_NAME = "export_api_name";
    private static final String OPTION_EXPORT_API_PACKAGE = "export_api_package";

    String compileSdkVersion;
    String minSdkVersion;
    String targetSdkVersion;
    String versionName;
    String versionCode;

    String projectPath = "";
    String moduleName;
    String modulePath;
    String export_api_name = null;
    String export_api_package = null;

    private static RudolphBuildInfo instance;

    static RudolphBuildInfo load(ProcessingEnvironment evn) {

        if (instance == null) {
            instance = new RudolphBuildInfo(evn);
        }
        return instance;
    }

    boolean exportApi() {
        return StringUtils.isNotEmpty(export_api_name) && StringUtils.isNotEmpty(export_api_package);
    }

    private RudolphBuildInfo(ProcessingEnvironment evn) {
        Map<String, String> options = evn.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            export_api_name = options.get(OPTION_EXPORT_API_NAME);
            export_api_package = options.get(OPTION_EXPORT_API_PACKAGE);
        }

        if (getModulePath(evn.getFiler())) {
            getGradleInfo();
        }
    }

    private boolean getModulePath(Filer filer) {
        try {
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", "tmp");
            String url = resource.toUri().getPath();
            int n = url.indexOf("/build/");
            if (n > -1) {

                int j = url.lastIndexOf("/", n - 1);
                if (j > -1) {
                    modulePath = url.substring(0, n);
                    moduleName = url.substring(j + 1, n);
                    projectPath = url.substring(0, j);
                }
            }
            resource.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void getGradleInfo() {
        File gradle = new File(modulePath + "/build.gradle");

        if (gradle.exists()) {
            FileReader fileReader = null;
            BufferedReader bufferedReader;
            try {
                fileReader = new FileReader(gradle);
                bufferedReader = new BufferedReader(fileReader);
                String str;
                boolean findDefaultConfig = false;
                while ((str = bufferedReader.readLine()) != null) {
                    if (str.contains("compileSdkVersion")) {
                        compileSdkVersion = getGradleValue(str, "compileSdkVersion");
                    } else if (str.contains("defaultConfig")) {
                        findDefaultConfig = true;
                    } else if (findDefaultConfig && str.contains("minSdkVersion")) {
                        minSdkVersion = getGradleValue(str, "minSdkVersion");
                    } else if (findDefaultConfig && str.contains("targetSdkVersion")) {
                        targetSdkVersion = getGradleValue(str, "targetSdkVersion");
                    } else if (findDefaultConfig && str.contains("versionName")) {
                        versionName = getGradleValue(str, "versionName");
                    } else if (findDefaultConfig && str.contains("versionCode")) {
                        versionCode = getGradleValue(str, "versionCode");
                    }
                }

                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private String getGradleValue(String str, String key) {
        String val = null;
        int n;
        if ((n = str.indexOf(key)) > -1) {
            val = str.substring(n + key.length() + 1).trim();
        }
        return val;
    }


}
