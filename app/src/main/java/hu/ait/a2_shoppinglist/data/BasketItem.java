package hu.ait.a2_shoppinglist.data;

import hu.ait.a2_shoppinglist.R;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BasketItem extends RealmObject{
    public enum ItemType {
        FOOD(0, R.drawable.fruit_veggies),
        BOOK(1, R.drawable.book_flat),
        ELECTRONIC(2, R.drawable.microwave_flat),
        CLOTHES(3, R.drawable.t_shirt),
        TOILETRIES(4, R.drawable.toiletries);

        private int value;
        private int iconId;


        private ItemType(int value, int iconId) {
            this.value = value;
            this.iconId = iconId;
        }

        public int getValue() {
            return value;
        }

        public int getIconId() {
            return iconId;
        }

        public static ItemType fromInt(int value) {
            for (ItemType p : ItemType.values()) {
                if (p.value == value) {
                    return p;
                }
            }
            return FOOD;
        }

    }

    @PrimaryKey
    private String itemID;

    private String itemName;
    private String itemDesc;
    private int itemEstPrice;
    private boolean alreadyBought;
    private int itemType;

    public BasketItem() {}

    public BasketItem(String itemName, boolean alreadyBought, String itemDesc, int itemEstPrice, int itemType ){
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.alreadyBought = alreadyBought;
        this.itemEstPrice = itemEstPrice;
        this.itemType = itemType;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isAlreadyBought() {
        return alreadyBought;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
    public int getItemEstPrice() {
        return itemEstPrice;
    }

    public void setItemEstPrice(int itemEstPrice) {
        this.itemEstPrice = itemEstPrice;
    }

    public boolean isBought() {
        return alreadyBought;
    }

    public void toggleBoughtStatus(){
        if (alreadyBought){
            this.alreadyBought = false;
        } else {
            this.alreadyBought = true;
        }
    }

    public ItemType getItemType() {
        return ItemType.fromInt(itemType);
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
