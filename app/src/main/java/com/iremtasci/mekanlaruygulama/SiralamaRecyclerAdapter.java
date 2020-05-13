package com.iremtasci.mekanlaruygulama;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//normalde bir image downloading kütüphanesi


public class SiralamaRecyclerAdapter extends RecyclerView.Adapter<SiralamaRecyclerAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private ArrayList<String> userCommentList;
    private ArrayList<String> userImageList;
    private ArrayList<String> userKonumList;

    public SiralamaRecyclerAdapter(ArrayList<String> userEmailList, ArrayList<String> userCommentList, ArrayList<String> userImageList, ArrayList<String> userKonumList) {
        this.userEmailList = userEmailList;
        this.userCommentList = userCommentList;
        this.userImageList = userImageList;
        this.userKonumList = userKonumList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //oluşturunca ne yapacağım

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        //bağlanınca ne yapacağım
        holder.konumText.setText(userKonumList.get(position));
        holder.userEmailText.setText(userEmailList.get(position));
        holder.commentText.setText(userCommentList.get(position));
        Picasso.get().load(userImageList.get(position)).into(holder.imageView);

    }

    @Override
    public int getItemCount() {//kaç row olacak onlar
        return userEmailList.size();
    }

    //her şeyi bağlayacak sınıfımız
    class PostHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView userEmailText;
        TextView commentText;
        TextView konumText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recyclerview_row_imageview);
            userEmailText = itemView.findViewById(R.id.recyclerview_row_useremail_text);
            commentText = itemView.findViewById(R.id.recyclerview_row_comment_text);
            konumText = itemView.findViewById(R.id.recyclerview_row_location_text);

        }
        //görünüm tutucu
    }

    public void  updateList(ArrayList<String> newList){

        userCommentList = new ArrayList<>();
        userCommentList.addAll(newList);
        notifyDataSetChanged();
    }
}
