package sample.com.medicalrecordtrackingsystem.viewholders;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import sample.com.medicalrecordtrackingsystem.Items;
import sample.com.medicalrecordtrackingsystem.R;
import sample.com.medicalrecordtrackingsystem.SlotsAdapter;
import sample.com.medicalrecordtrackingsystem.event.ResetOtherRecyclerViewEvent;
import sample.com.medicalrecordtrackingsystem.models.Slots;
import sample.com.medicalrecordtrackingsystem.utility.ItemClickSupport;

/**
 * Created by ayush on 1/4/17
 */
public class SlotsViewHolder extends BaseViewHolder<Items> {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.day)
    TextView day;

    private SlotsAdapter mAdapter;
    private static int lastClickId = -1;

    public SlotsViewHolder(ViewGroup parent) {
        super(parent, R.layout.slot_layout);
        ButterKnife.bind(this, itemView);

        final StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(itemView.getLayoutParams());
        params.setFullSpan(true);
        itemView.setLayoutParams(params);
        LinearLayoutManager layoutManager = new LinearLayoutManager(parent.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new SlotsAdapter(parent.getContext(), new Slots());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setData(Items item) {
        final Slots slot = item.slot;
        for (int i = 0; i < slot.getSlots().length; i++) {
            if (slot.getSlots()[i] == 1) {
                day.setText(slot.getDay());
                break;
            }
        }
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (lastClickId != slot.getId()) {
                    EventBus.getDefault().post(new ResetOtherRecyclerViewEvent(lastClickId));
                }
                mAdapter.setChecked(position, slot.getId());
                lastClickId = slot.getId();
            }
        });
        mAdapter.setData(slot);
    }
}
