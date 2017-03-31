package sample.com.medicalrecordtrackingsystem;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import sample.com.medicalrecordtrackingsystem.models.Slots;

/**
 * Created by ayush on 1/4/17
 */
public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.ViewHolder>{

    private Context context;
    private Slots slots;
    private String[] timeUnit;
    private int checkedPosition;
    private int currentId;
    private int lastClickId;

    public SlotsAdapter(Context context, Slots slots) {
        this.context = context;
        this.slots = slots;
        timeUnit = context.getResources().getStringArray(R.array.time_slot_array);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (slots.getSlots()[position] == 1) {
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(timeUnit[position]);
        } else {
            holder.time.setVisibility(View.GONE);
        }

        if (position == checkedPosition && slots.getId() == currentId) {
            GradientDrawable drawable = (GradientDrawable) holder.time.getBackground();
            drawable.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.time.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            GradientDrawable drawable = (GradientDrawable) holder.time.getBackground();
            drawable.setColor(ContextCompat.getColor(context, R.color.grey300));
            holder.time.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return slots.getSlots().length;
    }

    public void setData(Slots slot) {
        this.slots = slot;
        notifyDataSetChanged();
    }

    public void setChecked(int position, int id) {
        this.checkedPosition = position;
        this.currentId = id;
        notifyDataSetChanged();
    }

    public void setUnchecked(int lastClickId) {
        this.lastClickId = lastClickId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.time)
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
