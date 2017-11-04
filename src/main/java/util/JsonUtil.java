package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class JsonUtil {
    public static JSONArray toD3LineChartJson(Map<String, Map<Integer, Integer>> map, int[] yearRange) {
        JSONArray seriesArray = new JSONArray();
        for (String series : map.keySet()) {
            Map<Integer, Integer> yearMap = map.get(series);
            JSONArray yearArray = new JSONArray();
            for (int year : yearRange) {
                JSONObject object = new JSONObject();
                object.put("year", year);
                int count = yearMap.containsKey(year) ? yearMap.get(year) : 0;
                object.put("count", count);
                yearArray.add(object);
            }
            JSONObject seriesObject = new JSONObject();
            seriesObject.put("series", series);
            seriesObject.put("data", yearArray);
            seriesArray.add(seriesObject);
        }
        return seriesArray;
    }


}
