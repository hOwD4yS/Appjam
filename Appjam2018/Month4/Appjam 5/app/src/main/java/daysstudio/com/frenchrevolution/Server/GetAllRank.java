package daysstudio.com.frenchrevolution.Server;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import daysstudio.com.frenchrevolution.Ranking.RankStructure;

/**
 * Created by JinHunLee on 2018. 3. 31..
 */

public class GetAllRank {

    ArrayList<RankStructure> Rank = new ArrayList<>();

    public ArrayList<RankStructure> GetRank(Context context) {
        try {

            URL url = new URL("http://ngdb.kr:8080/api/get/all");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            //READ
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            StringBuilder rank_json = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null)
                rank_json.append(temp + "\n");


            JSONArray jsonArray = new JSONArray(rank_json.toString());

            for (int i = 0; i < jsonArray.length(); i++) {
               // Toast.makeText(context,jsonArray.getJSONObject(i).getString("name"), Toast.LENGTH_SHORT).show();
                JSONObject OneOfRank = jsonArray.getJSONObject(i);
                String name = OneOfRank.getString("name");
                String total = OneOfRank.getString("total");
                Rank.add(new RankStructure(name, total));
            }

            return Rank;

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(context,"A", Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,"B", Toast.LENGTH_SHORT).show();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context,"C", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
