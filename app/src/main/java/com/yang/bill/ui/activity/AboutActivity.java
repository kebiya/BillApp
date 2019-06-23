package com.yang.bill.ui.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.yang.bill.BuildConfig;
import com.yang.bill.R;

import androidx.annotation.NonNull;
import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;

/**
 * 关于activity
 */
public class AboutActivity extends AbsAboutActivity {
    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText("个人记账本");
        version.setText("v " + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull Items items) {
        items.add(new Card(getString(R.string.about_introduce)));
    }
}
