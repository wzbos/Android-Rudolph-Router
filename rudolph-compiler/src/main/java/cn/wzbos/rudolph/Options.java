package cn.wzbos.rudolph;

import org.apache.commons.collections4.MapUtils;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;

class Options {

    private static final String OPTION_EXPORT_API_NAME = "export_api_name";
    private static final String OPTION_EXPORT_API_PACKAGE = "export_api_package";

    String export_api_name = null;
    String export_api_package = null;

    Options(ProcessingEnvironment processingEnvironment) {
        Map<String, String> options = processingEnvironment.getOptions();
        if (MapUtils.isNotEmpty(options)) {
            export_api_name = options.get(OPTION_EXPORT_API_NAME);
            export_api_package = options.get(OPTION_EXPORT_API_PACKAGE);
        }
    }
}
