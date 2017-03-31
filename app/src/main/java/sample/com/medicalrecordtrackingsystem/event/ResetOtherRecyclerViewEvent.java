package sample.com.medicalrecordtrackingsystem.event;

/**
 * Created by ayush on 1/4/17
 */
public class ResetOtherRecyclerViewEvent {
    private int position;

    public ResetOtherRecyclerViewEvent(int position) {

        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
