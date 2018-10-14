package com.example.android.mychat.newContacts;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.mychat.R;
import com.example.android.mychat.User;
import com.example.android.mychat.databinding.SearchResultItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    List<User> users;

    public SearchAdapter(){
        users = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchResultItemBinding searchResultItemBinding = DataBindingUtil.
                inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.search_result_item,parent,false);

        return new SearchViewHolder(searchResultItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.searchResultItemBinding.setSearchResult(new SearchItemViewModel(users.get(position)));
    }

    public void switchUsers(List<User> newUsers){
        if(users!=newUsers){
            users.clear();
            users.addAll(newUsers);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if(users.isEmpty())return 0;
        return users.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        SearchResultItemBinding searchResultItemBinding;
        public SearchViewHolder(SearchResultItemBinding binding) {
            super(binding.searchResultLayout);
            this.searchResultItemBinding = binding;
        }
    }
}
