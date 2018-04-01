package daysstudio.com.frenchrevolution.ProductList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import daysstudio.com.frenchrevolution.ListItem.Product;
import daysstudio.com.frenchrevolution.MainActivity;
import daysstudio.com.frenchrevolution.ProductActivity;
import daysstudio.com.frenchrevolution.R;

import static daysstudio.com.frenchrevolution.MainActivity.contacts;

/**
 * Created by JinHunLee on 2018. 3. 31..
 */

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.Holder> {

    private Context context;
    private List<Product> list;

    public ListViewAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }



    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        //여기에 세부설정

        holder.productImg.setImageDrawable(list.get(position).img);
        holder.pruductName.setText(list.get(position).name);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView productImg;
        public TextView pruductName;
        public RelativeLayout full;


        public Holder(View view) {
            super(view);
            view.setOnClickListener(this);
            productImg = view.findViewById(R.id.list_product_img);
            pruductName = view.findViewById(R.id.list_product_name);
        }

        @Override
        public void onClick(View view) {
            Intent temp = new Intent(context , ProductActivity.class);
            temp.putExtra("num",getAdapterPosition());
            context.startActivity(temp);
        }
    }

}