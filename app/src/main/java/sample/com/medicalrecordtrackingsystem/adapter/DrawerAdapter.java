package sample.com.medicalrecordtrackingsystem.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import sample.com.medicalrecordtrackingsystem.R;

/**
 * Created by ayush on 28/3/17
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String[] navigationTitles;
    private String headerName;

    public DrawerAdapter(String[] navigationTitles, String headerName) {
        this.navigationTitles = navigationTitles;
        this.headerName = headerName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
            ViewHolder viewHolder = new ViewHolder(view, viewType);
            return viewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            ViewHolder viewHolder = new ViewHolder(view, viewType);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            holder.itemName.setText(navigationTitles[position - 1]);
        } else {
            holder.profileName.setText(headerName);
        }
    }

    @Override
    public int getItemCount() {
        return navigationTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView profileName;
        TextView itemName;
        View divider;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_ITEM) {
                itemName = (TextView) itemView.findViewById(R.id.item_name);
                divider = itemView.findViewById(R.id.divider);
            } else {
                profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
                profileName = (TextView) itemView.findViewById(R.id.profile_name);
            }
        }
    }
}
