package helpers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by ecobos on 5/30/17.
 */
public class ReflectionUtils {
    public Boolean isFieldType(Class classToCheck, String fieldName, Class type) throws ClassNotFoundException, NoSuchFieldException {
        Class<?> lastClass = getaClass(classToCheck, fieldName);
        return lastClass.isAssignableFrom(type);
    }

    public Class<?> getaClass(Class classToCheck, String fieldName) throws NoSuchFieldException, ClassNotFoundException {
        String[] stringFields = fieldName.split("\\.");
        Class<?> lastClass = classToCheck;
        for(Integer i = 0; i< stringFields.length; i++){
            Field field;
            try{
                field = lastClass.getDeclaredField(stringFields[i]);
            }catch (NoSuchFieldException e) {
                try{
                    field = lastClass.getField(stringFields[i]);
                }catch (NoSuchFieldException e1) {
                     field = lastClass.getSuperclass().getDeclaredField(stringFields[i]);
                }
            }
            Type genType = field.getGenericType();
            if(genType.getTypeName().contains("List")){
                ParameterizedType genericType = (ParameterizedType) genType;
                Type[] actualTypeArguments = genericType.getActualTypeArguments();
                lastClass = Class.forName(actualTypeArguments[0].getTypeName());
            }else{
                lastClass = (Class <?>) genType;
            }
        }
        return lastClass;
    }
}
