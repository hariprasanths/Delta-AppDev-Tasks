package com.example.android.task2_normal_mode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        }
    }
}
