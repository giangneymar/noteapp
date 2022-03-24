package com.example.notesapp.view.activity;

import static com.example.notesapp.utils.KeyConstants.CODE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstants.NOTE_CHECK_ICON;
import static com.example.notesapp.utils.KeyConstants.NOTE_INFO;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteBinding;
import com.example.notesapp.model.Note;
import com.example.notesapp.view.adapter.NoteAdapter;
import com.example.notesapp.viewmodel.NoteViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity implements NoteAdapter.NoteClickListener {
    private ActivityNoteBinding binding;
    private NoteViewModel viewModel;
    private long backPressed;
    @SuppressLint("StaticFieldLeak")
    public static NoteAdapter noteAdapter;
    public static ArrayList<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note);
        initNoteList();
        initViewModel();
        initRecyclerView();
        initToolbar();
        initSearchView();
    }

    private void initNoteList() {
        if (notes == null) {
            notes = new ArrayList<>();
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        initTable();
        getNotes();
    }

    private void initRecyclerView() {
        if (noteAdapter == null) {
            noteAdapter = new NoteAdapter(this, notes);
        }
        binding.listNote.setLayoutManager(new GridLayoutManager(this, 1));
        binding.listNote.setAdapter(noteAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(binding.toolBar);
    }

    private void initSearchView() {
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

    private void initTable() {
        viewModel.createTableNote(getApplicationContext());
    }

    private void getNotes() {
        viewModel.getNotesObserver(this).observe(this, notesLive -> {

        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteDialog(int position) {
        BottomSheetDialog dialog = new BottomSheetDialog(NoteActivity.this, R.style.DeleteDialogStyle);
        View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.dialog_delete, findViewById(R.id.dialogDelete));
        dialog.setContentView(view);
        Button btnYes = view.findViewById(R.id.btnYes);
        Button btnNo = view.findViewById(R.id.btnNo);
        btnYes.setOnClickListener(viewYes -> {
            viewModel.deleteNote(getApplicationContext(), position);
            noteAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        btnNo.setOnClickListener(viewNo -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    //handle event in toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_note_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuNewNote) {
            Intent intent = new Intent(NoteActivity.this, NoteDetailActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }

    //handle click listeners
    @Override
    public void itemClick(Note note) {
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
        notes.clear();
        deleteDialog(note.getId());
    }

    //handle back
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