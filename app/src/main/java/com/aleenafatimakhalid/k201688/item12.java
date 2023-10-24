package com.aleenafatimakhalid.k201688;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class item12 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item12);

        ItemModel item = getIntent().getParcelableExtra("item");

        ViewPager2 imageViewPager = findViewById(R.id.linear1);
        ImageAdapter adapter = new ImageAdapter(item.getImageUrls());
        imageViewPager.setAdapter(adapter);
        TextView hourlyRateTextView = findViewById(R.id.twelve);
        TextView cityTextView = findViewById(R.id.city);
        TextView descriptionTextView = findViewById(R.id.description);


        if (item != null) {
            String hourlyRateText = "$" + item.getHourlyRate();
            hourlyRateTextView.setText(hourlyRateText);
            String City = item.getMatch();
            cityTextView.setText((City));
            descriptionTextView.setText((item.getDescription()));
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });    }
}