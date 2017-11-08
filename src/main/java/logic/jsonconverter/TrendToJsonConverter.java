package logic.jsonconverter;

import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Converts the data of {@code TrendCommand} into JSON format.
 */
public class TrendToJsonConverter {

    /**
     * Converts {@code map} into JSON.
     */
    public <T> JSONArray mapsToJson(Map<T, Map<Integer, Integer>> map) {
        JSONArray resultArray = new JSONArray();
        for (Map.Entry<T, Map<Integer, Integer>> entry : map.entrySet()) {
            resultArray.add(getResultElement(entry));
        }
        return resultArray;
    }

    private <T> JSONObject getResultElement(Map.Entry<T, Map<Integer, Integer>> entry) {
        JSONArray yearCountPairArray = new JSONArray();
        JSONObject resultElement = new JSONObject();
        resultElement.put("category", entry.getKey());
        for (Map.Entry<Integer, Integer> innerEntry : entry.getValue().entrySet()) {
            yearCountPairArray.add(getYearCountPair(innerEntry));
        }
        resultElement.put("year-count-pair", yearCountPairArray);
        return resultElement;
    }

    private JSONObject getYearCountPair(Map.Entry<Integer, Integer> entry) {
        JSONObject yearCount = new JSONObject();
        yearCount.put("year", entry.getKey());
        yearCount.put("count", entry.getValue());
        return yearCount;
    }
}
