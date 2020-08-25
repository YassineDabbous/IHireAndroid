package net.ekhdemni.model.oldNet;

import java.util.Random;

/**
 * Created by X on 5/15/2018.
 */

public class Ekhdemni {

    public static String TAG = "qarya.app";
    public static int ADS_AFTER = 10;
    public static final int ADS_AFTER_DISABLED = 1000;
    public static String facebook = "https://www.facebook.com/ekhdemni";
    public static String android_help = "https://web.qarya.app/android-help";

    public static String web = "https://web.qarya.app/";
    public static String privacy = web+"privacy-policy";
    public static String contact = web+"contact-us";

    public static String server = "https://ihire.qarya.app/v1/";
    public static String configs = server+"configs";
    public static String apps = server+"apps";
    public static String services = server+"services";
    public static String resources = server+"resources";
    public static String countries = server+"countries";
    public static String categories = server+"categories";
    public static String forums_types = server+"forums_types";
    public static String forums = server+"forums";
    public static String posts = server+"posts";
    public static String comments = server+"comments";
    public static String linksComments = server+"links/comments";
    public static String jobs = server+"jobs";
    public static String users = server+"users";
    public static String data = server+"data";
    public static String conversations = server+"conversations";
    public static String notifications = server+"notifications";
    public static String relations = server+"relations";
    public static String requests = server+"requests";
    public static String reports = server+"reports";
    public static String alerts = server+"alerts";
    public static String works = server+"works";
    public static String photos = server+"photos";
    public static String liker = server+"liker";

    public static String degrees = server+"degrees";
    public static String worktypes = server+"worktypes";
    public static String languages = server+"languages";
    public static String languagesLevels = server+"languagesLevels";

    public static String broadcasts = server+"broadcasts";
    public static String login = server+"login";
    public static String register = server+"register";
    public static String reset_password = server+"recover";



    public static String searchUsers = server+"search/users/advanced";


    public static String works_delete = works+"/delete";
    public static String comments_delete = comments+"/delete";
    public static String comments_solution = comments+"/solution";


    public static String experiences = server+"users/experiences";
    public static String requestNewResume = server+"users/resume";

    public static String placeholder = "http://cdn.qarya.app/images/placeholder/"; //"http://lorempixel.com/400/200/business/";
    public static String randomPlaceHolder(){
        return placeholder+randInt(1,8)+".png";
    }
    public static String randomNoImagePlaceHolder(){
        String[] INDICATORS = new String[]{
                "http://vollrath.com/ClientCss/images/VollrathImages/No_Image_Available.jpg",
                "https://newprojects.99acres.com/projects/ahimsa_builder//images/45sxlar2.gif",
                "https://bookneighbour.com/static/images/no_image_available.jpg",
                "https://www.mancinifoods.com/site/wp-content/uploads/2018/05/no-thumbnail.png" };
        return INDICATORS[rand.nextInt(INDICATORS.length)];
    }
    public static Random rand = new Random();
    public static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
