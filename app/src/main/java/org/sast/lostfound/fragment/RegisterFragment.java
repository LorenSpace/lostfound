package org.sast.lostfound.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import org.sast.lostfound.R;

public class RegisterFragment extends Fragment {
    private EditText mNameEditText;
    private Spinner mCategorySpinner;
    private EditText mDescriptionEditText;
    private EditText mOwnerEditText;
    private EditText mContactEditText;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mNameEditText = view.findViewById(R.id.name_edit_text);
        mCategorySpinner = view.findViewById(R.id.category_spinner);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);
        mOwnerEditText = view.findViewById(R.id.owner_edit_text);
        mContactEditText = view.findViewById(R.id.contact_edit_text);

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                .createFromResource(view.getContext(), R.array.category_array,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);

        Button registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            String name = mNameEditText.getText().toString();
            String category = mCategorySpinner.getSelectedItem().toString();
            String description = mDescriptionEditText.getText().toString();
            String owner = mOwnerEditText.getText().toString();
            String contact = mContactEditText.getText().toString();
            // 将失物信息存储到数据库中
        });
        return view;
    }
}