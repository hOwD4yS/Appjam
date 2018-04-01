package daysstudio.com.frenchrevolution.ListItem;

import android.graphics.drawable.Drawable;

public class Product {
    public int id;
    public String name;
    public Drawable img;
    public String info;
    public int type;
    public int price;
    public boolean buy;
    public Drawable ba;


    public Product(int id,String name, Drawable img ,String info,int type,int price,boolean buy , Drawable ba) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.info = info;
        this.type = type;
        this.price = price;
        this.buy = buy;
        this.ba = ba;
    }

}
