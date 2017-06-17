package com.example.android.task2_hacker_mode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ImageWithCaption> imagesWithCaption = new ArrayList<>();
    ImagesAdaptor adaptor;
    Button addButton;
    ListView imagesList;
    Intent takePicture;
    Intent pickPhoto;
    CharSequence colors[];
    EditText captionTextbox;
    String captionText;
    Button captionButton;
    Bitmap capturedImage;
    Bitmap selectedImage;
    int flagForIntentPhoto = 0;
    int index = 0, flag = 0;
    String path;
    Uri uri;
    ImageWithCaption selecteditem;
    int indexForCropIntent;
    int flagForPermissions = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButton = (Button) findViewById(R.id.add_button);
        imagesList = (ListView) findViewById(R.id.list_view);
        adaptor = new ImagesAdaptor(this, imagesWithCaption);
        imagesList.setAdapter(adaptor);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == flag) {

                    colors = new CharSequence[]{"Camera", "Gallery"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Pick a photo from");
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                flagForIntentPhoto = 0;
                                takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, flagForIntentPhoto);
                            } else if (which == 1) {
                                flagForIntentPhoto = 1;
                                pickPhoto = new Intent(Intent.ACTION_PICK,
                                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, flagForIntentPhoto);
                            }
                        }
                    });
                    builder.show();
                } else
                    Toast.makeText(getApplicationContext(), "First add the caption!", Toast.LENGTH_SHORT).show();
            }
        });
        captionButton = (Button) findViewById(R.id.caption_button);
        captionTextbox = (EditText) findViewById(R.id.caption_textbox);
        captionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == (index - 1)) {
                    captionText = captionTextbox.getText().toString();
                    if (flagForIntentPhoto == 0)
                        imagesWithCaption.set(flag, new ImageWithCaption(capturedImage, captionText));
                    else
                        imagesWithCaption.set(flag, new ImageWithCaption(selectedImage, captionText));
                    adaptor.notifyDataSetChanged();
                    flag++;
                    captionTextbox.getText().clear();
                } else {
                    Toast.makeText(getApplicationContext(), "First add the image!", Toast.LENGTH_SHORT).show();
                    captionTextbox.getText().clear();
                }
            }
        });
        imagesList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        imagesList.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                int checkedCount = imagesList.getCheckedItemCount();
                mode.setTitle(checkedCount + " Selected");
                adaptor.toggleSelection(position);
                adaptor.notifyDataSetChanged();

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.main_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        SparseBooleanArray selected = adaptor.getSelectedIds();

                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                if (flag == index) {
                                    ImageWithCaption selecteditem = adaptor.getItem(selected.keyAt(i));
                                    imagesWithCaption.remove(selecteditem);
                                    adaptor.notifyDataSetChanged();
                                    flag--;
                                    index--;
                                } else
                                    Toast.makeText(getApplicationContext(), "First fill the list completely!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        mode.finish();
                        return true;
                    case R.id.crop:
                        isStoragePermissionGranted();
                        if (flagForPermissions == 0) {
                            if (flag == index) {
                                SparseBooleanArray selected1 = adaptor.getSelectedIds();
                                if (selected1.size() != 1)
                                    Toast.makeText(getApplicationContext(), "Select a single item to crop!", Toast.LENGTH_SHORT).show();
                                else {
                                    selecteditem = adaptor.getItem(selected1.keyAt((selected1.size() - 1)));
                                    try {

                                        uri = getImageUri(getApplicationContext(), selecteditem.getimageResouseID());
                                        Intent cropIntent = new Intent("com.android.camera.action.CROP");
                                        cropIntent.setDataAndType(uri, "image/*");
                                        cropIntent.putExtra("crop", true);
                                        cropIntent.putExtra("aspectX", 1);
                                        cropIntent.putExtra("aspectY", 1);
                                        cropIntent.putExtra("outputX", 128);
                                        cropIntent.putExtra("outputY", 128);
                                        cropIntent.putExtra("return-data", true);
                                        indexForCropIntent = selected1.keyAt((selected1.size() - 1));
                                        startActivityForResult(cropIntent, 2);

                                    } catch (ActivityNotFoundException anfe) {
                                        Toast.makeText(getApplicationContext(), "Whoops - your device doesn't support the crop action!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                                mode.finish();
                                return true;
                            } else
                                Toast.makeText(getApplicationContext(), "Fill all the fields first!", Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(getApplicationContext(), "Cropping requires this permission!", Toast.LENGTH_SHORT).show();

                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adaptor.removeSelection();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    capturedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    capturedImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File destination = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imagesWithCaption.add(index, new ImageWithCaption(capturedImage, "Add caption"));
                    adaptor.notifyDataSetChanged();
                    index++;
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    selectedImage = null;
                    if (imageReturnedIntent != null) {
                        try {
                            selectedImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageReturnedIntent.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    imagesWithCaption.add(index, new ImageWithCaption(selectedImage, "Add caption"));
                    adaptor.notifyDataSetChanged();
                    index++;
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    selecteditem = adaptor.getItem(indexForCropIntent);
                    Bitmap bmp = imageReturnedIntent.getExtras().getParcelable("data");
                    imagesWithCaption.set(indexForCropIntent, new ImageWithCaption(bmp, selecteditem.getcaption()));
                    adaptor.notifyDataSetChanged();
                    File file = new File(path);
                    file.delete();
                }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                flagForPermissions = 0;
            } else {
                flagForPermissions = -1;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);

            }
        } else {
            flagForPermissions = 0;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 7:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    flagForPermissions = 0;
                } else flagForPermissions = -1;
        }

    }
}
