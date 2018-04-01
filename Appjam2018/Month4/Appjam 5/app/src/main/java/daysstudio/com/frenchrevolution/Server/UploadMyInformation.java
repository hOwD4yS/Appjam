package daysstudio.com.frenchrevolution.Server;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by JinHunLee on 2018. 3. 31..
 */

public class UploadMyInformation {

    String name , UID , total;
    Context context;
    StringBuffer stringBuffer = new StringBuffer();
    StringBuilder builder = new StringBuilder();

    public UploadMyInformation(String name , String UID , String total , Context context)
    {
        this.name = name;
        this.UID = UID;
        this.total = total;
        this.context = context;
        Request();
    }

    private void Request() //POST로 값 갱신
    {
        Boolean RETURN = false;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {


                stringBuffer.append("name").append("=").append(name).append("&");
                stringBuffer.append("UID").append("=").append(UID).append("&");
                stringBuffer.append("total").append("=").append(total);

                try {

                    URL url = new URL("http://ngdb.kr:8080/api/posts/put");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDefaultUseCaches(true);
                    httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
                    PrintWriter printWriter = new PrintWriter(outputStreamWriter);
                    printWriter.write(stringBuffer.toString());
                    printWriter.flush();

                    httpURLConnection.getInputStream();
                    //POST 완료

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t.start();
        try {
            t.join();
            //Toast.makeText(context, builder.toString(), Toast.LENGTH_SHORT).show();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
