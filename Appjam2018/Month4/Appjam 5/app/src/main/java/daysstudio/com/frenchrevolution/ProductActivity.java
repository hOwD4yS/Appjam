package daysstudio.com.frenchrevolution;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import static daysstudio.com.frenchrevolution.MainActivity.EditUserDefault;
import static daysstudio.com.frenchrevolution.MainActivity.UserDefault;
import static daysstudio.com.frenchrevolution.MainActivity.contacts;


public class ProductActivity extends Activity {

    TextView price_p;
    ImageView img_p;
    TextView info_p;
    Button btn_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);
        Intent intent = getIntent();
        boolean buy = false;
        final int num = intent.getIntExtra("num",0);

        price_p = findViewById(R.id.product_price);
        img_p = findViewById(R.id.product_img_p);
        info_p = findViewById(R.id.product_info);
        btn_p = findViewById(R.id.product_btn);

        price_p.setText("가격 : "+contacts.get(num).price);
        img_p.setImageDrawable(contacts.get(num).img);
        info_p.setText(contacts.get(num).info);
        btn_p.setOnClickListener(
                new OnClickListener() {
                    public void onClick(View v) {
                        if(UserDefault.getInt("current",0)>=contacts.get(num).price) {
                            img_p.setImageDrawable(contacts.get(num).ba);
                            EditUserDefault.putInt("current", UserDefault.getInt("current", 0) - contacts.get(num).price);
                            EditUserDefault.apply();
                            btn_p.setText("구매후");
                            btn_p.setClickable(false);
                        }
                        else{
                            Toast.makeText(ProductActivity.this, "현재 포인트가 부족합니다!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
        );

    }



}
