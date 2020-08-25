package net.ekhdemni.presentation.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.ekhdemni.R;
import net.ekhdemni.presentation.base.MyActivity;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.model.feeds.db.MyDataBase;
import tn.core.presentation.base.MyFragment;
import net.ekhdemni.model.models.Category;
import net.ekhdemni.presentation.ui.views.tagview.Tag;
import net.ekhdemni.presentation.ui.views.tagview.TagView;
import net.ekhdemni.utils.AlertUtils;


public class TagsFragment extends MyFragment {

    private TagView tagGroup;
    Context context;
    private EditText editText;
    private List<TagClass> tagList;
    private List<Category> categoriesList;

    @Override
    public void clean() {
        super.clean();
        tagGroup = null;
        editText = null;
        tagList = null;
    }

    public TagsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    Category byName(String c){
        for (Category category: categoriesList) {
            if(category.getTitle().equals(c))
                return category;
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_tags, container, false);
        context = getContext();
        tagGroup = view.findViewById(R.id.tag_group);
        editText = view.findViewById(R.id.editText);
        editText.requestFocus();
        prepareTags();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w(s+"", s+"");
                setTags(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tagGroup.setOnTagLongClickListener(new TagView.OnTagLongClickListener() {
            @Override
            public void onTagLongClick(Tag tag, int position) {
                //Toast.makeText(context, "Long Click: " + tag.text, Toast.LENGTH_SHORT).show();
            }
        });

        tagGroup.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(Tag tag, int position) {
                //editText.setText(tag.text);
                //editText.setSelection(tag.text.length());//to set cursor position
                Category c = byName(tag.text);
                if(c!=null)
                    ((MyActivity) getActivity()).setFragment(FeedsFragment.newInstance(FeedsFragment.Filter.category, c));

            }
        });
        tagGroup.setOnTagDeleteListener(new TagView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(final TagView view, final Tag tag, final int position) {

                final Category c = byName(tag.text);
                if(c!=null){
                    AlertUtils.Action action = new AlertUtils.Action() {
                        public void doFunction(Object o) {
                            c.destroy(context, true);
                            view.remove(position);
                            Toast.makeText(context, "\"" + tag.text + "\" deleted", Toast.LENGTH_SHORT).show();
                        }
                    };
                    action.message = "\"" + tag.text + "\" will be delete. Are you sure?";
                    AlertUtils.alert(context, action);
                }
            }
        });


        return view;
    }











    private void prepareTags() {
        tagList = new ArrayList<>();
        MyDataBase dba = MyDataBase.getInstance(context);
        dba.openToRead();
        categoriesList = dba.takeCategories("200");
        dba.close();
        for (int i = 0; i < categoriesList.size(); i++) {
            int postsNbr = categoriesList.get(i).getPostsNumber(context);
            tagList.add(new TagClass(categoriesList.get(i).getTitle(), postsNbr));
        }

        addALL(tagList);
    }

    void addALL(List<TagClass> tgs){
        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag;
        for (int i = 0; i < tgs.size(); i++) {
            tag = new Tag(tgs.get(i).getName(), tgs.get(i).number);
            tag.radius = 24f;
            tag.layoutColor = Color.parseColor(tgs.get(i).getColor());
            tag.isDeletable = true;
            tags.add(tag);
        }
        Collections.sort(tags);
        tagGroup.addTags(tags);
    }

    private void setTags(CharSequence cs) {
        /**
         * for empty edittext
         */
        if (cs.toString().equals("")) {
            addALL(tagList);
            return;
        }

        String text = cs.toString();
        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag;


        for (int i = 0; i < tagList.size(); i++) {
            if (tagList.get(i).getName().toLowerCase().startsWith(text.toLowerCase())) {
                tag = new Tag(tagList.get(i).getName(), tagList.get(i).number);
                tag.radius = 14f;
                tag.layoutColor = Color.parseColor(tagList.get(i).getColor());
                if (i % 2 == 0) // you can set deletable or not
                    tag.isDeletable = true;
                tags.add(tag);
            }
        }
        tagGroup.addTags(tags);

    }

    public class TagClass {

        private String code;
        private String name;
        private String color;
        public  int number;

        public TagClass() {

        }

        public TagClass(String name, int number) {
            this.code = "blablabla";
            this.number = number;
            this.name = name;
            this.color = getRandomColor();
        }

        public String getRandomColor() {
            ArrayList<String> colors = new ArrayList<>();
            colors.add("#ED7D31");
            colors.add("#00B0F0");
            colors.add("#FF0000");
            colors.add("#D0CECE");
            colors.add("#00B050");
            colors.add("#9999FF");
            colors.add("#FF5FC6");
            colors.add("#FFC000");
            colors.add("#7F7F7F");
            colors.add("#4800FF");

            return colors.get(new Random().nextInt(colors.size()));
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSinif() {
            return code;
        }

        public void setSinif(String code) {
            this.code = code;
        }


    }


}
