package com.example.jackgu.light.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.example.jackgu.light.HelpActivity;
import com.example.jackgu.light.R;
import com.example.jackgu.light.view.TitleView;
//import com.example.jackgu.light.view.TitleView;
//import com.example.jackgu.light.view.TitleView.OnLeftButtonClickListener;
//import com.example.jackgu.light.view.TitleView.OnRightButtonClickListener;


public class SettingsFragment extends Fragment {
    private View mParent;

    private FragmentActivity mActivity;

    private TitleView mTitle;

    private TextView mText;

    /**
     * Create a new instance of DetailsFragment, initialized to show the text at 
     * 'index'. 
     */
    public static SettingsFragment newInstance(int index) {
        SettingsFragment f = new SettingsFragment();

        // Supply index input as an argument.  
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mParent = view;
        mTitle = (TitleView) mParent.findViewById(R.id.settings_title);
        mTitle.setTitle(R.string.title_settings);

        TextView tx = (TextView) mParent.findViewById(R.id.change_password_item);
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentIndicator.setIndicator(4);
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                getFragmentManager().beginTransaction().addToBackStack("resetPassword").replace(R.id.fragment_container, resetPasswordFragment, "resetPassword").commit();
            }
        });

        TextView question = (TextView) mParent.findViewById(R.id.question_item);
        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentIndicator.setIndicator(4);
                QuestionFragment questionFragment = new QuestionFragment();
                getFragmentManager().beginTransaction().addToBackStack("question").replace(R.id.fragment_container, questionFragment, "question").commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

//        mTitle.setLeftButton(R.string.exit, new OnLeftButtonClickListener(){
//
//            @Override
//            public void onClick(View button) {
//                mActivity.finish();
//            }
//
//        });
//        mTitle.setRightButton(R.string.help, new OnRightButtonClickListener() {
//
//            @Override
//            public void onClick(View button) {
//                goHelpActivity();
//            }
//        });

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
