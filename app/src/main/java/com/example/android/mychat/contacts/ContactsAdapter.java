package com.example.android.mychat.contacts;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.android.mychat.R;
import com.example.android.mychat.databinding.ContactItemViewBinding;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {
    private List<Contact> contacts;

    public ContactsAdapter() {
        contacts = new ArrayList<>();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactItemViewBinding contactItemViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_item_view,parent,false);
        return new ContactViewHolder(contactItemViewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        ContactItemViewBinding contactItemViewBinding = holder.contactItemViewBinding;
        contactItemViewBinding.setContact(new ContactItemViewModel(contacts.get(position)));
    }

    public void switchContacts(List<Contact> newContacts){
        if(contacts!=newContacts){
            contacts.clear();
            contacts.addAll(newContacts);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        if(contacts!=null) return contacts.size();
        return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        private ContactItemViewBinding contactItemViewBinding;
        public ContactViewHolder(ContactItemViewBinding binding) {
            super(binding.itemContactLayout);
            this.contactItemViewBinding = binding;
        }
    }
}