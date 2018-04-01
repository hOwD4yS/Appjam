package daysstudio.com.frenchrevolution.Server;

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

/**
 * Created by JinHunLee on 2018. 3. 31..
 */

public class GetMyRank {

    public String GetRank(String UID)
    {
        try {

            URL url = new URL("http://ngdb.kr:8080/api/posts/search");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDefaultUseCaches(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            //POST
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            PrintWriter printWriter = new PrintWriter(outputStreamWriter);
            printWriter.write("UID="+UID);
            printWriter.flush();

            //READ
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream() , "UTF-8"));
            StringBuilder rank_json = new StringBuilder();
            String temp;
            while((temp = bufferedReader.readLine()) != null)
                rank_json.append(temp + "\n");

            String rank =  new JSONObject(rank_json.toString()).getString("index").replace(" ","").replace("\n" ,"");

            return rank;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "A";
        } catch (IOException e) {
            e.printStackTrace();
            return "B";
        } catch (JSONException e) {
            e.printStackTrace();
            return "C";
        }

    }
}
