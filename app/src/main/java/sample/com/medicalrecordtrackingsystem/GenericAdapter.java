package sample.com.medicalrecordtrackingsystem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

import sample.com.medicalrecordtrackingsystem.viewholders.BaseViewHolder;
import sample.com.medicalrecordtrackingsystem.viewholders.SlotsViewHolder;

/**
 * Created by ayush on 1/4/17
 */
public class GenericAdapter extends RecyclerView.Adapter<BaseViewHolder<Items>> {

    private Context context;
    private List<Items> itemsList;

    public GenericAdapter(Context context, List<Items> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public BaseViewHolder<Items> onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder<Items> viewHolder;
        switch (viewType) {
            case Items.TYPE_SLOTS:
                viewHolder = new SlotsViewHolder(parent);
                break;
            default:
                viewHolder = new SlotsViewHolder(parent);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<Items> holder, int position) {
        Items item = itemsList.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public void setData(List<Items> items) {
        this.itemsList = items;
    }
}
