package com.androidtan.favoperdana.androidngunyah;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element adsElement = new Element();
        adsElement.setTitle("Aplikasi Order Food Restoran (Ngunyah)");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.favo)
                .setDescription("FAVO PERDANA H.S. (21120116120015)")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with me")
                .addEmail("perdanafavo97@gmail.com")
                .addWebsite("https://www.androidtan.com")
                .addTwitter("https://twitter.com/FavoPerdanaHS")
                .addFacebook("https://www.facebook.com/favoperdanahs")
                .addInstagram("https://www.instagram.com/favophs")
                .addGitHub("https://github.com/perdanafavo")
                .addItem(createCopyright())
                .create();

        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        final String copyrightString = String.format("Copyright %d by FavoPHS", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIcon(R.mipmap.ic_launcher);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AboutActivity.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
    }
}
