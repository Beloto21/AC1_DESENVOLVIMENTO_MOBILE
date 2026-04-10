package com.example.ac1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdaptarContato extends BaseAdapter {

    private Context context;
    private List<Contato> contatoes;

    public AdaptarContato(Context context, List<Contato> contatoes) {
        this.context = context;
        this.contatoes = contatoes;
    }

    @Override
    public int getCount() {
        return contatoes.size();
    }

    @Override
    public Object getItem(int position) {
        return contatoes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return contatoes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false);
        }

        Contato contato = contatoes.get(position);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvPhone = convertView.findViewById(R.id.tvPhone);
        TextView tvEmail = convertView.findViewById(R.id.tvEmail);
        TextView tvCategory = convertView.findViewById(R.id.tvCategory);
        TextView tvCity = convertView.findViewById(R.id.tvCity);

        tvName.setText(contato.getName() + (contato.isFavorite() ? " ⭐" : ""));
        tvPhone.setText(contato.getPhone());
        tvEmail.setText(contato.getEmail());
        tvCategory.setText(contato.getCategory());
        tvCity.setText(contato.getCity());

        return convertView;
    }
}
