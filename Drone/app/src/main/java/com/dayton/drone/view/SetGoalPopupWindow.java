package com.dayton.drone.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.bruce.pickerview.LoopListener;
import com.bruce.pickerview.LoopView;
import com.dayton.drone.R;
import com.dayton.drone.event.StepsGoalChangedEvent;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by med on 17/5/11.
 */

public class SetGoalPopupWindow extends PopupWindow{

    private Context context;
    private int stepsGoal;
    private View contentView;
    EditText stepGoalEdit;
    Button   confirm;
    Button   cancel;
    LoopView goalLoopView;

    public SetGoalPopupWindow(final Context context) {
        this.context = context;
        contentView = LayoutInflater.from(context).inflate(
                R.layout.set_goal_popup_window_layout, null);

        stepGoalEdit = (EditText) contentView.findViewById(R.id.set_goal_edit);
        goalLoopView = (LoopView) contentView.findViewById(R.id.goal_loopview);
        confirm = (Button) contentView.findViewById(R.id.popup_window_confirm);
        cancel  = (Button) contentView.findViewById(R.id.popup_window_cancel);

        stepsGoal = SpUtils.getIntMethod(context, CacheConstants.GOAL_STEP,10000);
        stepGoalEdit.setText(stepsGoal+"");
        goalLoopView.setNotLoop();
        final String[] goals = context.getResources().getStringArray(R.array.steps_goal_array);
        goalLoopView.setArrayList(new ArrayList<String>(Arrays.asList(goals)));
        goalLoopView.setInitPosition(1);
        goalLoopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                stepsGoal = Integer.valueOf(goals[item]);
            }
        });

        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        stepGoalEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String goal = stepGoalEdit.getText().toString();
                if (!TextUtils.isEmpty(goal)) {
                    stepsGoal = Integer.valueOf(goal);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtils.putIntMethod(context, CacheConstants.GOAL_STEP, stepsGoal);
                EventBus.getDefault().post(new StepsGoalChangedEvent(stepsGoal));
                close();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });


    }


    public void show(Activity activity) {

        if (null != activity) {
            setFocusable(true);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);
            trans.setDuration(300);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());
            contentView.startAnimation(trans);
        }
    }

    public void close() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(300);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });
        contentView.startAnimation(trans);
    }



}
