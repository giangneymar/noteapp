package com.example.notesapp.view.activity;

import static com.example.notesapp.utils.KeyConstant.SAVE_AVATAR;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteBinding;
import com.example.notesapp.databinding.DialogDeleteBinding;
import com.example.notesapp.model.Note;
import com.example.notesapp.view.adapter.NoteAdapter;
import com.example.notesapp.viewmodel.NoteViewModel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class NoteActivity extends AppCompatActivity implements NoteAdapter.NoteClickListener {

    private ActivityNoteBinding binding;
    private NoteViewModel viewModel;
    private long backPressed;
    private NoteAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        getNotes();
        onClick();
        setAvatar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getNotes();
    }

    private void setAvatar() {
        String image = sharedPreferences.getString(SAVE_AVATAR, "");
        if (image.equals("")) {
            binding.avatar.setImageResource(R.drawable.giang);
        } else {
            byte[] imageAsBytes = Base64.decode(image.getBytes(), Base64.DEFAULT);
            binding.avatar.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }
    }

    private void init() {
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        }

        if (adapter == null) {
            adapter = new NoteAdapter(this);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(binding.getRoot().getContext());
            layoutManager.setFlexDirection(FlexDirection.ROW);
            layoutManager.setJustifyContent(JustifyContent.FLEX_START);
            layoutManager.setAlignItems(AlignItems.FLEX_START);
            binding.listNote.setLayoutManager(layoutManager);
            binding.listNote.setAdapter(adapter);
        }

        sharedPreferences = getSharedPreferences(SAVE_AVATAR, Context.MODE_PRIVATE);
        setTime();
    }

    private void setTime() {
        int hour = Calendar.getInstance().getTime().getHours();
        if (hour >= 0 && hour <= 11) {
            binding.time.setText(R.string.good_morning);
        } else if (hour >= 12 && hour <= 18) {
            binding.time.setText(R.string.good_afternoon);
        } else {
            binding.time.setText(R.string.good_evening);
        }
    }

    private void getNotes() {
        viewModel.getNotes();
        viewModel.getLiveNotes().observe(NoteActivity.this, notes -> {
            adapter.setData(notes);
        });
    }

    private void onClick() {
        binding.add.setOnClickListener(view -> {
            NoteDetailActivity.start(NoteActivity.this, "add");
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
        binding.search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                search(editable.toString());
            }
        });
        binding.avatar.setOnClickListener(view -> pickImage());
    }

    private void search(String key) {
        viewModel.getSearchNote(key);
        viewModel.getLiveSearchNote().observe(this, notes -> adapter.setData(notes));
    }

    @Override
    public void onItemClick(Note note) {
        NoteDetailActivity.start(NoteActivity.this, "edit", note);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void deleteNote(Note note) {
        Dialog dialog = new Dialog(this);
        DialogDeleteBinding binding = DialogDeleteBinding.inflate(getLayoutInflater());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding.getRoot());

        binding.ok.setOnClickListener(view -> {
            viewModel.deleteNote(note.getId());
            getNotes();
            dialog.dismiss();
        });
        binding.cancel.setOnClickListener(view -> dialog.dismiss());

        setWindow(dialog);
    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this, R.string.check_exit, Toast.LENGTH_SHORT).show();
        }
        backPressed = System.currentTimeMillis();
    }

    private void setWindow(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.show();
    }

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        pickImage.launch(photoPickerIntent);
    }

    private ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                final Uri imageUri = data.getData();
                                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                binding.avatar.setImageBitmap(selectedImage);

                                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.PNG, 100, bao);
                                byte[] b = bao.toByteArray();
                                String encoded = Base64.encodeToString(b, Base64.DEFAULT);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(SAVE_AVATAR, encoded);
                                editor.apply();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
}