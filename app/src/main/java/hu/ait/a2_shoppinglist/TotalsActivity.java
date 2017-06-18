package hu.ait.a2_shoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import hu.ait.a2_shoppinglist.data.BasketItem;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by MacOwner on 4/18/17.
 */

public class TotalsActivity extends AppCompatActivity {
    private TextView tvTotal;
    private EditText etTax;
    private RealmResults<BasketItem> allItems;
    private int sum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_totals);

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        //etTax = (EditText) findViewById(R.id.etTax);

        allItems = getRealm().where(BasketItem.class).findAll();

        for (BasketItem item: allItems){
            sum += item.getItemEstPrice();
        }

        tvTotal.setText("$"+sum);

    };

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }

}
