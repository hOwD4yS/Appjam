package daysstudio.com.frenchrevolution.OtherFunction;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.Random;

import daysstudio.com.frenchrevolution.Server.UploadMyInformation;

/**
 * Created by JinHunLee on 2018. 3. 31..
 */

public class SetDefaultUserInformation {
    SharedPreferences UserDefault;

    public  SetDefaultUserInformation(Context context )
    {
        UserDefault = context.getSharedPreferences("UserDefault",0);

        if( UserDefault.getInt("First" , 0) == 0 ) // 0 == 처음 시작 1 == 실행한 적 있음
        {
            SharedPreferences.Editor UserDefault_Editor = UserDefault.edit();

            char[] RandomTable = {'a','b','c','d','e','f','g','h','i','j','k','l','n','m','o','p','q','r','s','t','u','v', 'w','x' , 'y' ,'z' , '0' , '1','2','3','4','5','6','7','8','9'};

            StringBuffer UID = new StringBuffer();
            Random UID_RANDOM = new Random();
            for(int i=0; i < 20; i++) //랜덤 uid 생성 10 자리
                UID.append(RandomTable[UID_RANDOM.nextInt(RandomTable.length)]); //랜덤 테이블이용해서 랜덤으로 문자열 생성

            //Name == PlayerXXXX
            char[] NameRandomTable = {'0' , '1','2','3','4','5','6','7','8','9'};
            Random Name_RANDOM = new Random();
            StringBuffer Name = new StringBuffer();
            Name.append("Player");
            for(int i=0; i < 4; i++)
                Name.append(NameRandomTable[Name_RANDOM.nextInt(NameRandomTable.length)]);

           // Toast.makeText(context, UID.toString(), Toast.LENGTH_SHORT).show();

            UserDefault_Editor.putInt("First" , 1);
            UserDefault_Editor.putString("UID",UID.toString());
            UserDefault_Editor.putString("name" , Name.toString());
            UserDefault_Editor.putInt("current",0);
            UserDefault_Editor.putInt("total" , 0);
            UserDefault_Editor.apply(); //완료

            new UploadMyInformation(Name.toString() , UID.toString() , "0" ,context); //UPLOADINFORMATION
        }
    }
}
