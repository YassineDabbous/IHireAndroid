package net.ekhdemni.utils;

import android.content.Context;


import net.ekhdemni.model.oldNet.Action;
import net.ekhdemni.model.oldNet.Ekhdemni;

import tn.core.util.Completion;

/**
 * Created by X on 7/7/2018.
 */

public class BroadUtils {

    public static void follow(Context context, boolean follow, String key, String value, final Completion completion){
        Action action = new Action(context) {
            public void doFunction(String s) {
                if(parse(s)){
                    completion.finish(s);
                }
            }
        };
        if(follow){
            action.url = Ekhdemni.broadcasts;
        }else{
            action.url = Ekhdemni.broadcasts+"/delete";
        }
        action.method = action.POST;
        action.params.put("key", key);
        action.params.put("value", value);
        action.run();
    }
}
