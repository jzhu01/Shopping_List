package hu.ait.a2_shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

import hu.ait.a2_shoppinglist.data.BasketItem;
import io.realm.Realm;

public class CreateItemActivity extends AppCompatActivity {
    public static final String KEY_ITEM = "KEY_ITEM";
    private Spinner spinnerItemType;
    private EditText etItem;
    private EditText etItemDesc;
    private EditText etItemCost;
    private CheckBox cbItemPurchased;
    private BasketItem itemToEdit = null;

    private CoordinatorLayout layoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        setupUI();

        if (getIntent().getSerializableExtra(MainActivity.KEY_EDIT) != null) {
            initEdit();
        } else {
            initCreate();
        }
    }

    private void initCreate() {
        getRealm().beginTransaction();
        itemToEdit = getRealm().createObject(BasketItem.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }

    private void initEdit() {
        String itemID = getIntent().getStringExtra(MainActivity.KEY_EDIT);
        itemToEdit = getRealm().where(BasketItem.class)
                .equalTo("itemID", itemID)
                .findFirst();

        etItem.setText(itemToEdit.getItemName());
        etItemDesc.setText(itemToEdit.getItemDesc());
        etItemCost.setText(itemToEdit.getItemEstPrice());
        cbItemPurchased.setChecked(itemToEdit.isAlreadyBought());
        spinnerItemType.setSelection(itemToEdit.getItemType().getValue());
    }

    private void setupUI() {
        spinnerItemType = (Spinner) findViewById(R.id.spinnerItemType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.basketItem_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerItemType.setAdapter(adapter);

        etItem = (EditText) findViewById(R.id.etItemName);
        etItemDesc = (EditText) findViewById(R.id.etItemDesc);
        etItemCost = (EditText) findViewById(R.id.etItemCost);
        layoutContent = (CoordinatorLayout) findViewById(R.id.layoutContent);

        cbItemPurchased = (CheckBox) findViewById(R.id.is_purchased);
        cbItemPurchased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbItemPurchased.isChecked()){
                    cbItemPurchased.setChecked(true);
                } else {
                    cbItemPurchased.setChecked(false);
                }
                getRealm().beginTransaction();
                itemToEdit.toggleBoughtStatus();
                getRealm().commitTransaction();
            }
        });

        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });

        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public Realm getRealm() {
        return ((MainApplication)getApplication()).getRealmItems();
    }

    private void saveItem() {
        String itemName = etItem.getText().toString();
        String itemCost = etItemCost.getText().toString();
        if (itemName.equalsIgnoreCase("") || itemName.equalsIgnoreCase(" ")
                || itemCost.equalsIgnoreCase("") || itemCost.equalsIgnoreCase(" ")){
            finish();
        } else {
            Intent intentResult = new Intent();

            getRealm().beginTransaction();
            itemToEdit.setItemName(etItem.getText().toString());
            itemToEdit.setItemDesc(etItemDesc.getText().toString());
            itemToEdit.setItemEstPrice(Integer.parseInt(etItemCost.getText().toString()));
            itemToEdit.setItemType(spinnerItemType.getSelectedItemPosition());
            getRealm().commitTransaction();

            intentResult.putExtra(KEY_ITEM, itemToEdit.getItemID());
            setResult(RESULT_OK, intentResult);
            super.onBackPressed();
        }
    }
}
