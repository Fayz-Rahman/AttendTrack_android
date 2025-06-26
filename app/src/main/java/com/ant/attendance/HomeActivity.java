package com.ant.attendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        setContentView(R.layout.activity_home);

        CardView manageClassesCard = findViewById(R.id.card_manage_classes);
        CardView takeAttendanceCard = findViewById(R.id.card_take_attendance);

        manageClassesCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ClassesActivity.class));
        });

        takeAttendanceCard.setOnClickListener(v -> {
            startActivity(new Intent(this, TakeAttendanceActivity.class));
        });

        View mainContainer = findViewById(R.id.main_container);
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer, (v, insets) -> {
            int systemBarsTop = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
            int systemBarsBottom = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;

            v.setPadding(v.getPaddingLeft(),
                    systemBarsTop,
                    v.getPaddingRight(),
                    systemBarsBottom);

            return insets;
        });

    }
}
