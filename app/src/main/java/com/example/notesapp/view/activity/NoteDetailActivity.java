package com.example.notesapp.view.activity;

import static com.example.notesapp.utils.KeyConstant.CODE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstant.NOTE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstant.NOTE_INFO;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteDetailBinding;
import com.example.notesapp.model.Note;
import com.example.notesapp.viewmodel.NoteViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetailActivity extends AppCompatActivity {
    /*
    Area : variable
     */
    private ActivityNoteDetailBinding binding;
    private NoteViewModel viewModel;
    private Note noteTemp;

    /*
    Area : function
     */

    private void initAll() {
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    }

    private void onClick() {
        onClickAddEditNote();
        onClickBack();
    }

    private void onClickAddEditNote() {
        binding.add.setOnClickListener(view -> checkEvent(false));
        if (getIntent().getIntExtra(NOTE_CHECK_ICON, -1) == CODE_CHECK_ICON) {
            binding.add.setText("Sửa");
            getInfoNote();
            binding.add.setOnClickListener(view -> checkEvent(true));
        }
    }

    private void checkEvent(boolean isUpdate) {
        String title = binding.titleNote.getText().toString().trim();
        String content = binding.contentNote.getText().toString().trim();
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(currentTime);
        if (title.isEmpty()) {
            setSnackBar(R.string.input_title);
            setStatusInput(false);
        } else if (content.isEmpty()) {
            setSnackBar(R.string.input_content);
            setStatusInput(false);
        } else {
            Note note = new Note();
            note.setTitle(title);
            note.setContent(content);
            note.setDate(date);
            if (isUpdate) {
                note.setId(noteTemp.getId());
                viewModel.updateNote(note);
            } else {
                viewModel.insertNote(note);
            }
            finish();
        }
    }

    private void setSnackBar(int message) {
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

    private void setStatusInput(boolean status) {
        binding.titleNote.setEnabled(status);
        binding.contentNote.setEnabled(status);
    }

    private void getInfoNote() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        Note note = bundle.getParcelable(NOTE_INFO);
        if (note != null) {
            noteTemp = note;
            binding.setNote(note);
        }
    }

    private void onClickBack() {
        binding.back.setOnClickListener(view -> onBackPressed());
    }

    /*
    Area : override
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAll();
        onClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}