package daysstudio.com.frenchrevolution;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import daysstudio.com.frenchrevolution.Bluetooth.BluetoothService;
import daysstudio.com.frenchrevolution.ListItem.Product;
import daysstudio.com.frenchrevolution.OtherFunction.SetDefaultUserInformation;
import daysstudio.com.frenchrevolution.ProductList.ListViewAdapter;
import daysstudio.com.frenchrevolution.Ranking.RankActivity;
import daysstudio.com.frenchrevolution.Server.GetMyRank;
import daysstudio.com.frenchrevolution.Server.UploadMyInformation;

public class MainActivity extends AppCompatActivity {
    private static Context context;

/*
UserDefault
{
    FIRST;
    UID;
    NICKNAME;
}
 */

    ImageView UserProfileImage;

    final int REQUEST_IMAGE = 9876;

    public static TextView UserProfileName , UserProfileTotalScore, UserProfileCurrentScore ;

    public static Button blue_btn;

    public static SharedPreferences UserDefault;
    public static SharedPreferences.Editor EditUserDefault;

    static RecyclerView recyclerView;
    static ListViewAdapter adapter;
    public static ArrayList<Product> contacts = new ArrayList<>();

    private static final String TAG = "Main";

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;


    ImageButton UserProfileRank;

    private BluetoothService btService = null;


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contacts = new ArrayList<>();
        UserDefault = getSharedPreferences("UserDefault" , 0);
        EditUserDefault = UserDefault.edit();
        UserProfileCurrentScore = findViewById(R.id.UserProfileCurrentScore);
        UserProfileTotalScore = findViewById(R.id.UserProfileTotalScore);

        UserProfileCurrentScore.setText("Current : "+UserDefault.getInt("current",0));
        UserProfileTotalScore.setText("Total: "+UserDefault.getInt("total",0));
        context = this;
        blue_btn = findViewById(R.id.BlueTooth);

        blue_btn.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        if(blue_btn.getText().equals("블루투스 연결 안됨")){
                            if(btService.getDeviceState()) {
                                btService.enableBluetooth();
                            } else {
                                finish();
                            }
                        }
                    }
                }
        );

        //파일 읽음 , 쓰기 권한
        verifyStoragePermissions(this);

        //view 기본설정
        SetViewID();

        //recyclerview 초기설정
        adapter = new ListViewAdapter(this, contacts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //recyclerview 아이템 추가
        ItemList();

        //Http통신 설정
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        //프로필 사진 설정
        UserProfileImage.setImageBitmap(GetProfileImage());


        // 처음 아닐때 이름 설정
        if(UserDefault.getString("name",null) != null)
            UserProfileName.setText(UserDefault.getString("name",null));
        else { // 처음일때 UID 생성  , POST
            new SetDefaultUserInformation(this);
            UserProfileName.setText(UserDefault.getString("name",null));
        }


        //이미지 바꾸기 설정
        UserProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeUserProfileImage();
            }
        });

        //이름 바꾸기
        UserProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeName();
            }
        });

        if(btService == null) {
            btService = new BluetoothService(this, mHandler);
        }

        UserProfileRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this , RankActivity.class));
            }
        });


        new UploadMyInformation(UserDefault.getString("name",null) , UserDefault.getString("UID",null) , String.valueOf(UserDefault.getInt("total",0)),context);

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        UserProfileCurrentScore.setText("Current : "+UserDefault.getInt("current",0));

    }

    public static void to_st(final String p0){

        Handler cHandler = new Handler(Looper.getMainLooper()) {
            @Override public void handleMessage(Message msg) {
                switch(p0){
                    case "연결 안됨":
                        Toast.makeText(context, p0, Toast.LENGTH_SHORT).show();
                        blue_btn.setText("블루투스 연결 안됨");
                        blue_btn.setBackgroundColor(Color.parseColor("#94ff0011"));
                        break;
                    case "연결 성공":
                        Toast.makeText(context, p0, Toast.LENGTH_SHORT).show();
                        blue_btn.setText("블루투스 연결 성공");
                        blue_btn.setBackgroundColor(Color.parseColor("#941bf522"));
                        break;


                }

            }
        };
        cHandler.sendEmptyMessage(0);


    }

    public static void plus(){

        Handler cHandler = new Handler(Looper.getMainLooper()) {
            @Override public void handleMessage(Message msg) {
                EditUserDefault.putInt("current",UserDefault.getInt("current",0)+10);
                EditUserDefault.putInt("total" , UserDefault.getInt("total",0)+10);
                EditUserDefault.apply();
                UserProfileCurrentScore.setText("Current : "+UserDefault.getInt("current",0));
                UserProfileTotalScore.setText("Total: "+UserDefault.getInt("total",0));
                new UploadMyInformation(UserDefault.getString("name",null) , UserDefault.getString("UID",null) , String.valueOf(UserDefault.getInt("total",0)),context);

            }
        };
        cHandler.sendEmptyMessage(0);


    }
    private void ChangeName() //이름 바꾸기
    {
        final EditText Name = new EditText(this);
        Name.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lp =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Name.setLayoutParams(lp);
        AlertDialog.Builder NameDialog = new AlertDialog.Builder(this);
        NameDialog.setView(Name);

        NameDialog.setTitle("변경할 닉네임을 입력해주세요.");

        NameDialog.setPositiveButton("변경", new DialogInterface.OnClickListener()  { // 변경
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(!TextUtils.isEmpty(Name.getText().toString())) { //빈칸 아닐시

                    //새롭게 업로드
                    new UploadMyInformation(Name.getText().toString(), UserDefault.getString("UID","0") , String.valueOf(UserDefault.getInt("total",0)) , MainActivity.this);

                    UserProfileName.setText(Name.getText().toString());
                    EditUserDefault.putString("name" , Name.getText().toString());

                    Toast.makeText(MainActivity.this, "완료", Toast.LENGTH_SHORT).show();
                    EditUserDefault.apply();
                }
                else
                    Toast.makeText(MainActivity.this,"입력해주세요",Toast.LENGTH_SHORT).show();

                dialogInterface.dismiss();
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() { //취소
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        NameDialog.show();
    }

    private void SetViewID() //View ID
    {
        recyclerView = findViewById(R.id.productlist);
        UserProfileImage = findViewById(R.id.UserProfileImage);
        UserProfileName = findViewById(R.id.UserProfileName);
        UserProfileTotalScore = findViewById(R.id.UserProfileTotalScore);
        UserProfileRank = findViewById(R.id.UserProfileRank);
    }


    private void ChangeUserProfileImage() //이미지 바꾸기
    {
        RectBitmap();
    }



    private void RectBitmap()
    {
        ImageCrop(500 , 500);
    }  // 정사각형



    private void ImageCrop(int RatioX , int RatioY)
    {
        Intent intent  = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra("crop",true);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX",RatioX);
        intent.putExtra("outputX",RatioY);
        intent.putExtra("noFaceDetection",true);
        intent.putExtra("return-data",true);
        startActivityForResult(intent , REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        OutputStream outStream;
        if(requestCode == REQUEST_IMAGE && resultCode  == Activity.RESULT_OK ) //ok
        {
            Bundle extras = data.getExtras();
            if(extras!=null)
            {
                Bitmap bitmap =(Bitmap)extras.getParcelable("data");
                UserProfileImage.setImageBitmap(bitmap);
                try {
                    outStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Profile.png");
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, outStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    btService.getDeviceInfo(data);
                }
                break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    btService.scanDevice();
                } else {
                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }


    private Bitmap GetProfileImage()
    {
        return BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Profile.png");
    }
    private void ItemList()
    {
        contacts.add(new Product(1,"메로나",getDrawable(R.drawable.product_1),"메론 맛 나는 네모난 아이스크림!",1,400,false , getDrawable(R.drawable.barcode_1)));
        contacts.add(new Product(2,"수박바",getDrawable(R.drawable.product_2),"수박 맛 나는 세모 아이스크림!",1,400,false ,getDrawable(R.drawable.barcode_2)));

        adapter.notifyDataSetChanged();
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
