package relcy.excersise;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mkankanala on 8/27/15.
 */
public class ParseJson {


    public static ArrayList<DataModel> getObjects(String jsonString) {
        if (jsonString != null) {

            ArrayList<DataModel> imageDataList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("photos");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject photoObj = jsonArray.getJSONObject(i);
                    DataModel imageData = new DataModel();
                    imageData.setId(photoObj.getString("id"));
                    imageData.setImageUrl(photoObj.getString("image_url"));
                    imageData.setName(photoObj.getString("name"));
                    imageDataList.add(imageData);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return imageDataList;
        } else
            return null;
    }

}

