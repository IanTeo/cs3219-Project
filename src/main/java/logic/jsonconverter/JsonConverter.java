package logic.jsonconverter;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Converts the data of {@code TrendCommand} into JSON format.
 */
public class JsonConverter {
    
    public static JSONObject errorMessageToJson(String message) {
        JSONObject object = new JSONObject();
        object.put("error", message);
        
        return object;
    }
    /**
     * Converts {@code entryList} into a JSON.
     */
    public static JSONArray entryListToJson(List<Map.Entry<String, Integer>> entryList) {
        if (entryList.size() == 0) {
            return new JSONArray();
        }

        JSONArray array = new JSONArray();
        for (Map.Entry<String, Integer> entry : entryList) {
            String key = entry.getKey();
            int value = entry.getValue();

            JSONObject object = new JSONObject();
            object.put("series", key);
            object.put("count", value);
            array.add(object);
        }
        return array;
    }

    /**
     * Converts {@code entryList} into a specialized word cloud JSON.
     */
    public static JSONArray entryListToWordCloudJson(List<Map.Entry<String, Integer>> entryList) {
        if (entryList.size() == 0) {
            return new JSONArray();
        }

        int maxCount = entryList.stream()
                .mapToInt(Map.Entry::getValue)
                .max().getAsInt();

        JSONArray array = new JSONArray();
        for (Map.Entry<String, Integer> entry : entryList) {
            String key = entry.getKey();
            int count = entry.getValue();

            JSONObject object = new JSONObject();
            object.put("word", key);
            object.put("count", count);
            object.put("size", getPercentageInt(count, maxCount));
            array.add(object);
        }
        return array;
    }

    private static int getPercentageInt(int numerator, int denominator) {
        return (int) ((numerator * 100.0) / denominator);
    }


    /**
     * Converts {@code map} into JSON.
     */
    public static <T> JSONArray mapsToJson(Map<T, Map<Integer, Integer>> map) {
        JSONArray resultArray = new JSONArray();
        for (Map.Entry<T, Map<Integer, Integer>> entry : map.entrySet()) {
            resultArray.add(getResultElement(entry));
        }
        return resultArray;
    }

    private static <T> JSONObject getResultElement(Map.Entry<T, Map<Integer, Integer>> entry) {
        JSONArray yearCountPairArray = new JSONArray();
        JSONObject resultElement = new JSONObject();
        resultElement.put("series", entry.getKey());
        for (Map.Entry<Integer, Integer> innerEntry : entry.getValue().entrySet()) {
            yearCountPairArray.add(getYearCountPair(innerEntry));
        }
        resultElement.put("data", yearCountPairArray);
        return resultElement;
    }

    private static JSONObject getYearCountPair(Map.Entry<Integer, Integer> entry) {
        JSONObject yearCount = new JSONObject();
        yearCount.put("year", entry.getKey());
        yearCount.put("count", entry.getValue());
        return yearCount;
    }
}
