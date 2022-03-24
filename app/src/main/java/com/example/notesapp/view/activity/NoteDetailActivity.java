package com.example.notesapp.view.activity;

import static com.example.notesapp.utils.KeyConstants.CODE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstants.NOTE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstants.NOTE_INFO;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteDetailBinding;
import com.example.notesapp.model.Note;
import com.example.notesapp.viewmodel.NoteViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {
    private ActivityNoteDetailBinding binding;
    private NoteViewModel viewModel;
    private Note note;
    public static MenuItem menuItemEdit, menuItemAdd, menuItemNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note_detail);
        initViewModel();
        initToolbar();
        getInfoNote();
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolBar);
        binding.toolBar.setNavigationIcon(R.drawable.ic_back);
        binding.toolBar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void getInfoNote() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        note = bundle.getParcelable(NOTE_INFO);
        if (note != null) {
            binding.setNote(note);
        }
    }

    private boolean checkEmpty(String title, String content) {
        if (title.isEmpty()) {
            setSnackBar(R.string.input_title);
            setStatusInput(false);
            return true;
        }
        if (content.isEmpty()) {
            setSnackBar(R.string.input_content);
            setStatusInput(false);
            return true;
        }
        return false;
    }

    private void setSnackBar(int message){
        Snackbar snackbar = Snackbar.make(binding.noteDetailLayout, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.retry, view -> {
            setStatusInput(true);
            snackbar.dismiss();
        });
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.YELLOW);
        TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.BLUE);
        snackbar.show();
    }

    private void setStatusInput(boolean status){
        binding.titleNote.setEnabled(status);
        binding.contentNote.setEnabled(status);
    }

    // handler toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_note_activity, menu);
        menuItemAdd = menu.findItem(R.id.menuAdd);
        menuItemEdit = menu.findItem(R.id.menuEdit);
        menuItemNewNote = menu.findItem(R.id.menuNewNote);
        menuItemNewNote.setVisible(false);
        if (getIntent().getIntExtra(NOTE_CHECK_ICON, -1) == CODE_CHECK_ICON) {
            menuItemEdit.setVisible(true);
        } else {
            menuItemAdd.setVisible(true);
        }
        return true;
    }

    //handle add edit note in toolbar
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NoteActivity.notes.clear();
        String title = binding.titleNote.getText().toString();
        String content = binding.contentNote.getText().toString();
        Date currentTime = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(currentTime);
        if (item.getItemId() == R.id.menuAdd) {
            if (checkEmpty(title, content)) {
                return false;
            }
            viewModel.insertNote(getApplicationContext(), title, content, date);
        }
        if (item.getItemId() == R.id.menuEdit) {
            note = getIntent().getExtras().getParcelable(NOTE_INFO);
            int id = note.getId();
            if (checkEmpty(title, content)) {
                return false;
            }
            viewModel.updateNote(getApplicationContext(), id, title, content, date);
        }
        NoteActivity.noteAdapter.notifyDataSetChanged();
        finish();
        return super.onOptionsItemSelected(item);
    }

    //handle back
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}