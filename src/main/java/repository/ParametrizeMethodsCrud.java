package repository;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import exception.NotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static util.Constants.ERROR_IO;
import static util.Constants.NOT_NULL;

public interface ParametrizeMethodsCrud {
    static <T> void save(T t, List<T> list, File file, Gson gson, Type type) {
        try (FileOutputStream output = new FileOutputStream(file)) {
            StringBuilder builder = new StringBuilder();
            if (list.isEmpty()) {
                list.add(t);
                builder.append(gson.toJson(list, type));
                output.write(builder.toString().getBytes());
            } else {
                list.add(t);
                builder.append(gson.toJson(list, type));
                String[] strings = builder.toString().split("},");
                for (String dev : strings) {
                    if (!dev.endsWith("}]")) {
                        String save = dev + "},\n";
                        output.write(save.getBytes());
                    } else {
                        output.write(dev.getBytes());
                    }
                }
            }
        } catch (IOException | JsonParseException e) {
            System.out.println(ERROR_IO + e);
        }
    }

    static <T> List<T> findAll(File file, Gson gson, Type type) {
        List<T> list = new ArrayList<>();
        try (FileReader reader = new FileReader(file)) {
            list = gson.fromJson(reader, type);
        } catch (IOException | JsonParseException e) {
            System.out.println(ERROR_IO + e);
        }
        if (list != null) {
            return list;
        }
        return new ArrayList<>();
    }

    static <T> T findById(Predicate<T> predicate, List<T> list, String exception) {
        return list.stream().filter(predicate).findFirst()
                .orElseThrow(() -> new NotFoundException(exception));
    }

    static <T> void update(List<T> list, Predicate<T> predicate, Consumer<T> consumer) {
        list.stream().filter(predicate).forEach(consumer);
    }

    static <T> void deleteById(List<T> list, Predicate<T> predicate, Consumer<T> consumer) {
        list.stream().filter(predicate).forEach(consumer);
    }

    static <T> void deleteAll(List<T> list, Consumer<T> consumer) {
        list.forEach(consumer);
    }

    static void cleanFile(File file) {
        requireNonNull(file);
        try (FileOutputStream output = new FileOutputStream(file.getPath())) {
            output.write("".getBytes());
        } catch (IOException e) {
            System.out.println(ERROR_IO + e);
        } catch (NullPointerException e) {
            System.out.println(NOT_NULL + e);
        }
    }
}
