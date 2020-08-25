package tn.core.model.net.custom;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

import tn.core.model.responses.BaseResponse;


public class MyResponseDeserializer implements JsonDeserializer<BaseResponse> {
    @Override
    public BaseResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {


        if (((JsonObject) json).get("data") instanceof JsonObject){
            return new Gson().fromJson(json, BaseResponse.class);
        } else {
            return new Gson().fromJson(json, BaseResponse.class);
        }

    }

}
