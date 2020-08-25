package tn.core.util;

import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
    public static String getText(TextInputLayout inputLayout){
        return inputLayout.getEditText().getText()==null ? "" : inputLayout.getEditText().getText().toString();
    }
    public static String getText(EditText editText){
        return editText.getText()==null ? "" : editText.getText().toString();
    }

    public static void htmlToView(TextView textView, String html){
        htmlToViewNonClickable(textView, html);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setLinksClickable(true);

    }
    public static void htmlToViewNonClickable(TextView textView, String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }


    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(
                    text.substring(urlMatcher.start(0), urlMatcher.end(0))
            );
        }

        return containedUrls;
    }


    public static String replaceUrlsWithAnchorTag(String text){
        List<String> list = extractUrls(text);
        for (String t: list) {
            text = t.replace(t, "<a href=\""+t+"\">"+t+"</a>");
        }
        return text;
    }







    public interface LinkListener{
        void onClick(String url);
    }


    protected static void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span, final LinkListener listener)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                listener.onClick(span.getURL());
                //Toast.makeText(view.getContext(), span.getURL(), Toast.LENGTH_SHORT).show();
                // Do something with span.getURL() to handle the link click...
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    public static void setTextViewHTML(TextView text, String html, LinkListener listener)
    {
        CharSequence sequence = Html.fromHtml(html);
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span, listener);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }




}
