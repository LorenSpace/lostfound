package org.sast.lostfound.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import org.sast.lostfound.R;
import org.sast.lostfound.dao.LostItemDAO;
import org.sast.lostfound.model.LostItem;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class RegisterFragment extends Fragment {
    private EditText mNameEditText;
    private Spinner mCategorySpinner;
    private EditText mDescriptionEditText;
    private EditText mLocationEditText;
    private DatePicker mDatePicker;
    private ImageView mImageView;
    private final LostItem lostItem;

    // 分别处理拍照和从相册中选择图片的 ActivityResultLauncher
    private ActivityResultLauncher<Intent> mTakePictureLauncher;
    private ActivityResultLauncher<String> mGetContentLauncher;

    private static final String TAG = "LostItem Test";

    public RegisterFragment() {
        // Required empty public constructor
        lostItem = new LostItem();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTakePictureLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 在这里处理拍照的结果
                        Bitmap bitmap = BitmapFactory.decodeFile(lostItem.getPhotoPath());
                        mImageView.setImageBitmap(bitmap);
                        // 将图片转换为字节数组
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    }
                });

        mGetContentLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), mImageUri -> {
                    if (mImageUri != null) {
                        try {
                            // 将图片转换为字节数组
                            InputStream inputStream = requireActivity()
                                    .getContentResolver().openInputStream(mImageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            // 将图片显示到 ImageView 中
                            mImageView.setImageBitmap(bitmap);
                            // 将图片路径保存到 LostItem 中
                            String uri = getRealPathFromUri(requireContext(), mImageUri);
                            lostItem.setPhotoPath(uri);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化组件
        mNameEditText = view.findViewById(R.id.name_edit_text);
        mCategorySpinner = view.findViewById(R.id.category_spinner);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);
        mLocationEditText = view.findViewById(R.id.location_edit_text);
        mDatePicker = view.findViewById(R.id.date_picker);
        mImageView = view.findViewById(R.id.image_view);

        // 设置失物类别下拉列表
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                .createFromResource(requireContext(), R.array.category_array,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);

        // 设置拍照按钮的点击事件
        Button captureButton = view.findViewById(R.id.capture_button);
        captureButton.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(requireActivity()
                    .getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(requireContext(),
                            "org.sast.lostfound.file-provider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    mTakePictureLauncher.launch(takePictureIntent);
                }
            }
        });

        // 设置上传照片按钮的点击事件
        Button uploadButton = view.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(v -> {
            mGetContentLauncher.launch("image/*");
        });

        LostItemDAO lostItemDAO = new LostItemDAO(view.getContext());
        Button registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            // 将失物信息存储到数据库中
            String name = mNameEditText.getText().toString();
            String category = mCategorySpinner.getSelectedItem().toString();
            String description = mDescriptionEditText.getText().toString();
            String location = mLocationEditText.getText().toString();

            // 获取日期
            int year = mDatePicker.getYear();
            int month = mDatePicker.getMonth();
            int day = mDatePicker.getDayOfMonth();
            LocalDate localDate = LocalDate.of(year, month, day);
            Date date = Date.from(localDate
                    .atStartOfDay(ZoneId.systemDefault()).toInstant());

            // 设置失物信息
            lostItem.setTitle(name);
            lostItem.setCategory(category);
            lostItem.setDescription(description);
            lostItem.setLocation(location);
            lostItem.setStatus(getString(R.string.status_unclaimed));
            lostItem.setTime(date);

            Log.i(TAG, lostItem.toString());
            lostItemDAO.addLostItem(lostItem);
            Toast.makeText(view.getContext(), "登记成功", Toast.LENGTH_SHORT).show();
        });

    }

    // 获取文件路径
    private String getRealPathFromUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor == null) {
            return null;
        }
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        lostItem.setPhotoPath(image.getAbsolutePath());
        return image;
    }
}