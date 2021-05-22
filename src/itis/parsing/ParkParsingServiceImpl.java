package itis.parsing;

import itis.parsing.annotations.FieldName;
import itis.parsing.annotations.MaxLength;
import itis.parsing.annotations.NotBlank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParkParsingServiceImpl implements ParkParsingService {

    //Парсит файл в обьект класса "Park", либо бросает исключение с информацией об ошибках обработки
    @Override
    public Park parseParkData(String parkDatafilePath) throws ParkParsingException {
        try {
            HashMap<String,String> stringStringHashMap = new HashMap<>();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(parkDatafilePath));
            bufferedReader.readLine();
            String line;
            while (!(line = bufferedReader.readLine()).trim().equals("***")) {
                String[] split = line.split(":");
                stringStringHashMap.put(remove(split[0]), remove(split[1]));
            }
            return parkMap(stringStringHashMap);
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {

            throw new ParkParsingException("Не удалось считать парк из файла ", new ArrayList<>());
        }
    }
    private Park parkMap(HashMap<String, String> map) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<Park> parkClass = Park.class;
        Constructor<Park> constructor = parkClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        Park instance = constructor.newInstance();
        List<ParkParsingException.ParkValidationError> errorList = new ArrayList<>();
        List<String> message = new ArrayList<>();
        for (Field field : parkClass.getDeclaredFields()) {
            field.setAccessible(true);
            Annotation[] annotations = field.getAnnotations();
            String name = field.getName();
            boolean hasError = false;
            for (Annotation a : annotations) {
                if (a.annotationType() == FieldName.class) {
                    name = ((FieldName) a).value();
                }
                if (a.annotationType() == NotBlank.class && (!map.containsKey(name) || remove(map.get(name)).isEmpty())) {
                    String errorMessage = "Поле " + field.getName() + " пустое";
                    message.add(errorMessage);
                    errorList.add(new ParkParsingException.ParkValidationError(field.getName(), errorMessage));
                    hasError = true;
                }
                if (a.annotationType() == MaxLength.class && remove(map.get(name)).length() > ((MaxLength) a).value()) {
                    String errorMessage = "Размер больше установленного " + ((MaxLength) a).value();
                    message.add(errorMessage);
                    errorList.add(new ParkParsingException.ParkValidationError(field.getName(), errorMessage));
                    hasError = true;
                }
            }
            if (!hasError) {
                field.set(instance, returnObject(field.getType(), remove(map.get(name))));
            }
        }
        if (errorList.isEmpty()) {
            return instance;
        } else {
            throw new ParkParsingException(message.size() + " ошибки", errorList);
        }
    }
    private String remove(String string) {
        return string.trim().replaceAll("\"", "");
    }
    private Object returnObject(Class parentClass, String data) {
        if(data.equals("null")){
            return null;
        }else if (parentClass.isAssignableFrom(Integer.class)) {
            return Integer.parseInt(data);
        } else if (parentClass.isAssignableFrom(LocalDate.class)) {
            return LocalDate.parse(data);
        }
        return data;
    }


}
