package com.example.eshop.ViewHolder;

import android.view.View;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.eshop.Interface.ItemClickListner;
        import com.example.eshop.R;

public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtproductName, txtproductDescription, txtProductPrice,txtProductStatus;
    public ImageView imageView;
    public ItemClickListner listner;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.seller_product_image);
        txtproductName = (TextView) itemView.findViewById(R.id.seller_product_name);
        txtproductDescription = (TextView) itemView.findViewById(R.id.seller_product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.seller_product_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.seller_product_status);
    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v, getAdapterPosition(), false );
    }
}
