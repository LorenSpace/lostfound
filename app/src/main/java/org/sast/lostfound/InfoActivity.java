package org.sast.lostfound;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sast.lostfound.model.LostItem;

import java.io.File;
import java.util.Date;

public class InfoActivity extends AppCompatActivity {
    private LostItem item;
    private TextView statusTextView;
    private TextView claimTimeTextView;
    private Button claimButton;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        item = (LostItem) getIntent().getSerializableExtra("lost_item");

        TextView titleTextView = findViewById(R.id.title_text_view);
        TextView locationTextView = findViewById(R.id.location_text_view);
        ImageView photoImageView = findViewById(R.id.photo_image_view);
        TextView descriptionTextView = findViewById(R.id.description_text_view);
        statusTextView = findViewById(R.id.status_text_view);
        claimButton = findViewById(R.id.claim_button);
        claimTimeTextView = findViewById(R.id.claim_time_text_view);
        TextView categoryTextView = findViewById(R.id.catalogue_text_view);

        // 显示失物详情
        if (item != null) {
            setTitle(item.getTitle());
            titleTextView.setText(item.getTitle());
            locationTextView.setText(item.getLocation());
            File photoFile = new File(item.getPhotoPath());
            if (photoFile.exists()) {
                Bitmap photoBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                photoImageView.setImageBitmap(photoBitmap);
            }
            descriptionTextView.setText(item.getDescription());
            categoryTextView.setText(item.getCategory());
            statusTextView.setText(item.getStatus());
            if (item.getStatus().equals(getString(R.string.status_unclaimed))) {
                claimButton.setVisibility(View.VISIBLE);
                claimTimeTextView.setVisibility(View.GONE);
                claimButton.setOnClickListener(v -> {
                    // 弹出对话框确认认领
                    AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                    builder.setTitle(R.string.claim_dialog_title)
                            .setMessage(R.string.claim_dialog_message)
                            .setPositiveButton(R.string.claim_dialog_positive_button,
                                    (dialog, which) -> {
                                        // 更新失物认领状态和认领时间
                                        item.setStatus(getString(R.string.status_claimed));
                                        item.setTime(new Date());
                                        Toast.makeText(InfoActivity.this,
                                                R.string.claim_success_message, Toast.LENGTH_SHORT).show();
                                        statusTextView.setText(item.getStatus());
                                        claimButton.setVisibility(View.GONE);
                                        claimTimeTextView.setVisibility(View.VISIBLE);
                                        claimTimeTextView.setText(getString(R.string.claim_time_prefix)
                                                + " " + DateFormat.format("yyyy-MM-dd HH:mm:ss",
                                                item.getTime()));
                                    })
                            .setNegativeButton(R.string.claim_dialog_negative_button, null)
                            .show();
                });
            } else {
                claimButton.setVisibility(View.GONE);
                claimTimeTextView.setVisibility(View.VISIBLE);
                claimTimeTextView.setText(getString(R.string.claim_time_prefix) + " "
                        + DateFormat.format("yyyy-MM-dd HH:mm:ss", item.getTime()));
            }
        }
    }

}