package com.test.json;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonDemo {
	@Test
	public void test() throws JSONException{
		
		String response_data = "{'body':{'apps':[{'name':'111'},{'name':'222'}]}}";
		JSONObject data_obj = new JSONObject(response_data);
		String apps_str =((JSONObject) data_obj.get("body")).get("apps").toString();
		JSONArray apps_array = new JSONArray(apps_str);
		String[] result = new String[apps_array.length()];
		for(int i=0;i<apps_array.length();i++){
			JSONObject app_obj = new JSONObject(apps_array.get(i).toString());
			String name = app_obj.get("name").toString();
			result[i] = name;
		}
		System.out.println(Arrays.toString(result));
	}
}
