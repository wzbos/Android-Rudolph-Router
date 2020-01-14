package cn.wzbos.android.rudolph;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypesException;
import javax.lang.model.type.TypeMirror;

import cn.wzbos.android.rudolph.annotations.Route;

/**
 * https://stackoverflow.com/questions/7687829/java-6-annotation-processing-getting-a-class-from-an-annotation
 */
public class APUtils {
    @FunctionalInterface
    public interface GetClassValue {
        void execute() throws MirroredTypesException;
    }

    public static TypeMirror getTypeMirrorFromAnnotationValue(GetClassValue c) {

        try {
            c.execute();
        } catch (MirroredTypesException ex) {
           if( ex.getTypeMirrors().size() > 0)
            return ex.getTypeMirrors().get(0);
        }
        return null;
    }


    private static AnnotationMirror getAnnotationMirror(TypeElement typeElement, Class<?> clazz) {
        String clazzName = clazz.getName();
        for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }


    public static TypeMirror getClazz(TypeElement foo) {
        AnnotationMirror am = getAnnotationMirror(foo, Route.class);
        if (am == null) {
            return null;
        }
        AnnotationValue av = getAnnotationValue(am, "clazz");
        if (av == null) {
            return null;
        } else {
            return (TypeMirror) av.getValue();
        }
    }
}
