package com.example.notesapp.view.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.databinding.ItemNotesBinding;
import com.example.notesapp.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ItemHolder> implements Filterable {
    /*
    Area : variable
     */

    private List<Note> notes;
    private final List<Note> notesTemp;
    private final NoteClickListener clickListener;

    /*
    Area : inner class
     */

    public class ItemHolder extends RecyclerView.ViewHolder {
        /*
        Area : variable
         */
        private final ItemNotesBinding binding;

        /*
        Area : function
         */

        public ItemHolder(ItemNotesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindNote(int position) {
            Note note = notes.get(position);
            binding.setNote(note);
            binding.getRoot().setOnClickListener(view -> clickListener.onItemClick(note));
            binding.imgDelete.setOnClickListener(view -> clickListener.deleteNote(note));
            int[] colors = {Color.rgb(0, 255, 0),
                    Color.rgb(51, 153, 255),
                    Color.YELLOW,
                    Color.rgb(229, 204, 255),
                    Color.rgb(255, 204, 204)};
            binding.itemNote.setCardBackgroundColor(colors[(int) Math.floor(Math.random() * colors.length)]);
            binding.executePendingBindings();
        }
    }

    /*
    Area : function
     */

    public NoteAdapter(NoteClickListener clickListener, List<Note> notes) {
        this.clickListener = clickListener;
        this.notes = notes;
        this.notesTemp = notes;
    }

    /*
    Area : override
     */

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemNotesBinding binding = ItemNotesBinding.inflate(layoutInflater, parent, false);
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bindNote(position);
    }

    @Override
    public int getItemCount() {
        if (notes == null) {
            return 0;
        }
        return notes.size();
    }

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

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notes = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface NoteClickListener {
        void onItemClick(Note note);

        void deleteNote(Note note);
    }
}
