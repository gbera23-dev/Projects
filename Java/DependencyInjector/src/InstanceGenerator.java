import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class InstanceGenerator {

    private static String ANNOTATION_NAME = "InjectEligible";


    public static Object getInstance(Class clazz)
            throws InvocationTargetException,
            InstantiationException,
            IllegalAccessException, NoSuchFieldException {

        //identify whether InjectEligible is present on top of this class
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        InjectEligible annotation = (InjectEligible) Arrays.stream(annotations).filter(an -> an.annotationType().getCanonicalName().equals(
                ANNOTATION_NAME
        )).findFirst().orElseThrow(() -> new RuntimeException("Cannot instantiate the object!"));

        //get type of class this annotation holds
        Class annotationClass = annotation.clazz();

        //now instantiate this class with its default constructor
        Object toInjectObject = annotationClass.getDeclaredConstructors()[0].newInstance();

        //now instantiate our class
        Object ourObject = clazz.getDeclaredConstructors()[0].newInstance();

        //now look through declared instance variables, find the one whose class type is of annotationClass
        //and set toInjectObject to it

        Field toInjectObjectField = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.getType().
                        getCanonicalName().equals(annotationClass.getCanonicalName()))
                .findFirst().orElseThrow(() -> new RuntimeException("Cannot find field to inject!"));

        //make it temporarily accessible
        toInjectObjectField.setAccessible(true);

        //now set
        toInjectObjectField.set(ourObject, toInjectObject);

        //now make it not accessible again
        toInjectObjectField.setAccessible(false);

        //return our object
        return ourObject;
    }

}
