package org.sast.lostfound;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private Spinner mCategorySpinner;
    private EditText mDescriptionEditText;
    private EditText mOwnerEditText;
    private EditText mContactEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameEditText = findViewById(R.id.name_edit_text);
        mCategorySpinner = findViewById(R.id.category_spinner);
        mDescriptionEditText = findViewById(R.id.description_edit_text);
        mOwnerEditText = findViewById(R.id.owner_edit_text);
        mContactEditText = findViewById(R.id.contact_edit_text);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);

        Button registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameEditText.getText().toString();
                String category = mCategorySpinner.getSelectedItem().toString();
                String description = mDescriptionEditText.getText().toString();
                String owner = mOwnerEditText.getText().toString();
                String contact = mContactEditText.getText().toString();
                // 将失物信息存储到数据库中
            }
        });
    }
}