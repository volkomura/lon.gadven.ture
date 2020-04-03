package lon.gadven.ture;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;

public class GridAdapter extends BaseAdapter {
    enum Status {CELL_NORMAL, CELL_ROTATED_RIGHT_90, CELL_ROTATED_RIGHT_180, CELL_ROTATED_RIGHT_270}

    private Context mContext;
    private Integer mCols, mRows;
    private ArrayList<String> arrPict;
    private ArrayList<Status> arrStatus;
    private String picturePrefecs;
    private Resources mRes;


    public GridAdapter(Context mContext, Integer mCols, Integer mRows) {
        this.mContext = mContext;
        this.mCols = mCols;
        this.mRows = mRows;
        arrPict = new ArrayList<>();
        arrStatus = new ArrayList<>();
        picturePrefecs = "puzzles_";
        mRes = mContext.getResources();
        makePuzzleArray();
        makePuzzleRotate();

    }

    private void makePuzzleRotate() {
        arrStatus.clear();
        for (int i = 0; i < 2; i++) {
            arrStatus.add(Status.CELL_NORMAL);
            arrStatus.add(Status.CELL_ROTATED_RIGHT_90);
            arrStatus.add(Status.CELL_ROTATED_RIGHT_180);
            arrStatus.add(Status.CELL_ROTATED_RIGHT_270);
        }
        arrStatus.add(Status.CELL_NORMAL);
        Collections.shuffle(arrStatus);

    }

    private void makePuzzleArray() {
        arrPict.clear();
        for (int i = 1; i <= (mCols * mRows); i++) {
            arrPict.add(picturePrefecs + i);
        }

    }

    public void rotateCell(int position) {
        if (arrStatus.get(position) == Status.CELL_ROTATED_RIGHT_90) {
            arrStatus.set(position, Status.CELL_ROTATED_RIGHT_180);
        } else if (arrStatus.get(position) == Status.CELL_ROTATED_RIGHT_180) {
            arrStatus.set(position, Status.CELL_ROTATED_RIGHT_270);
        } else if (arrStatus.get(position) == Status.CELL_ROTATED_RIGHT_270) {
            arrStatus.set(position, Status.CELL_NORMAL);
        } else if (arrStatus.get(position) == Status.CELL_NORMAL) {
            arrStatus.set(position, Status.CELL_ROTATED_RIGHT_90);
        }

        notifyDataSetChanged();
    }
    public boolean checkGameWin() {
        int indexWin = 0;
        for (int i = 0; i < arrStatus.size(); i++){
            if(arrStatus.get(i) == Status.CELL_NORMAL){
                ++indexWin;
            }
        }
        return indexWin == 9;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        int drawableId;
        if (view == null) {
            imageView = new ImageView(mContext);
        } else
            imageView = (ImageView) view;
        switch (arrStatus.get(i)) {
            case CELL_NORMAL:
                drawableId = mRes.getIdentifier(arrPict.get(i), "drawable", mContext.getPackageName());
                imageView.setImageResource(drawableId);
                imageView.setRotation(0);
                break;
            case CELL_ROTATED_RIGHT_90:
                drawableId = mRes.getIdentifier(arrPict.get(i), "drawable", mContext.getPackageName());
                imageView.setImageResource(drawableId);
                imageView.setRotation(90);
                break;
            case CELL_ROTATED_RIGHT_180:
                drawableId = mRes.getIdentifier(arrPict.get(i), "drawable", mContext.getPackageName());
                imageView.setImageResource(drawableId);
                imageView.setRotation(180);
                break;
            case CELL_ROTATED_RIGHT_270:
                drawableId = mRes.getIdentifier(arrPict.get(i), "drawable", mContext.getPackageName());
                imageView.setImageResource(drawableId);
                imageView.setRotation(270);
                break;

        }
        return imageView;
    }

    @Override
    public int getCount() {
        return mCols * mRows;
    }

    @Override
    public Object getItem(int position) {
        return arrPict.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}

