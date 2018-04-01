package daysstudio.com.frenchrevolution.Ranking;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import daysstudio.com.frenchrevolution.R;

/**
 * Created by JinHunLee on 2018. 4. 1..
 */

public class RankingListAdapter extends BaseAdapter {

    private Context context;
    private List<RankStructure> list;

    public RankingListAdapter(Context context ,List<RankStructure> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            convertView = View.inflate(context , R.layout.ranking_list_item , null);
        }

        ImageView RankImage = convertView.findViewById(R.id.list_ranking_img);
        TextView  RankNumber = convertView.findViewById(R.id.list_ranking_rank);
        TextView  RankScore = convertView.findViewById(R.id.list_ranking_point);
        TextView  RankName = convertView.findViewById(R.id.list_ranking_name);

        if(position + 1 == 1)
            RankImage.setImageDrawable(context.getDrawable(R.drawable.gold));
        else if(position + 1 == 2)
            RankImage.setImageDrawable(context.getDrawable(R.drawable.silver));
        else if(position +1 == 3)
            RankImage.setImageDrawable(context.getDrawable(R.drawable.bronze));
        else
            RankImage.setImageDrawable(context.getDrawable(R.drawable.norank));

        RankNumber.setText(String.valueOf(position + 1 )); // position 즉 순서
        RankScore.setText(list.get(position).total);
        RankName.setText(list.get(position).name);


       return convertView;
    }
}
