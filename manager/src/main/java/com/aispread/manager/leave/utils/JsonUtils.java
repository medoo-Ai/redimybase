package com.aispread.manager.leave.utils;



import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.util.Iterator;

/**
 * @auther SyntacticSugar
 * @data 2019/1/27 0027下午 5:27
 */
public class JsonUtils {
    public static JSONObject mergeJson(Object object1, Object object2){
        if (object1 == null &&object2 == null){
            return null;
        }
        try {
            if (object1 == null){
                return new JSONObject(object2.toString());
            }
            if (object2 == null){
                return new JSONObject(object1.toString());
            }
            JSONObject jsonObject1 = new JSONObject(object1.toString());
            JSONObject jsonObject2= new JSONObject(object2.toString());
            Iterator iterator = jsonObject2.keys();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                Object value2 = jsonObject2.get(key);
                if (jsonObject1.has(key)){
                    Object value1 = jsonObject1.get(key);
                    if (!(value1 instanceof JSONObject)){
                        jsonObject1.put(key,value2);
                    }else {
                        jsonObject1.put(key,mergeJson(value1,value2));
                    }
                }else {
                    jsonObject1.put(key,value2);
                }
            }
            return jsonObject1;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
