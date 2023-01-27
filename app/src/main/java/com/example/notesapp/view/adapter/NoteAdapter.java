package com.example.notesapp.view.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.databinding.ItemNotesBinding;
import com.example.notesapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ItemHolder> {

    private final List<Note> notes;
    private final NoteClickListener clickListener;

    public class ItemHolder extends RecyclerView.ViewHolder {

        private final ItemNotesBinding binding;

        public ItemHolder(ItemNotesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(Note note) {
            binding.title.setText(note.getTitle());
            onClick(note);
        }

        @SuppressLint("NotifyDataSetChanged")
        public void onClick(Note note) {
            binding.getRoot().setOnClickListener(view -> clickListener.onItemClick(note));
            binding.getRoot().setOnLongClickListener(view -> {
                clickListener.deleteNote(note);
                return true;
            });
            int[] colors = {Color.rgb(192, 192, 192),
                    Color.rgb(198, 226, 255),
                    Color.YELLOW,
                    Color.rgb(255, 250, 240),
                    Color.rgb(245, 245, 220),
                    Color.rgb(255, 218, 185),
                    Color.rgb(238, 224, 229),
                    Color.rgb(224, 255, 255),
                    Color.rgb(255, 246, 143)};
            binding.itemNote.setCardBackgroundColor(colors[(int) Math.floor(Math.random() * colors.length)]);
        }
    }

    public NoteAdapter(NoteClickListener clickListener) {
        this.clickListener = clickListener;
        this.notes = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNotesBinding binding = ItemNotesBinding.inflate(layoutInflater, parent, false);
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Note note = notes.get(position);
        holder.setData(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public interface NoteClickListener {
        void onItemClick(Note note);

        void deleteNote(Note note);
    }
}
