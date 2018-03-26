package com.example.andyshi.smartmeter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AndyShi on 2017/12/6.
 */

public class MeterAdapter extends ArrayAdapter<String> {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    Activity mActivity;
    public MeterAdapter(@NonNull Context context, String[] resource) {
        super(context, R.layout.item_row, resource);
        this.mActivity = (Activity) context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View meterView = inflater.inflate(R.layout.item_row, parent, false);
        String meterData = (String)getItem(position);
        TextView status = (TextView)meterView.findViewById(R.id.tv_status);
        TextView name = (TextView)meterView.findViewById(R.id.tv_metername);
        TextView current = (TextView)meterView.findViewById(R.id.tv_current);
        Button history = (Button)meterView.findViewById(R.id.btn_history);
        pref = getContext().getSharedPreferences("id",0);
        editor = pref.edit();
        final String[] splitDatas = meterData.split(":");
        name.setText(splitDatas[0]);
        current.setText(splitDatas[1]);
        //status.setText(splitDatas[2]);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),splitDatas[0],Toast.LENGTH_SHORT).show();
                editor.putString("device",splitDatas[0]);
                editor.commit();
                Intent it = new Intent(getContext(),HistroyDataActivity.class);
                getContext().startActivity(it);

            }
        });

        return meterView;
    }
}
