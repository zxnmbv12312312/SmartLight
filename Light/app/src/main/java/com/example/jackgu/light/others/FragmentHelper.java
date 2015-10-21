package com.example.jackgu.light.others;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import com.example.jackgu.light.R;
import com.example.jackgu.light.fragment.CustomModeFragment;

import java.util.List;

/**
 * Created by jackgu on 6/4/2015.
 */
public class FragmentHelper {
    public static void ShowFragemnt(FragmentManager manager, String fragmentName) {
        List<Fragment> fragments = manager.getFragments();
        FragmentTransaction transaction = manager.beginTransaction();
        boolean isexist = false;
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
            if (fragment.getClass().toString().contains(fragmentName)) {
                transaction.show(fragment);
                isexist = true;
            }
        }
        if (!isexist) {
            try {
                Class c = Class.forName(fragmentName);
                Fragment newfragment = (Fragment) c.newInstance();
                transaction.add(newfragment, null);
                transaction.show(newfragment);
            } catch (Exception ex) {

            }
        }
        transaction.commit();
    }

    public static void HideAllFragemnt(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
        transaction.commit();
    }

    public static void RemoveAllFragemnt(FragmentManager manager) {
        List<Fragment> fragments = manager.getFragments();
        FragmentTransaction transaction = manager.beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.remove(fragment);
        }
        transaction.commit();
    }

    public static void RecreateFragemnt(FragmentManager manager, String fragmentName, int containerId, String tag ) {
        List<Fragment> fragments = manager.getFragments();
        FragmentTransaction transaction = manager.beginTransaction();
        boolean isexist = false;
        try {
            for (Fragment fragment : fragments) {
                transaction.hide(fragment);
                if (fragment.getClass().toString().contains(fragmentName)) {
                    transaction.remove(fragment);
                    Class c = Class.forName(fragmentName);
                    Fragment newfragment = (Fragment) c.newInstance();
                    transaction.add(containerId, newfragment, tag);
                    isexist = true;
                    transaction.show(newfragment);
                }
            }
            if (!isexist) {
                Class c = Class.forName(fragmentName);
                Fragment newfragment = (Fragment) c.newInstance();
                transaction.add(containerId, newfragment, tag);
                transaction.show(newfragment);
            }
            transaction.commit();
        } catch (Exception ex) {

        }
    }

    public static Fragment GetFragemnt(FragmentManager manager, String fragmentName) {
        List<Fragment> fragments = manager.getFragments();
        try {
            for (Fragment fragment : fragments) {

                if (fragment.getClass().toString().contains(fragmentName)) {
                    return fragment;
                }
            }
        } catch (Exception ex) {

        }
        return null;
    }

}
