package com.nick.mvvm.freme;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nick.mvvm.eventbus.IMessageEvent;

import java.util.ArrayList;

public abstract class BaseFragment extends Fragment implements IBindBase {
    private DelegateFragment mDelegate = new DelegateFragment(this);

    private Dialog mWaitDialog;

    public BaseFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDelegate.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mDelegate.onDestroy();
        super.onDestroy();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(getContentLayoutResId(), container, false);
//    }


    public IMessageEvent onGlobalEvent() {
        return null;
    }

    public IMessageEvent onMessageEvent() {
        return null;
    }

    // 无效
    @Override
    public final ArrayList<View> initInterceptFocusViews() {
        return null;
    }


}
