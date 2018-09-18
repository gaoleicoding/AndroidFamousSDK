package com.gaolei.dagger_two;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaolei.dagger2.R;


public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mParentView = inflater.inflate(R.layout.fragment_main, container, false);

        return mParentView;
    }
}