package net.ekhdemni.presentation.ui.activities.settings;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import net.ekhdemni.R;
import net.ekhdemni.presentation.ui.activities.MainActivity;
import net.ekhdemni.presentation.base.MyActivity;
import tn.core.util.LocaleManager;

/**
 * Created by X on 2/11/2018.
 */

public class SettingsPrefActivity extends AppCompatPreferenceActivity {

    //private static final String TAG = SettingsPrefActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);

            // gallery EditText change listener
            bindPreferenceSummaryToValue(findPreference(getString(R.string.key_gallery_name)));

            // notification preference change listener
            //bindPreferenceSummaryToValue(findPreference(getString(R.string.key_notifications_new_message_ringtone)));





            Preference soundPref = findPreference(getString(R.string.key_notifications_new_message_ringtone));
            soundPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){

                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    //String sound = preference.getSharedPreferences().getString(getString(R.string.key_notifications_new_message_ringtone), "");

                    //String sound = prefManager.getPreference().getString(getString(R.string.key_notifications_new_message_ringtone),"plucky.mp3");

                    MediaPlayer mp = new MediaPlayer();
                    //if(mp!=null && mp.isPlaying())
                    //    mp.stop();
                    switch (o.toString()){
                        case "plucky": mp = MediaPlayer.create(getActivity(), R.raw.plucky); break;
                        case "wrinkle": mp =  MediaPlayer.create(getActivity(), R.raw.wrinkle); break;
                        case "inquisitiveness": mp = MediaPlayer.create(getActivity(), R.raw.inquisitiveness); break;
                        case "sound": mp = MediaPlayer.create(getActivity(), R.raw.sound); break;
                    }
                    if(mp!=null)
                        mp.start();
                    Log.w("soundsoundsound", " : "+o);
                    return true;
                }
            });






            Preference prefLanguages = findPreference(getString(R.string.key_languages));
            prefLanguages.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener(){
                @Override
                public boolean onPreferenceChange(Preference preference, Object o) {
                    setLanguages(preference, o);
                    return true;
                }
            });




            Preference feedbackPref = findPreference(getString(R.string.key_send_feedback));
            feedbackPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    sendFeedback(getActivity());
                    return true;
                }
            });
        }

        void setLanguages(Preference preference, Object o){
            MyActivity.log(o.toString());
            LocaleManager.setNewLocale(getActivity(), o.toString(), false, MainActivity.class);
        }
    }








    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference,
                PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(),  "")
        );
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary( index >= 0 ? listPreference.getEntries()[index] : null );

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);
                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(R.string.summary_choose_ringtone);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else if (preference instanceof EditTextPreference) {
                if (preference.getKey().equals("key_gallery_name")) {
                    // update the changed gallery name to summary filed
                    preference.setSummary(stringValue);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"yassine.dabbous@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from Ekhdemni android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
}
