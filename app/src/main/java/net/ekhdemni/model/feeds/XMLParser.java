package net.ekhdemni.model.feeds;

/**
 * Created by X on 1/21/2018.
 */

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Observable;
import java.util.TimeZone;

import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Resource;


public class XMLParser extends Observable {

    private ArrayList<Article> articles;
    private Article currentArticle;
    private Resource resource;
    String baseUri;

    public XMLParser() {
        articles = new ArrayList<>();
        currentArticle = new Article();
    }

    public void parseXML( Context context, Resource resource) throws XmlPullParserException, IOException {

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

        factory.setNamespaceAware(false);
        XmlPullParser xmlPullParser = factory.newPullParser();

        xmlPullParser.setInput(new StringReader(resource.data));
        boolean insideItem = false;
        int eventType = xmlPullParser.getEventType();

        baseUri = resource.getBaseUri();
        long last = 0;

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_TAG) {

                if (xmlPullParser.getName().equalsIgnoreCase("lastBuildDate")) {
                    if (!insideItem) {
                        String last_update = xmlPullParser.nextText();
                        //replace some text inside date
                        last_update = last_update.replace(" +0000", "");

                        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

                        try {
                            Date date = formatter.parse(last_update);
                            last = date.getTime();
                            MyActivity.log( "last " + last + " > "+ resource.last_update+ "resource.last_update");
                            if(last > resource.last_update){
                                resource.last_update = last;
                                resource.update(context);
                            }
                            else{
                                MyActivity.log("No news for: "+resource.url+"\n\n");
                                break;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }


                if (xmlPullParser.getName().equalsIgnoreCase("item")) {

                    insideItem = true;

                } else if (xmlPullParser.getName().equalsIgnoreCase("title")) {

                    if (insideItem) {
                        String title = xmlPullParser.nextText();
                        currentArticle.setTitle(title);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("link")) {

                    if (insideItem) {
                        String link = xmlPullParser.nextText();
                        currentArticle.setUrl(link);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("dc:creator")) {

                    if (insideItem) {
                        String author = xmlPullParser.nextText();
                        currentArticle.setAuthor(author);
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("category")) {

                    if (insideItem) {
                        String category = xmlPullParser.nextText();
                        currentArticle.addCategory(category);
                    }

                }
                else if (xmlPullParser.getName().equalsIgnoreCase("content:encoded")) {

                    if (insideItem) {
                        String htmlData = xmlPullParser.nextText();
                        Document doc = Jsoup.parse(htmlData);try{
                            doc.select("script").remove();
                            doc.select("*").removeAttr("style");
                        }catch (Exception e){
                            MyActivity.log( "can't remove script and style dom for article content "+e.toString());
                        }
                        //doc = absoluteLinks(doc);
                        try {
                            //choose the first image found in the article
                            String pic = doc.select("img").first().attr("src");
                            currentArticle.setImg(pic);
                        } catch (NullPointerException e) {
                            currentArticle.setImg("");
                        }
                        currentArticle.setContent(doc.html());
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("description")) {

                    if (insideItem) {
                        String description = xmlPullParser.nextText();
                        currentArticle.setDescription(description);
                        Document doc = Jsoup.parse(description);
                        //doc = absoluteLinks(doc);
                        try {
                            //choose the first image found in the article
                            String pic = doc.select("img").first().attr("src");
                            currentArticle.setImg(pic);
                        } catch (NullPointerException e) {
                        }
                        if(currentArticle.getContent() == null || currentArticle.getContent()==""){try{
                            doc.select("script").remove();
                            doc.select("*").removeAttr("style");
                        }catch (Exception e){
                            MyActivity.log( "can't remove script and style dom for article content "+e.toString());
                        }
                            currentArticle.setContent(doc.html());
                        }
                    }

                } else if (xmlPullParser.getName().equalsIgnoreCase("pubDate")) {

                     String pubDate = xmlPullParser.nextText();
                    //replace some text inside date
                    String formatedDate = pubDate.replace(" +0000", "").replace("GMT", "").trim();

                    Locale loc;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        loc = Resources.getSystem().getConfiguration().getLocales().get(0);
                    } else {
                        loc = Resources.getSystem().getConfiguration().locale;
                    }

                    //change date format
                    SimpleDateFormat curFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss", Locale.US);
                    Date dateObj;
                    try {
                        try{
                            dateObj = curFormater.parse(formatedDate);

                        }catch (Exception e){
                            MyActivity.log( e.getMessage());
                            dateObj = new Date(last);
                        }


                        SimpleDateFormat postFormater = new SimpleDateFormat("EEE, dd.MM.yyyy - HH:mm", loc);

                        //get the timezone settings from the phone
                        String timezoneID = TimeZone.getDefault().getID();

                        //set it dynamically
                        postFormater.setTimeZone(TimeZone.getTimeZone(timezoneID));
                        String newDateStr = postFormater.format(dateObj);

                        //currentArticle.setPublished(newDateStr);
                        currentArticle.setPublished(dateObj.getTime()+"");

                    } catch (Exception e) {
                        MyActivity.log( e.getMessage());
                    }

                }

            } else if (eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equalsIgnoreCase("item")) {
                insideItem = false;

                //currentArticle.setContent(HtmlUtils.improveHtmlContent(currentArticle.getContent(), resource.getBaseUri()));
                if(currentArticle.getPublished() == null || currentArticle.getPublished() == ""){
                    long l = (new Date()).getTime();
                    currentArticle.setPublished(l+"");
                }
                currentArticle.resource = resource.dbId;
                articles.add(currentArticle);
                currentArticle = new Article();
            }
            eventType = xmlPullParser.next();
        }
        triggerObserver();
    }

    private Document absoluteLinks(Document document) {
        overrideUrl(document.select("a[href]"), baseUri);
        overrideUrl(document.select("iframe[src]"), baseUri);
        overrideUrl(document.select("frame[src]"), baseUri);
        overrideUrl(document.select("[src]"), baseUri);
        overrideUrl(document.select("link[href]"), baseUri);
        return document;
    }


    public Elements overrideUrl(Elements links, String baseUri){
        for (Element link : links)  {
            String url = link.attr("href").toLowerCase();
            if (!url.startsWith("#"))    {
                //5aliha hakaak
            }
            else if (url.startsWith("file://"))    {
                url = url.replace("file://","https://");
                link.attr("href", url);
            }
            else if (url.startsWith("/"))    {
                link.attr("href", baseUri+link.attr("href"));
            }
            else if (!url.startsWith("http://") && !url.startsWith("https://"))    {
                link.attr("href", "http://"+link.attr("href"));
            }
            Log.w("☻☻☻ UserLink ☻☻☻: ", url);
        }
        return links;
    }

    private void triggerObserver() {
        setChanged();
        notifyObservers(articles);
    }
}