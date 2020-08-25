package net.ekhdemni.presentation.ui.fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.text.Html;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import net.ekhdemni.R;
import net.ekhdemni.model.feeds.db.MyDataBase;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.utils.ProgressUtils;

import tn.core.presentation.base.MyFragment;
import tn.core.presentation.base.loader.BaseLoader;
import tn.core.model.net.net.NetworkUtils;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewResourceFragment extends MyFragment {

    private EditText mNameEditText, mUrlEditText;
    Context context;
    Button saveBtn;

    @Override
    public void clean() {
        super.clean();
        if(saveBtn != null) saveBtn.setOnClickListener(null);
        saveBtn = null;
        mNameEditText = mUrlEditText = null;
    }

    public NewResourceFragment() {
        // Required empty public constructor
    }

    public static NewResourceFragment newInstance() {

        Bundle args = new Bundle();

        NewResourceFragment fragment = new NewResourceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_resource, container, false);
        context = getContext();
        TextView url_textview = (TextView) view.findViewById(R.id.url_textview);
        TextView name_textview = (TextView) view.findViewById(R.id.name_textview);
        mNameEditText = (EditText) view.findViewById(R.id.feed_title);
        mUrlEditText = (EditText) view.findViewById(R.id.feed_url);
        saveBtn = (Button) view.findViewById(R.id.saveBtn);



        saveBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                final String name = mNameEditText.getText().toString().trim();
                final String urlOrSearch = mUrlEditText.getText().toString().trim();
                if (urlOrSearch.isEmpty()) {
                    Toast.makeText(context, R.string.network_error, Toast.LENGTH_SHORT).show();
                }
                if (!urlOrSearch.contains(".") || !urlOrSearch.contains("/") || urlOrSearch.contains(" ")) {
                    final ProgressDialog pd = ProgressUtils.getProgressDialog(context);
                    pd.show();

                    getLoaderManager().restartLoader(1, null, new LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>>() {


                        @Override
                        public Loader<ArrayList<HashMap<String, String>>> onCreateLoader(int id, Bundle args) {
                            String encodedSearchText = urlOrSearch;
                            try {
                                encodedSearchText = URLEncoder.encode(urlOrSearch, "UTF-8");
                            } catch (UnsupportedEncodingException ignored) {
                            }

                            return new GetFeedSearchResultsLoader(context, encodedSearchText);
                        }

                        @Override
                        public void onLoadFinished(Loader<ArrayList<HashMap<String, String>>> loader, final ArrayList<HashMap<String, String>> data) {
                            pd.cancel();

                            if (data == null) {
                                Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show();
                            } else if (data.isEmpty()) {
                                Toast.makeText(context, R.string.no_result, Toast.LENGTH_SHORT).show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setTitle(R.string.feed_search);

                                // create the grid item mapping
                                String[] from = new String[]{FEED_SEARCH_TITLE, FEED_SEARCH_DESC};
                                int[] to = new int[]{android.R.id.text1, android.R.id.text2};

                                // fill in the grid_item layout
                                SimpleAdapter adapter = new SimpleAdapter(context, data, R.layout.item_search_result, from,
                                        to);
                                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                         MyDataBase db = MyDataBase.getInstance(context);
                         db.openToWrite();
                         db.insertResource(name.isEmpty() ? data.get(which).get(FEED_SEARCH_TITLE) : name,
                                           data.get(which).get(FEED_SEARCH_URL),
                                           data.get(which).get(FEED_SEARCH_LOGO), false, true);
                         db.close();

                                        getActivity().setResult(-1);
                                        getFragmentManager().beginTransaction().replace(R.id.content_main, new ResourcesFragment()).commit();
                                    }
                                });
                                builder.show();
                            }
                        }

                        @Override
                        public void onLoaderReset(Loader<ArrayList<HashMap<String, String>>> loader) {
                        }
                    });
                }else if(Patterns.WEB_URL.matcher(urlOrSearch).matches()) {
                    MyDataBase db = MyDataBase.getInstance(context);
                    db.openToWrite();
                    db.insertResource(name, urlOrSearch, "", false, true);
                    db.close();
                    getActivity().setResult(-1);
                    getFragmentManager().beginTransaction().replace(R.id.content_main, new ResourcesFragment()).commit();
                }
            }
        });

        return view;
    }


















    static final String FEED_SEARCH_TITLE = "title";
    static final String FEED_SEARCH_URL = "feedId";
    static final String FEED_SEARCH_DESC = "description";
    static final String FEED_SEARCH_LOGO = "visualUrl";

    static class GetFeedSearchResultsLoader extends BaseLoader<ArrayList<HashMap<String, String>>> {
        private final String mSearchText;

        public GetFeedSearchResultsLoader(Context context, String searchText) {
            super(context);
            mSearchText = searchText;
        }

        /**
         * This is where the bulk of our work is done. This function is called in a background thread and should generate a new set of data to be
         * published by the loader.
         */
        @Override
        public ArrayList<HashMap<String, String>> loadInBackground() {
            String jsonStr = null;
            try {
                HttpURLConnection conn = NetworkUtils.setupConnection("http://cloud.feedly.com/v3/search/feeds?count=20&locale=" + getContext().getResources().getConfiguration().locale.getLanguage() + "&query=" + mSearchText);
                try {
                    jsonStr = new String(NetworkUtils.getBytes(conn.getInputStream()));

                    // Parse results
                    final ArrayList<HashMap<String, String>> results = new ArrayList<>();
                    JSONArray entries = new JSONObject(jsonStr).getJSONArray("results");
                    for (int i = 0; i < entries.length(); i++) {
                        try {
                            JSONObject entry = (JSONObject) entries.get(i);
                            String url = entry.get(FEED_SEARCH_URL).toString().replace("feed/", "");
                            if (!url.isEmpty()) {
                                HashMap<String, String> map = new HashMap<>();
                                map.put(FEED_SEARCH_TITLE, Html.fromHtml(entry.get(FEED_SEARCH_TITLE).toString()).toString());
                                map.put(FEED_SEARCH_LOGO, Html.fromHtml(entry.get(FEED_SEARCH_LOGO).toString()).toString());
                                map.put(FEED_SEARCH_URL, url);
                                map.put(FEED_SEARCH_DESC, Html.fromHtml(entry.get(FEED_SEARCH_DESC).toString()).toString());

                                results.add(map);
                            }
                        } catch (Exception ignored) {
                            MyActivity.log( "parse resource error: "+ignored.getMessage());
                        }
                    }

                    return results;
                } finally {
                    conn.disconnect();
                }
            } catch (Exception e) {
                MyActivity.log( e.getStackTrace().toString());
                MyActivity.log( "response: "+jsonStr);
                return null;
            }
        }
    }

}
