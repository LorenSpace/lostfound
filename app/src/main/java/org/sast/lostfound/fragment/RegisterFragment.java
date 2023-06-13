package org.sast.lostfound.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class RegisterFragment extends Fragment {
    private static final String TAG = "LostItem Test";
    private final LostItem lostItem;
    private EditText mNameEditText;
    private Spinner mCategorySpinner;
    private EditText mDescriptionEditText;
    private EditText mLocationEditText;
    private DatePicker mDatePicker;
    private ImageView mImageView;
    private TimePicker mTimePicker;
    // 分别处理拍照和从相册中选择图片的 ActivityResultLauncher
    private ActivityResultLauncher<Intent> mTakePictureLauncher;
    private ActivityResultLauncher<String> mGetContentLauncher;
    private ActivityResultLauncher<String> mRequestCameraPermissionLauncher;

    private Uri mPhotoUri;

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
        mTakePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 拍照完成，处理照片
                        // 将图片转换为字节数组
                        InputStream inputStream;
                        try {
                            inputStream = requireActivity()
                                    .getContentResolver().openInputStream(mPhotoUri);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("LostItem Test", "onCreateView: " + mPhotoUri);
                        // 将图片路径保存到 LostItem 中
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        // 将图片显示到 ImageView 中
                        mImageView.setImageBitmap(bitmap);
                    } else {
                        // 用户取消了拍照，可以提示用户
                        Toast.makeText(requireContext(), "取消拍照", Toast.LENGTH_SHORT).show();
                    }
                });

        /* 由于 Android 10 引入了分区存储机制，
         因此在 Android 10 及以上版本中，query() 方法可能会返回 null，导致无法获取真实路径。
         为了解决这个问题，可以使用 ActivityResultContracts.OpenDocument() API，
         可以在 Android 10 及以上版本中访问分区存储中的文件，并且可以获取文件的真实路径。 */
        // Android 存在沙盒机制，无法直接获取文件的真实路径，并且这种只适用于 Gallery
        mGetContentLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(), uri -> {
                    if (uri != null) {
                        try {
                            // 将图片转换为字节数组
                            InputStream inputStream = requireActivity()
                                    .getContentResolver().openInputStream(uri);
                            // 将图片路径保存到 LostItem 中
                            String filePath = getRealPathFromUri(requireContext(), uri);
                            if (filePath != null) {;
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                // 将图片显示到 ImageView 中
                                mImageView.setImageBitmap(bitmap);
                                lostItem.setPhotoPath(filePath);
                            } else {
                                Toast.makeText(getContext(),
                                        "获取图片路径失败，请从相册中选择图片",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });

        mRequestCameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // 用户已授予相机权限，执行拍照操作
                        dispatchTakePictureIntent();
                    } else {
                        // 用户拒绝了相机权限请求，给出提示或者执行其他操作
                        Toast.makeText(requireContext(), "需要相机权限才能拍照", Toast.LENGTH_SHORT).show();
                    }
                });
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    private void dispatchTakePictureIntent() {
        // 创建一个 Intent，用于启动相机
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if (photoFile != null) {
                mPhotoUri = FileProvider.getUriForFile(requireContext(),
                        "org.sast.lostfound.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                mTakePictureLauncher.launch(takePictureIntent);
            }
        } else {
            Toast.makeText(requireContext(), "无法启动相机", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化组件
        mNameEditText = view.findViewById(R.id.name_edit_text);
        mCategorySpinner = view.findViewById(R.id.register_category_spinner);
        mDescriptionEditText = view.findViewById(R.id.description_edit_text);
        mLocationEditText = view.findViewById(R.id.location_edit_text);
        mDatePicker = view.findViewById(R.id.date_picker);
        mTimePicker = view.findViewById(R.id.time_picker);
        mImageView = view.findViewById(R.id.register_item_image_view);


        // 设置失物类别下拉列表
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter
                .createFromResource(requireContext(), R.array.register_category_array,
                        android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(categoryAdapter);

        // 设置拍照按钮的点击事件
        Button captureButton = view.findViewById(R.id.capture_button);
        captureButton.setOnClickListener(v -> {
            // 检查相机权限
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onViewCreated: " + "相机权限已经被授权");
                // 相机权限已经被授权，执行拍照操作
                dispatchTakePictureIntent();
            } else {
                Log.d(TAG, "onViewCreated: " + "相机权限没有被授权");
                // 相机权限没有被授权，请求相机权限
                mRequestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
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
            int hour = mTimePicker.getHour();
            int minute = mTimePicker.getMinute();
            // 创建一个 Calendar 对象，并设置年、月、日、时、分等时间信息
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day, hour, minute);
            // 将 Calendar 对象转换为 Date 对象
            Date date = calendar.getTime();

            if (lostItem.getPhotoPath() != null) {
                // 设置失物信息
                lostItem.setTitle(name);
                lostItem.setCategory(category);
                lostItem.setDescription(description);
                lostItem.setLocation(location);
                lostItem.setStatus(getString(R.string.status_unclaimed));
                lostItem.setTime(date);

                Log.i(TAG, lostItem.toString());
                lostItemDAO.addLostItem(lostItem);
                Log.i("RegisterFragment", lostItem.toString());
                Toast.makeText(view.getContext(), "登记成功", Toast.LENGTH_SHORT).show();

                // 清空控件内容
                mNameEditText.setText("");
                mDescriptionEditText.setText("");
                mLocationEditText.setText("");
                mImageView.setImageBitmap(null);
            } else {
                Toast.makeText(view.getContext(), "请上传物品照片", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 获取文件路径
    // 查询媒体库获取文件路径
    private String getRealPathFromUri(Context context, Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_CONTENT)) {
            try (Cursor cursor = context.getContentResolver().query(uri, projection,
                    null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    return cursor.getString(index);
                }
            }
        } else if (Objects.equals(uri.getScheme(), ContentResolver.SCHEME_FILE)) {
            return uri.getPath();
        }
        return null;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
                .format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        mPhotoUri = Uri.parse(imageFile.getAbsolutePath());
        lostItem.setPhotoPath(imageFile.getAbsolutePath());
        return imageFile;
    }
}