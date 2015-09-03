package lazyloading.infinitegridofimages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mkankanala on 8/25/15.
 */

public class ParseJson {

    public static ArrayList<ImageData> parseJson(String jsonString) {
        ArrayList<ImageData> imageDataList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("photos");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject photoObj = jsonArray.getJSONObject(i);
                ImageData imageData = new ImageData();
                imageData.setImageId(photoObj.getString("id"));
                imageData.setImageUrl(photoObj.getString("image_url"));
                imageData.setName(photoObj.getString("name"));
                imageDataList.add(imageData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageDataList;
    }

}
