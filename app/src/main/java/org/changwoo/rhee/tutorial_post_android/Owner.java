package org.changwoo.rhee.tutorial_post_android;

import android.text.TextUtils;
import io.swagger.client.StringUtil;
import io.swagger.client.model.Post;

public class Owner {
    static boolean is(Integer userid, Integer targetUserid){
        return userid == targetUserid;
    }
    static boolean is(Integer userid, Post post){
        Integer postUserid = post.getUser().getId();
        return Owner.is(userid, postUserid);
    }
    static boolean is(String email, String targetUserEmail){
        return TextUtils.equals(email, targetUserEmail);
    }
    static boolean is(String email, Post post){
        String targetUserEmail = post.getUser().getEmail();
        return Owner.is(email, targetUserEmail);
    }
}
