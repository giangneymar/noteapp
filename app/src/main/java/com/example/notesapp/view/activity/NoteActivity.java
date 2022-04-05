package com.example.notesapp.view.activity;

import static com.example.notesapp.utils.KeyConstant.CODE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstant.NOTE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstant.NOTE_INFO;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteBinding;
import com.example.notesapp.databinding.DialogDeleteBinding;
import com.example.notesapp.model.Note;
import com.example.notesapp.view.adapter.NoteAdapter;
import com.example.notesapp.viewmodel.NoteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements NoteAdapter.NoteClickListener {
    /*
    Area : variable
     */

    private ActivityNoteBinding binding;
    private NoteViewModel viewModel;
    private long backPressed;
    private NoteAdapter noteAdapter;

    /*
    Area : function
     */

    private void initAll() {
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        getObserveNotes();
        setDateNow();
    }

    private void getObserveNotes() {
        viewModel.getNotes().observe(this, this::setRecyclerView);
    }

    private void setRecyclerView(List<Note> notes) {
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(this, notes);
        }
        binding.listNote.setLayoutManager(new LinearLayoutManager(this));
        binding.listNote.setAdapter(noteAdapter);
    }

    private void setDateNow() {
        Date currentTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date = dateFormat.format(currentTime);
        binding.dateNow.setText(date);
    }

    private void onClick() {
        binding.add.setOnClickListener(view -> {
            Intent intent = new Intent(NoteActivity.this, NoteDetailActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void setSearchView() {
        EditText txtSearch = ((EditText) binding.search.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        binding.search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                noteAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    /*
    Area : override
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initAll();
        setSearchView();
        onClick();
    }

    @Override
    public void onItemClick(Note note) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(NOTE_INFO, note);
        Intent intent = new Intent(NoteActivity.this, NoteDetailActivity.class);
        intent.putExtras(bundle);
        intent.putExtra(NOTE_CHECK_ICON, CODE_CHECK_ICON);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void deleteNote(Note note) {
        BottomSheetDialog dialog = new BottomSheetDialog(NoteActivity.this, R.style.DeleteDialogStyle);
        DialogDeleteBinding bindingDialog = DialogDeleteBinding.inflate(getLayoutInflater());
        dialog.setContentView(bindingDialog.getRoot());
        bindingDialog.yes.setOnClickListener(viewYes -> {
            viewModel.deleteNote(note.getId());
            dialog.dismiss();
        });
        bindingDialog.no.setOnClickListener(viewNo -> dialog.dismiss());
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Snackbar snackbar = Snackbar.make(binding.noteLayout, R.string.check_exit, Snackbar.LENGTH_SHORT);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.YELLOW);
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.BLUE);
            snackbar.show();
        }
        backPressed = System.currentTimeMillis();
    }
}