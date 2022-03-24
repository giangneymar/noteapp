package com.example.notesapp.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.databinding.ItemNotesBinding;
import com.example.notesapp.model.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ItemHolder> implements Filterable{

    private ArrayList<Note> notes;
    private final ArrayList<Note> notesTemp;
    private final NoteClickListener clickListener;

    public NoteAdapter(NoteClickListener clickListener, ArrayList<Note> notes) {
        this.clickListener = clickListener;
        this.notes = notes;
        this.notesTemp = notes;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final ItemNotesBinding binding;

        public ItemHolder(ItemNotesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindNote(Note note) {
            binding.getRoot().setOnClickListener(view -> clickListener.itemClick(note));
            binding.imgDelete.setOnClickListener(view -> clickListener.deleteNote(note));
        }
    }

    //handle recyclerview
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNotesBinding binding = ItemNotesBinding.inflate(layoutInflater, parent, false);
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bindNote(notes.get(position));
        holder.binding.setNote(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    //handle event search
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if (search.isEmpty()) {
                    notes = notesTemp;
                } else {
                    ArrayList<Note> mNotes = new ArrayList<>();
                    for (Note note : notesTemp) {
                        if (note.getTitle().toLowerCase().contains(search.toLowerCase())) {
                            mNotes.add(note);
                        }
                    }
                    notes = mNotes;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = notes;
                return filterResults;
            }
            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface NoteClickListener{
        void itemClick(Note note);
        void deleteNote(Note note);
    }
}
