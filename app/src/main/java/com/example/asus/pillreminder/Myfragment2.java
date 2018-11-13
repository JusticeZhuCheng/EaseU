package com.example.asus.pillreminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by asus on 5/26/2016.
 */
public class Myfragment2 extends android.support.v4.app.Fragment{

    private Button addMedicine;
    private LinkedList<PillData> mData = null;
    private Context mContext;
    private pillsetlistAdapter mAdapter = null;
    private ListView list_one;
    private TextView txt_empty;
    public Myfragment2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mData = new LinkedList<>();
        View view = inflater.inflate(R.layout.fragment_setting, null);
        addMedicine = (Button)view.findViewById(R.id.button_newpill);
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PillSet.class);
                startActivity(intent);
            }
        });
        SharedHelper sp = new SharedHelper(getActivity());
        ArrayList<PillData> pillRecords = sp.read();
//        Log.i("pillData", sp.read().get(0).getPillName());


        list_one = (ListView) view.findViewById(R.id.list_one);
        mData = new LinkedList<PillData>();
        mData.addAll(pillRecords);
        mAdapter = new pillsetlistAdapter((LinkedList<PillData>) mData, mContext);
        list_one.setAdapter(mAdapter);
        txt_empty = (TextView) view.findViewById(R.id.txt_empty);
        list_one.setEmptyView(txt_empty);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("visible", "onResume");
        SharedHelper sp = new SharedHelper(getActivity());
        ArrayList<PillData> pillRecords = sp.read();
        mData.clear();
        mData.addAll(pillRecords);
        mAdapter = new pillsetlistAdapter((LinkedList<PillData>) mData, mContext);
        list_one.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("visible", "onStart");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("visible", "onAttach");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.i("visible", "setVisible");
            try{
                SharedHelper sp = new SharedHelper(getActivity());
                ArrayList<PillData> pillRecords = sp.read();
                mData.clear();
                mData.addAll(pillRecords);
                mAdapter = new pillsetlistAdapter((LinkedList<PillData>) mData, mContext);
                list_one.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            catch(Exception e){

            }
        }
    }
}
