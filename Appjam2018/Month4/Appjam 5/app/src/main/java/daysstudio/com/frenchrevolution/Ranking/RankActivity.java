package daysstudio.com.frenchrevolution.Ranking;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import daysstudio.com.frenchrevolution.R;
import daysstudio.com.frenchrevolution.Server.GetAllRank;
import daysstudio.com.frenchrevolution.Server.GetMyRank;

import static daysstudio.com.frenchrevolution.MainActivity.UserDefault;

/**
 * Created by JinHunLee on 2018. 4. 1..
 */

public class RankActivity extends AppCompatActivity{

    ArrayList<RankStructure>  Ranking = new ArrayList<>();
    RankingListAdapter rankingListAdapter;
    ListView RankList;

   // public static SharedPreferences UserDefault;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        RankList = (ListView)findViewById(R.id.ranking);
        Ranking = new GetAllRank().GetRank(this);
        rankingListAdapter = new RankingListAdapter(this , Ranking);
        RankList.setAdapter(rankingListAdapter);
        rankingListAdapter.notifyDataSetChanged();
        try {
            RankList.smoothScrollToPosition(Integer.valueOf(new GetMyRank().GetRank(UserDefault.getString("UID", "0")))+4);
        } catch (Exception e)
        {
            RankList.smoothScrollToPosition(Integer.valueOf(new GetMyRank().GetRank(UserDefault.getString("UID", "0"))));

        }

    }

}
