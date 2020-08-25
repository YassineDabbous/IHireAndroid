package net.ekhdemni.model.feeds.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ekhdemni.presentation.ui.activities.auth.YDUserManager;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.model.models.Article;
import net.ekhdemni.model.models.Category;
import net.ekhdemni.model.models.Resource;
import net.ekhdemni.model.oldNet.Ekhdemni;

public class MyDataBase {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ekhdemni";
    private static final String TABLE_RESOURCES = "resources";
    private static final String TABLE_POSTS = "posts";
    private static final String TABLE_TAG = "tags";
    private static final String TABLE_POSTS_TAG = "posts_tags";
    private static final String LIMIT = "10";

    // Common column names
    public static final String KEY_ROWID = BaseColumns._ID;
    public static final String KEY_TITLE = "title";
    private static final String KEY_CREATED_AT = "created_at";
    public static final String KEY_MARKED = "marked";

    // NOTES Table - column nmaes
    private static final String KEY_ID       = "id";
    private static final String KEY_RESOURCE = "resource";
    public static final String KEY_READED_AT = "readed_at"; //////////// needed in activities list (last readed posts)
    public static final String KEY_PUBLISHED = "published";
    public static final String KEY_AUTHOR    = "author";
    public static final String KEY_URL       = "url";
    public static final String KEY_CONTENT   = "content";
    public static final String KEY_IMG       = "img";
    public static final String KEY_READ      = "read";
    public static final String KEY_SEEN      = "seen";

    // Resources Table
    public static final String KEY_POSITION = "position";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_LAST_UPDATE = "last_update";
    public static final String KEY_LOGO = "logo";
    public static final String KEY_VERIFIED = "verified";
    public static final String KEY_ENABLE = "enable";
    public static final String KEY_ENABLE_NOTIFICATION = "enable_notification";

    // TAGS Table - column names
   // public static final String KEY_POST_NUMBER = "postsnumber";
    public static final String KEY_IS_CITY = "is_city";

    // NOTE_TAGS Table - column names
    private static final String KEY_POST_ID = "post_id";
    private static final String KEY_TAG_TITLE = "tag_title";

    String[] postColumns = new String[]{KEY_ROWID, KEY_RESOURCE, KEY_AUTHOR, KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_PUBLISHED, KEY_READED_AT, KEY_URL, KEY_IMG, KEY_MARKED, KEY_READ, KEY_SEEN};
    String[] resourcesColumns = new String[]{KEY_ROWID, KEY_POSITION, KEY_COUNTRY, KEY_TITLE, KEY_LAST_UPDATE, KEY_URL, KEY_LOGO, KEY_MARKED, KEY_ENABLE_NOTIFICATION, KEY_ENABLE, KEY_VERIFIED};

    // Table Create Statements
    // Article table create statement
    private static final String CREATE_TABLE_RESOURCES = "CREATE TABLE " + TABLE_RESOURCES + "(" +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_POSITION + " integer DEFAULT 0, " +
            KEY_COUNTRY + " text not null, " +
            KEY_TITLE + " text not null, " +
            KEY_LAST_UPDATE + " integer null default 0, " +
            KEY_URL + " text not null, " +
            KEY_LOGO + " text null, " +
            KEY_MARKED + " boolean DEFAULT 0, " +
            KEY_ENABLE_NOTIFICATION + " boolean DEFAULT 1, " +
            KEY_ENABLE + " boolean DEFAULT 1, " +
            KEY_VERIFIED + " boolean DEFAULT 0);";

    // Article table create statement
    private static final String CREATE_TABLE_POSTS = "CREATE TABLE " + TABLE_POSTS + "(" +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_RESOURCE + " integer DEFAULT 0, " +
            KEY_AUTHOR + " text null DEFAULT 'EKHDEMNI', " +
            KEY_ID + " text not null, " +
            KEY_TITLE + " text not null, " +
            KEY_CONTENT + " text not null, " +
            KEY_PUBLISHED + " text not null, " +
            KEY_READED_AT + " text null, " +
            KEY_URL + " text not null, " +
            KEY_IMG + " text not null, " +
            KEY_MARKED + " boolean DEFAULT 0, " +
            KEY_READ + " boolean DEFAULT 0, " +
            KEY_SEEN + " boolean DEFAULT 0);";

    // Tag table create statement
    private static final String CREATE_TABLE_TAG = "CREATE TABLE " + TABLE_TAG + "(" +
            KEY_ROWID + " integer primary key autoincrement, " +
            KEY_TITLE + " text not null, " +
            //KEY_POST_NUMBER + " integer DEFAULT 0, " +
            KEY_MARKED + " boolean DEFAULT 0, " +
            KEY_IS_CITY + " boolean DEFAULT 0" +
            ");";

    // todo_tag table create statement
    private static final String CREATE_TABLE_POSTS_TAG = "CREATE TABLE " + TABLE_POSTS_TAG + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_POST_ID + " text not null,"
            + KEY_TAG_TITLE + " text not null,"
            + KEY_CREATED_AT + " DATETIME" + ")";

    private static MyDataBase db;
    static SQLiteHelper sqLiteHelper;
    SQLiteDatabase sqLiteDatabase;
    private Context context;

    public MyDataBase(Context c) {
        context = c;
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized MyDataBase getInstance(Context context) {
        if (db==null)
            db = new MyDataBase(context);
        if (sqLiteHelper == null) {
            sqLiteHelper = new SQLiteHelper(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
        }
        return db;
    }

    public static class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // creating required tables
            db.execSQL(CREATE_TABLE_RESOURCES);
            db.execSQL(CREATE_TABLE_POSTS);
            db.execSQL(CREATE_TABLE_TAG);
            db.execSQL(CREATE_TABLE_POSTS_TAG);
            //db.execSQL("INSERT INTO "+TABLE_TAG+" ("+KEY_TITLE+","+KEY_IS_CITY+") VALUES('أريانة',1), ('باجة',1), ('بن عروس',1), ('بنزرت',1), ('تطاوين',1), ('توزر',1), ('تونس',1), ('جندوبة',1), ('زغوان',1), ('سليانة',1), ('سوسة',1), ('سيدي بوزيد',1), ('صفاقس',1), ('قابس',1), ('قبلي',1), ('القصرين',1), ('قفصة',1), ('القيروان',1), ('الكاف',1), ('مدنين',1), ('المنستير',1), ('منوبة',1), ('المهدية',1), ('نابل',1);");
            //String incriment = "(SELECT MAX("+KEY_ROWID+") + 1 FROM "+TABLE_RESOURCES+")" ;
            //db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+","+KEY_VERIFIED+") VALUES("+incriment+", 'Ekhdemni','http://www.ekhdemni.tn/feeds/posts/default/?alt=rss','http://cdn.ekhdemni.net/images/logo.png', 1);");
            /* db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'الوكالة الوطنية للتشغيل','http://www.emploi.nat.tn/concours.xml','http://www.centresmigrants.tn/images/partenaires/partenaire2.jpg');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Concours tunisie','https://concours-tunisie.tn/feed/','https://concours-tunisie.tn/wp-content/uploads/2018/02/Tunisie-Concours-Logo-carr%C3%A9.png');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Tunisie Travail','http://www.tunisietravail.net/feed','http://static.alouiconsulting.net/tunisietravail/copyright-tunisie-travail-200x200.png');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Job.tn','http://www.job.tn/rss/all','https://www.nashvilleservingveterans.com/uploaded/pageimg/1498752116_tn.png');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Cafe Job','https://cafe-job.net/feed/','https://cafe-job.net/wp-content/uploads/2018/02/Logo-Caf%C3%A9-Job-en-Tunisie-long.png');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Bayt','https://www.bayt.com/live-bookmarks/all-rss.xml','http://img0bm.b8cdn.com/images/logos/fb_bayt_new_en.png');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Tanqeeb','https://tunisia.tanqeeb.com/ar/s/%D9%88%D8%B8%D8%A7%D8%A6%D9%81/%D9%88%D8%B8%D8%A7%D8%A6%D9%81-%D8%AA%D9%88%D9%86%D8%B3?rss=1','https://pbs.twimg.com/profile_images/844554564309266440/196eM_LB.jpg');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'El5edma','http://el5edma.com/?feed=rss2','http://el5edma.com/wp-content/uploads/2014/12/logo5edma3.png');");
            db.execSQL("INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+") VALUES("+incriment+", 'Emploi.rn','http://www.emploi.rn.tn/?format=feed&type=rss','http://www.emploi.rn.tn/images/emploi/2016/02/17/concours-640x405.jpg');");
            */
        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // on upgrade drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAG);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS_TAG);

            // create new tables
            onCreate(db);
        }
    }

    public MyDataBase openToRead() throws SQLException {
        if(sqLiteDatabase != null && sqLiteDatabase.isOpen()){
            sqLiteDatabase.close();
            sqLiteHelper.close();
        }
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public MyDataBase openToWrite() throws SQLException {
        if(sqLiteDatabase != null && sqLiteDatabase.isOpen())
            sqLiteDatabase.close();
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (sqLiteDatabase!=null) sqLiteDatabase.close();
        if (sqLiteHelper!=null) sqLiteHelper.close();
        //sqLiteHelper=null;
        //sqLiteDatabase=null;
    }


    public boolean deleteOldPosts() {
        try {
            String countQuery = "SELECT  * FROM " + TABLE_POSTS;
            Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();
            if (count>=200){
                String d = "DELETE FROM "+TABLE_POSTS+" WHERE id NOT IN (SELECT id FROM "+TABLE_POSTS+" ORDER BY  "+KEY_ROWID+"  DESC LIMIT 200);";
                Cursor c = sqLiteDatabase.rawQuery(d, null);
                c.close();
                return true;//sqLiteDatabase.delete(TABLE_POSTS, KEY_MARKED + " ! = 0 ORDER BY "+KEY_ROWID+" DESC LIMIT "+(count-201), null) > 0;
            }
        }catch (Exception e){
            MyActivity.log("deleteOldPosts Exception => "+e.getMessage());
        }
        return false;
    }

    public long insertPost(Article article) {
        deleteOldPosts();
        article.setTitle(article.getTitle().replace("'","\""));
        article.setContent(article.getContent().replace("'","\""));
        article.setAuthor(article.getAuthor().replace("'","\""));

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, 1);
        initialValues.put(KEY_TITLE, article.getTitle());
        initialValues.put(KEY_RESOURCE, article.resource);
        initialValues.put(KEY_AUTHOR, article.getAuthor());
        initialValues.put(KEY_CONTENT, article.getContent());
        initialValues.put(KEY_PUBLISHED, article.getPublished());
        initialValues.put(KEY_URL, article.getUrl());
        initialValues.put(KEY_IMG, article.getImg());
        initialValues.put(KEY_READ, false);
        initialValues.put(KEY_SEEN, false);
        try {
            return sqLiteDatabase.insert(TABLE_POSTS, null, initialValues);
        }catch (Exception e){
            return 0;
        }
    }

    public Article getPost(String id) throws SQLException {
        Cursor mCursor =
                sqLiteDatabase.query(true, TABLE_POSTS,
                        postColumns,
                        KEY_ROWID + "= " + id,
                        null,
                        null,
                        null,
                        null,
                        LIMIT);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            return getArticle(mCursor);
        }
        return null;
    }


    public List<Article> take(String nbr, long oldThen) throws SQLException {
        MyActivity.log(KEY_ROWID +" oldThen < "+ oldThen);
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                         postColumns,
                        (oldThen != 0) ? KEY_ROWID + " < " + oldThen  : null,
                        null, null, null,
                KEY_ROWID+" DESC",//
                        nbr);

        return getArticles(mCursor);
    }

    public List<Article> takeNewer(String nbr, long newerThen) throws SQLException {
        MyActivity.log(KEY_ROWID +" newerThen > "+ newerThen);
        Cursor mCursor =
                sqLiteDatabase.query(true, TABLE_POSTS,
                        postColumns,
                        (newerThen != 0) ? KEY_ROWID + " > " + newerThen  : null,
                        null, null, null,
                        null,
                        nbr);
        return getArticles(mCursor);
    }

    public boolean markAsUnread(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_READ, false);
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + "=" + id, null) > 0;
    }

    public boolean markAsRead(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_READ, true);
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + "=" + id, null) > 0;
    }

    public boolean updateReadDate(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_READED_AT, (new Date()).getTime()+"");
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + "=" + id, null) > 0;
    }

    public boolean markAsUnSeen(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_SEEN, false);
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + "=" + id, null) > 0;
    }

    public boolean markAsSeen(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_SEEN, true);
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + "=" + id, null) > 0;
    }


    public boolean addPostForCategory(String categoryTitle, long postId) {
        categoryTitle = categoryTitle.replace("'","\"");
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TAG_TITLE, categoryTitle);
        initialValues.put(KEY_POST_ID, postId);
        return sqLiteDatabase.insert(TABLE_POSTS_TAG, null, initialValues)>0;
    }


    public List<Article> getPosts(String title) throws SQLException {
        title = title.replace("'","\"");
        try{
            Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                    postColumns,
                    KEY_TITLE + " LIKE '%" + title + "%'", null, null, null,
                    KEY_ROWID + " DESC", LIMIT);
            return getArticles(mCursor);
        }catch (IllegalStateException e){
            MyActivity.log("getPosts(String title) IllegalStateException"+ e.getMessage());
        }
        return new ArrayList<>();
    }


    public int getResourcePostsCount(long resource) throws SQLException {
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                postColumns,
                KEY_RESOURCE + " = " + resource,
                null, null, null,
                KEY_ROWID + " DESC", null);
        return mCursor.getCount();
    }
    public List<Article> getResourcePosts(long resource, float oldThen) throws SQLException {
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                postColumns,
                KEY_RESOURCE + " = " + resource + ((oldThen != 0) ? " and "+KEY_ROWID + " < " + oldThen  : ""),
                null, null, null,
                KEY_ROWID + " DESC", LIMIT);
        return getArticles(mCursor);
    }


    public int getCategoryPostsNumber(String category) throws SQLException {
        category = category.replace("'","\"");
        String countQuery = "SELECT  * FROM " + TABLE_POSTS_TAG + " WHERE "+ KEY_TAG_TITLE + " = '" + category + "' LIMIT "+LIMIT;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean resourceExist(String title,String url) throws SQLException {
        title = title.replace("'","\"");
        url = url.replace("'","\"");
        String countQuery = "SELECT  * FROM " + TABLE_RESOURCES + " WHERE "+ KEY_TITLE + " = '" + title + "' or "+ KEY_URL + " = '" + url + "' LIMIT "+LIMIT;
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count>0;
    }

    public List<Article> getPostsByCategory(String category, int oldThen) throws SQLException {
        category = category.replace("'","\"");
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS_TAG,
                new String[]{KEY_POST_ID, KEY_TAG_TITLE},
                KEY_TAG_TITLE + " = '" + category + "'" +((oldThen != 0) ? " and "+KEY_ROWID + " < " + oldThen  : ""),
                null, null, null,
                KEY_POST_ID + " DESC", LIMIT);
        if (mCursor != null) {
            mCursor.moveToFirst();
            List<Article> posts = new ArrayList<Article>();
            int count = mCursor.getCount();
            for (int i = 0; i < count; i++) {
                Article a = getPost(mCursor.getString(mCursor.getColumnIndex(KEY_POST_ID)));
                if (a != null)
                    posts.add(a);
                mCursor.moveToNext();
            }
            mCursor.close();
            Log.w(Ekhdemni.TAG, category+" "+count);
            return posts;
        }
        return null;
    }

    public List<Article> getMarkedPosts(float oldThen) throws SQLException {
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                postColumns,
                KEY_MARKED + " = 1" +((oldThen != 0) ? " and "+KEY_ROWID + " < " + oldThen  : ""),
                null, null, null,
                KEY_ROWID + " DESC", LIMIT);
        return getArticles(mCursor);
    }

    public List<Article> getUnreadPosts(float oldThen) throws SQLException {
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                postColumns,
                KEY_READ + "= 0" +((oldThen != 0) ? " and "+KEY_ROWID + " < " + oldThen  : ""),
                null, null, null,
                KEY_ROWID + " DESC", LIMIT);
        return getArticles(mCursor);
    }
    public List<Article> getLastReadedPosts(float oldThen) throws SQLException {
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                postColumns,
                KEY_READ + " = 1" +((oldThen != 0) ? " and "+KEY_ROWID + " < " + oldThen  : ""),
                null, null, null,
                KEY_READED_AT + " DESC", LIMIT);
        return getArticles(mCursor);
    }

    public List<Article> resourceUnreadPosts(long resource) throws SQLException {
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS,
                postColumns,
                KEY_READ + " = 0 and "+ KEY_RESOURCE + " = '"+resource+"'", null, null, null,
                KEY_ROWID + " DESC", null);
        return getArticles(mCursor);
    }



    public List<Category> takeCities(String nbr) throws SQLException {
        Cursor mCursor =
                sqLiteDatabase.query(true, TABLE_TAG,
                        new String[]{KEY_ROWID, KEY_TITLE, KEY_MARKED, KEY_IS_CITY},
                        KEY_IS_CITY + "= '1'",
                        null, null, null,null,
                        nbr);
        return getCategories(mCursor);
    }


    public long insertCategory(String title) {
        if(getCategory(title)==null){
            title = title.replace("'","\"");
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_TITLE, title);
            return sqLiteDatabase.insert(TABLE_TAG, null, initialValues);
        }
        return 0;
    }

    public Category getCategory(String title) throws SQLException {
        title = title.replace("'","\"");
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_TAG,
                new String[]{KEY_ROWID, KEY_TITLE, KEY_MARKED},
                KEY_TITLE + "= '" + title + "'",
                null,
                null,
                null,
                null,
                LIMIT);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            return getCategory(mCursor);
        }
        return null;
    }
    public Resource getResource(String title) throws SQLException {
        title = title.replace("'","\"");
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_RESOURCES,
                resourcesColumns,
                KEY_TITLE + "= '" + title + "'",
                null,
                null,
                null,
                null,
                LIMIT);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            return getResource(mCursor);
        }
        return null;
    }


    public List<Category> takeCategories(String nbr) throws SQLException {
        Cursor mCursor =
                sqLiteDatabase.query(true, TABLE_TAG,
                        new String[]{KEY_ROWID, KEY_TITLE, KEY_MARKED},
                        KEY_IS_CITY + "!= '1'",
                        null, null, null,
                        KEY_ROWID + " DESC",
                        nbr);

        return getCategories(mCursor);
    }


    public List<Resource> takeResources(boolean onlyActive) throws SQLException {
        String country = YDUserManager.get(context, YDUserManager.COUNTRY_KEY);
        country = country==null ? "1" : country;
        MyActivity.log("get resources for country: "+country);
        String where = KEY_COUNTRY + " = " + country;
        if(onlyActive)
            where = KEY_ENABLE + "!= 0 and "+where;
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_RESOURCES,
                resourcesColumns,
                where,
                null, null, null,
                "'" + KEY_POSITION + "' DESC",
                 LIMIT);
        return getResources(mCursor);
    }

    public boolean unmarked(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_MARKED, false);
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + " = " + id, null) > 0;
    }

    public boolean marked(long id) {
        ContentValues args = new ContentValues();
        args.put(KEY_MARKED, true);
        return sqLiteDatabase.update(TABLE_POSTS, args, KEY_ROWID + " = " + id, null) > 0;
    }
    public boolean unmarkedCategory(String title) {
        ContentValues args = new ContentValues();
        args.put(KEY_MARKED, false);
        return sqLiteDatabase.update(TABLE_TAG, args, KEY_TITLE + "='" + title + "'", null) > 0;
    }

    public boolean markedCategory(String title) {
        title = title.replace("'","\"");
        ContentValues args = new ContentValues();
        args.put(KEY_MARKED, true);
        return sqLiteDatabase.update(TABLE_TAG, args, KEY_TITLE + "='" + title + "'", null) > 0;
    }






    public List<Article> getArticles(Cursor mCursor){
        List<Article> posts = new ArrayList<Article>();
        try {
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0; i < mCursor.getCount(); i++) {
                    Article a = getArticle(mCursor);
                    if (a != null)
                        posts.add(a);
                    mCursor.moveToNext();
                }
                mCursor.close();
                return posts;
            }
        }catch (SQLiteCantOpenDatabaseException e){
            //Toast.makeText(context, "can't open db", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            //Toast.makeText(context, "db error => " +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return posts;
    }


    //String[] postColumns = new String[]{KEY_ROWID, KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_PUBLISHED, KEY_URL, KEY_IMG, KEY_MARKED, KEY_READ, KEY_SEEN};

    public Article getArticle(Cursor mCursor){
        Article a = new Article();
        a.setDbId(mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID)));
        a.setId(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(KEY_ID))));
        a.resource = mCursor.getLong(mCursor.getColumnIndex(KEY_RESOURCE));
        a.setTitle(mCursor.getString(mCursor.getColumnIndex(KEY_TITLE)).replace("\"","'"));
        a.setContent(mCursor.getString(mCursor.getColumnIndex(KEY_CONTENT)).replace("\"","'"));
        a.setPublished(mCursor.getString(mCursor.getColumnIndex(KEY_PUBLISHED)));
        a.setReaded_at(mCursor.getString(mCursor.getColumnIndex(KEY_READED_AT)));
        a.setUrl(mCursor.getString(mCursor.getColumnIndex(KEY_URL)));
        a.setImg(mCursor.getString(mCursor.getColumnIndex(KEY_IMG)));
        a.marked = (mCursor.getInt(mCursor.getColumnIndex(KEY_MARKED)) > 0);
        a.read = (mCursor.getInt(mCursor.getColumnIndex(KEY_READ)) > 0);
        a.seen = (mCursor.getInt(mCursor.getColumnIndex(KEY_SEEN)) > 0);
        return a;
    }

    public List<Category> getCategories(Cursor mCursor){
        List<Category> categories = new ArrayList<Category>();
        if (mCursor != null) {
            mCursor.moveToFirst();
            for (int i = 0; i < mCursor.getCount(); i++) {
                categories.add(getCategory(mCursor));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return categories;
    }
    public Category getCategory(Cursor mCursor){
        Category a = new Category();
        a.setTitle(mCursor.getString(mCursor.getColumnIndex(KEY_TITLE)).replace("\"","'"));
        //a.setPostsNumber(mCursor.getInt(mCursor.getColumnIndex(KEY_POST_NUMBER)));
        a.setMarked(mCursor.getInt(mCursor.getColumnIndex(KEY_MARKED)) > 0);
        a.setDbId(mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID)));
        return a;
    }



    public long insertResource(String title, String url, String logo, boolean isVerified, boolean isEnabled) {
        title = title.replace("'","\"");
        url = url.replace("'","\"");
        logo = logo.replace("'","\"");
        int verified = isVerified ? 1 : 0;
        int enable = isEnabled ? 1 : 0;
        String country = YDUserManager.get(context, YDUserManager.COUNTRY_KEY);
        country = country==null ? "1" : country;
        MyActivity.log("insert resource for country: "+country);
        if(!resourceExist(title, url)){
            MyActivity.log("inserted");
            String incriment = "(SELECT MAX("+KEY_ROWID+") + 1 FROM "+TABLE_RESOURCES+")" ;
            String q = "INSERT INTO "+TABLE_RESOURCES+"("+KEY_POSITION+","+KEY_COUNTRY+","+KEY_TITLE+","+KEY_URL+","+KEY_LOGO+","+KEY_VERIFIED+","+KEY_ENABLE+") " +
                    "VALUES("+incriment+" , '"+
                    country  +"', '" +
                    title    +"', '" +
                    url      +"', '" +
                    logo     +"', " +
                    verified +", " +
                    enable +");";
            sqLiteDatabase.execSQL(q);
            MyActivity.log( q);
        }
        return 0;
    }
    public Resource getResource(long id){
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_RESOURCES,
                resourcesColumns,
                KEY_ROWID + " = " + id,
                null,
                null,
                null,
                null,
                LIMIT);
        if (mCursor != null && mCursor.getCount() > 0) {
            mCursor.moveToFirst();
            return getResource(mCursor);
        }
        //Log.w("☻♥♦♣♠☺•◘○","☻♥♦♣♠☺•◘○ ☻♥♦♣♠☺•◘○ can't find resource");
        return new Resource();
    }



    public Resource getResource(Cursor mCursor){
        Resource a = new Resource();
        int n = mCursor.getInt(mCursor.getColumnIndex(KEY_ENABLE_NOTIFICATION)) ;//KEY_ENABLE_NOTIFICATION
        a.enabledNotification = n > 0;
        Log.w(Ekhdemni.TAG, "a.enabledNotification "+n);
        a.verified = (mCursor.getInt(mCursor.getColumnIndex(KEY_VERIFIED)) > 0);
        a.enabled = (mCursor.getInt(mCursor.getColumnIndex(KEY_ENABLE)) > 0);
        a.logo = (mCursor.getString(mCursor.getColumnIndex(KEY_LOGO)));
        a.position = (mCursor.getInt(mCursor.getColumnIndex(KEY_POSITION)));
        a.country = (mCursor.getString(mCursor.getColumnIndex(KEY_COUNTRY)));
        a.title = (mCursor.getString(mCursor.getColumnIndex(KEY_TITLE)).replace("\"","'"));
        a.url = mCursor.getString(mCursor.getColumnIndex(KEY_URL));
        a.last_update = mCursor.getLong(mCursor.getColumnIndex(KEY_LAST_UPDATE));
        a.dbId = (mCursor.getLong(mCursor.getColumnIndex(KEY_ROWID)));
        return a;
    }
    public List<Resource> getResources(Cursor mCursor){
        List<Resource> resources = new ArrayList<Resource>();
        if (mCursor != null) {
            mCursor.moveToFirst();
            for (int i = 0; i < mCursor.getCount(); i++) {
                resources.add(getResource(mCursor));
                mCursor.moveToNext();
            }
        }
        mCursor.close();
        return resources;
    }
    public boolean updateResourceDate(long id, long lastUpdate) {
        ContentValues args = new ContentValues();
        args.put(KEY_LAST_UPDATE, lastUpdate);
        return sqLiteDatabase.update(TABLE_RESOURCES, args, KEY_ROWID + " = " + id, null) > 0;
    }
    public boolean updateResourcePosition(long id, int position) {
        ContentValues args = new ContentValues();
        args.put(KEY_POSITION, position);
        return sqLiteDatabase.update(TABLE_RESOURCES, args, KEY_ROWID + " = " + id, null) > 0;
    }

    public boolean enableResource(long id, boolean b) {
        ContentValues args = new ContentValues();
        args.put(KEY_ENABLE, b);
        return sqLiteDatabase.update(TABLE_RESOURCES, args, KEY_ROWID + " = " + id, null) > 0;
    }
    public boolean enableNotificationResource(long id, boolean b) {
        ContentValues args = new ContentValues();
        args.put(KEY_ENABLE_NOTIFICATION, b);
        return sqLiteDatabase.update(TABLE_RESOURCES, args, KEY_ROWID + " = " + id, null) > 0;
    }
    public boolean deleteResource(long id) {
        sqLiteDatabase.delete(TABLE_POSTS, KEY_RESOURCE + "=" +id, null);
        deleteEmptyCategory();
        deleteUnvalidRelations();
        Log.d("☺☻☺ DELETing resource ☺", "children deleted ☺☻☺");
        return sqLiteDatabase.delete(TABLE_RESOURCES, KEY_ROWID + "=" +id, null) > 0;
    }
    public boolean deleteArticle(long id) {
        return sqLiteDatabase.delete(TABLE_POSTS, KEY_ROWID + "=" +id, null) > 0;
    }


    public boolean deleteCategory(long id) {
        return sqLiteDatabase.delete(TABLE_TAG, KEY_ROWID + "=" +id, null) > 0;
    }
    public boolean deleteUnvalidRelations() {
        String count = "(SELECT count(*) FROM "+TABLE_TAG+" WHERE "+KEY_TITLE+" = "+TABLE_TAG+"."+KEY_TITLE+")";
        return sqLiteDatabase.delete(TABLE_POSTS_TAG, count + "=" +0, null) > 0;
    }
    public boolean deleteEmptyCategory() {
        String count = "(SELECT count(*) FROM "+TABLE_POSTS_TAG+" WHERE "+KEY_TAG_TITLE+" = "+TABLE_TAG+"."+KEY_TITLE+")";
        return sqLiteDatabase.delete(TABLE_TAG, count + "=" +0, null) > 0;
    }
    public boolean deleteCategoryWithPosts(long id, String name) {
        if(deleteCategoryPosts(name)){
            return deleteEmptyCategory();
            //return sqLiteDatabase.delete(TABLE_TAG, KEY_ROWID + "=" +id, null) > 0;
        }
        else return false;
    }
    public boolean deleteCategoryPosts(String category) throws SQLException {
        category = category.replace("'","\"");
        Cursor mCursor = sqLiteDatabase.query(true, TABLE_POSTS_TAG,
                new String[]{KEY_ID, KEY_POST_ID, KEY_TAG_TITLE},
                KEY_TAG_TITLE + "= '" + category + "'", null, null, null,
                KEY_ID + " DESC", null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            for (int i = 0; i < mCursor.getCount(); i++) {
                String postId = mCursor.getString(mCursor.getColumnIndex(KEY_POST_ID));
                // delete post
                sqLiteDatabase.delete(TABLE_POSTS, KEY_ROWID+ "=" +postId, null);
                // delete post in post_tag table
                sqLiteDatabase.delete(TABLE_POSTS_TAG, KEY_POST_ID+ "='" + postId + "'", null);
                mCursor.moveToNext();
            }
            // delete category in post_tag table (may be zayda)
            sqLiteDatabase.delete(TABLE_POSTS_TAG, KEY_TAG_TITLE+ "='" + category + "'", null);
            mCursor.close();
            return true;
        }
        return false;
    }

}