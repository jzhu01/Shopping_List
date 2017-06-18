package hu.ait.a2_shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import hu.ait.a2_shoppinglist.MainActivity;
import hu.ait.a2_shoppinglist.MainApplication;
import hu.ait.a2_shoppinglist.R;
import hu.ait.a2_shoppinglist.data.BasketItem;
import io.realm.Realm;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public TextView tvItem;
        public TextView tvCost;
        public CheckBox cbPurchased;
        public Button btnDelete;
        public Button btnView;

        public ViewHolder(View itemView) {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            tvItem = (TextView) itemView.findViewById(R.id.tvItem);
            tvCost = (TextView) itemView.findViewById(R.id.tvCost);
            cbPurchased = (CheckBox) itemView.findViewById(R.id.is_purchased);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnView = (Button) itemView.findViewById(R.id.btnView);
//            mainLayout = (ViewGroup) itemView.findViewById(R.id.recyclerViewItems);
        }
    }

    private List<BasketItem> basketItemList;
    private Context context;
    private int lastPosition = -1;

    public ItemAdapter(List<BasketItem> basketItemList, Context context) {
        this.basketItemList = basketItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public Realm getRealm() {
        return ((MainApplication)context.getApplicationContext()).getRealmItems();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if (basketItemList.get(position).isValid()){
            viewHolder.tvItem.setText(basketItemList.get(position).getItemName());
            viewHolder.tvCost.setText("$"+basketItemList.get(position).getItemEstPrice());
            viewHolder.ivIcon.setImageResource(
                    basketItemList.get(position).getItemType().getIconId());
            viewHolder.cbPurchased.setChecked(basketItemList.get(position).isBought());
            viewHolder.cbPurchased.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (viewHolder.cbPurchased.isChecked()){
                        viewHolder.cbPurchased.setChecked(true);
                    } else {
                        viewHolder.cbPurchased.setChecked(false);
                    }

                    getRealm().beginTransaction();
                    basketItemList.get(position).toggleBoughtStatus();
                    getRealm().commitTransaction();
                }
            });
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(viewHolder.getAdapterPosition());
                }
            });
            viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) context).showEditItemActivity(
                            basketItemList.get(viewHolder.getAdapterPosition()).getItemID(),
                            viewHolder.getAdapterPosition());
                }
            });

            setAnimation(viewHolder.itemView, position);
        }
    }

    @Override
    public int getItemCount() {
        return basketItemList.size();
    }

    public void addItem(BasketItem item) {
        basketItemList.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int index, BasketItem item) {
        basketItemList.set(index, item);

        notifyItemChanged(index);

    }

    public void removeItem(int index) {
        ((MainActivity)context).deleteItem(basketItemList.get(index));
        basketItemList.remove(index);
        notifyItemRemoved(index);
    }

    public void swapPlaces(int oldPosition, int newPosition) {
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(basketItemList, i, i + 1);
            }
        } else {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(basketItemList, i, i - 1);
            }
        }
        notifyItemMoved(oldPosition, newPosition);
    }

    public BasketItem getItem(int i) {
        return basketItemList.get(i);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
