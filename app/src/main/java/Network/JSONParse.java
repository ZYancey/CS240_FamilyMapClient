package Network;

import com.google.gson.Gson;

public class JSONParse {
    public static Object deserialize(String jsonStr, Class<?> cls) {
        Gson gson = new Gson();
        return gson.fromJson(jsonStr, cls);
    }

    public static String serialize(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}