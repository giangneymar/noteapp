package com.example.notesapp.view.activity;

import static com.example.notesapp.utils.KeyConstant.ACTION;
import static com.example.notesapp.utils.KeyConstant.NOTE_INFO;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteDetailBinding;
import com.example.notesapp.model.Note;
import com.example.notesapp.viewmodel.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteDetailActivity extends AppCompatActivity {

    private ActivityNoteDetailBinding binding;
    private NoteViewModel viewModel;
    private String action;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        action = getIntent().getStringExtra(ACTION);
        init();
        onClick();
    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
    }

    private void onClick() {
        binding.back.setOnClickListener(view -> onBackPressed());
        if (action.equals("add")) {
            binding.done.setOnClickListener(view -> {
                String title = binding.title.getText().toString().trim();
                String content = binding.content.getText().toString().trim();
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_time), Locale.ENGLISH);
                String date = dateFormat.format(currentTime);

                if (!title.isEmpty() && !content.isEmpty()) {
                    Note note = new Note();
                    note.setTitle(title);
                    note.setContent(content);
                    note.setDate(date);
                    viewModel.insertNote(note);
                    finish();
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                }
            });
        }

        if (action.equals("edit")) {
            getInfoNote();
            binding.done.setOnClickListener(view -> {
                String title = binding.title.getText().toString().trim();
                String content = binding.content.getText().toString().trim();
                Date currentTime = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.format_time), Locale.ENGLISH);
                String date = dateFormat.format(currentTime);

                if (!title.isEmpty() && !content.isEmpty()) {
                    Note noteU = new Note();
                    noteU.setId(note.getId());
                    noteU.setTitle(title);
                    noteU.setContent(content);
                    noteU.setDate(date);
                    viewModel.updateNote(noteU);
                    finish();
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getInfoNote() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            return;
        }
        note = bundle.getParcelable(NOTE_INFO);
        if (note != null) {
            binding.title.setText(note.getTitle());
            binding.content.setText(note.getContent());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public static void start(Context context, String action, Note note) {
        Intent starter = new Intent(context, NoteDetailActivity.class);
        starter.putExtra(ACTION, action);
        starter.putExtra(NOTE_INFO, note);
        context.startActivity(starter);
    }

    public static void start(Context context, String action) {
        Intent starter = new Intent(context, NoteDetailActivity.class);
        starter.putExtra(ACTION, action);
        context.startActivity(starter);
    }
}