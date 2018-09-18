package com.barisetech.www.workmanage.view.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.barisetech.www.workmanage.R;
import com.barisetech.www.workmanage.adapter.ItemCallBack;
import com.barisetech.www.workmanage.adapter.OnScrollListener;
import com.barisetech.www.workmanage.adapter.PlanListAdapter;
import com.barisetech.www.workmanage.base.BaseConstant;
import com.barisetech.www.workmanage.base.BaseFragment;
import com.barisetech.www.workmanage.base.BaseLoadMoreWrapper;
import com.barisetech.www.workmanage.bean.EventBusMessage;
import com.barisetech.www.workmanage.bean.ToolbarInfo;
import com.barisetech.www.workmanage.bean.incident.IncidentInfo;
import com.barisetech.www.workmanage.bean.incident.ReqAllIncident;
import com.barisetech.www.workmanage.bean.incident.ReqIncidentSelectItem;
import com.barisetech.www.workmanage.bean.pipe.ReqAllPipe;
import com.barisetech.www.workmanage.bean.workplan.PlanBean;
import com.barisetech.www.workmanage.bean.workplan.ReqAllPlan;
import com.barisetech.www.workmanage.bean.workplan.ReqPlanNum;
import com.barisetech.www.workmanage.databinding.FragmentPlanListBinding;
import com.barisetech.www.workmanage.utils.DisplayUtil;
import com.barisetech.www.workmanage.utils.LogUtil;
import com.barisetech.www.workmanage.utils.SharedPreferencesUtil;
import com.barisetech.www.workmanage.utils.TimeUtil;
import com.barisetech.www.workmanage.utils.ToastUtil;
import com.barisetech.www.workmanage.viewmodel.PlanViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class PlanListFragment extends BaseFragment {
    public static final String TAG = "PlanListFragment";

    FragmentPlanListBinding mBinding;
    private List<PlanBean> planBeanList;

    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private TimePickerDialog startTimePicker;
    private TimePickerDialog endTimePicker;

    private String startTime = "";
    private String endTime = "";
    private Disposable curDisposable;

    //每次加载个数
    private static final int PAGE_COUNT = 10;
    private int maxNum;
    private int curType = BaseConstant.TYPE_INCIDENT_ALL;
    private int selectType;//选择的类型
    private PlanListAdapter planListAdapter;
    private BaseLoadMoreWrapper loadMoreWrapper;
    private PlanViewModel planViewModel;
    private String role;
    private Disposable numDisposable;


    public PlanListFragment() {
        // Required empty public constructor
    }

    public static PlanListFragment newInstance() {
        PlanListFragment fragment = new PlanListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planBeanList = new ArrayList<>();
        role = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ROLE, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_plan_list, container, false);
        setToolBarHeight(mBinding.toolbar.getRoot());
        mBinding.setFragment(this);
        ToolbarInfo toolbarInfo = new ToolbarInfo();
        toolbarInfo.setTitle(getString(R.string.title_plan_list));
        toolbarInfo.setOneText(getString(R.string.plan_list_publish));
        observableToolbar.set(toolbarInfo);

        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        startDatePicker = TimeUtil.getDatePicker(getActivity(), onStartDateSetListener);
        endDatePicker = TimeUtil.getDatePicker(getActivity(), onEndDateSetListener);
        startTimePicker = TimeUtil.getTimePicker(getActivity(), onStartTimeSetListener);
        endTimePicker = TimeUtil.getTimePicker(getActivity(), onEndTimeSetListener);

        initRecyclerView();

        mBinding.planListStartTime.setOnClickListener(view -> {
            //显示开始日期选择器
            startDatePicker.show();
        });
        mBinding.planListEndTime.setOnClickListener(view -> {
            //显示结束日期选择器
            endDatePicker.show();
        });

        mBinding.planListUncompleted.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_PLAN_UNCOMPLETED;
            setDateTime();
        });
        mBinding.planListCompleted.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_PLAN_COMPLETED;
            setDateTime();
        });
        mBinding.planListTimeout.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_PLAN_TIMEOUT;
            setDateTime();
        });
        mBinding.planListExcess.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_PLAN_EXCESS;
            setDateTime();
        });
        mBinding.planListAll.setOnClickListener(view -> {
            RadioButton radioButton = (RadioButton) view;
            cleanFilterGruop();
            radioButton.setChecked(true);
            selectType = BaseConstant.TYPE_PLAN_ALL;
            setDateTime();
        });

        mBinding.planListConfirm.setOnClickListener(view -> {
            if (TextUtils.isEmpty(startTime)) {
                ToastUtil.showToast("请选择开始时间");
                return;
            }
            if (TextUtils.isEmpty(endTime)) {
                ToastUtil.showToast("请选择结束时间");
                return;
            }
            closeDisposable();
            transFilterLayout();
            curType = selectType;
            planBeanList.clear();
//            if (maxNum >= PAGE_COUNT) {
//                getDatas(0, PAGE_COUNT);
//            } else {
//                getDatas(0, maxNum);
//            }
            getListNums();
        });
    }

    private void closeDisposable() {
        if (curDisposable != null) {
            curDisposable.dispose();
        }
        if (numDisposable != null) {
            numDisposable.dispose();
        }
    }

    /**
     * 开始日期选择回调
     */
    private DatePickerDialog.OnDateSetListener onStartDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
        String monthS = String.valueOf(monthOfYear + 1);
        String dayS = String.valueOf(dayOfMonth);
        if (monthS.length() == 1) {
            monthS = "0" + monthS;
        }
        if (dayS.length() == 1) {
            dayS = "0" + dayS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-")
                .append(monthS).append("-")
                .append(dayS).append(" ");
        startTime = sb.toString();
        startTimePicker.show();
    };

    /**
     * 结束日期选择回调
     */
    private DatePickerDialog.OnDateSetListener onEndDateSetListener = (datePicker, year, monthOfYear, dayOfMonth) -> {
        String monthS = String.valueOf(monthOfYear + 1);
        String dayS = String.valueOf(dayOfMonth);
        if (monthS.length() == 1) {
            monthS = "0" + monthS;
        }
        if (dayS.length() == 1) {
            dayS = "0" + dayS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-")
                .append(monthS).append("-")
                .append(dayS).append(" ");
        endTime = sb.toString();
        endTimePicker.show();
    };

    /**
     * 开始时间选择回调
     */
    private TimePickerDialog.OnTimeSetListener onStartTimeSetListener = ((timePicker, hour, minute) -> {
        String hourS = String.valueOf(hour);
        String minuteS = String.valueOf(minute);
        if (hourS.length() == 1) {
            hourS = "0" + hourS;
        }
        if (minuteS.length() == 1) {
            minuteS = "0" + minuteS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(startTime)
                .append(hourS).append(":")
                .append(minuteS).append(":00");
        startTime = sb.toString();
        mBinding.planListStartTime.setText(startTime);
    });

    /**
     * 结束时间选择回调
     */
    private TimePickerDialog.OnTimeSetListener onEndTimeSetListener = ((timePicker, hour, minute) -> {
        String hourS = String.valueOf(hour);
        String minuteS = String.valueOf(minute);
        if (hourS.length() == 1) {
            hourS = "0" + hourS;
        }
        if (minuteS.length() == 1) {
            minuteS = "0" + minuteS;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(endTime)
                .append(hourS).append(":")
                .append(minuteS).append(":00");
        endTime = sb.toString();
        mBinding.planListEndTime.setText(endTime);
    });

    /**
     * 显示日期选择按钮
     */
    private void setDateTime() {
        mBinding.planListStartTime.setText("1970-01-01 00:00:00");
        mBinding.planListEndTime.setText(TimeUtil.ms2Date(System.currentTimeMillis()));

    }

    private void cleanFilterGruop() {
        mBinding.planListUncompleted.setChecked(false);
        mBinding.planListCompleted.setChecked(false);
        mBinding.planListTimeout.setChecked(false);
        mBinding.planListExcess.setChecked(false);
        mBinding.planListAll.setChecked(false);
    }

    /**
     * 打开或关闭筛选选择框
     */
    private void transFilterLayout() {
        if (mBinding.planListFilterLayout.getVisibility() == View.GONE) {
            mBinding.planListFilterLayout.setVisibility(View.VISIBLE);
        } else {
            mBinding.planListFilterLayout.setVisibility(View.GONE);
        }
    }

    private void initRecyclerView() {

        planListAdapter = new PlanListAdapter(planBeanList, getContext(), itemCallBack);
        loadMoreWrapper = new BaseLoadMoreWrapper(planListAdapter);
        loadMoreWrapper.setLoadingViewHeight(DisplayUtil.dip2px(getContext(), 50));
        mBinding.planList.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.planList.setAdapter(loadMoreWrapper);
        mBinding.planList.setItemAnimator(new DefaultItemAnimator());

        mBinding.planList.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onLoadMore() {
                if (planBeanList.size() == maxNum) {
//                    if (maxNum != 0) {
                    //已加载到最大，不再加载
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
//                    }
                } else {
                    int count = maxNum - planBeanList.size();
                    if (count < PAGE_COUNT) {
                        //还剩不到PAGE_COUNT数量的数据加载
                        updateRecyclerView(planBeanList.size(), count);
                    } else {
                        updateRecyclerView(planBeanList.size(), PAGE_COUNT);
                    }
                }
            }
        });
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        getDatas(fromIndex, toIndex);
    }

    private void getDatas(int formIndex, int toIndex) {
        if (toIndex <= 0) {
            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
            return;
        }
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqAllPlan reqAllPlan = new ReqAllPlan();
        if (curType == BaseConstant.TYPE_PLAN_ALL) {
            reqAllPlan.isGetAll = "true";
        } else {
            reqAllPlan.State = String.valueOf(curType);
            reqAllPlan.isGetAll = "false";
        }
        reqAllPlan.startIndex = String.valueOf(formIndex);
        reqAllPlan.numberOfRecords = String.valueOf(toIndex);
        reqAllPlan.TimeQueryChecked = "true";
        if (TextUtils.isEmpty(startTime)) {
            //接口设计原因，默认使用最小时间
            reqAllPlan.mStartTime = "1970-01-01 00:00:00";
        } else {
            reqAllPlan.mStartTime = startTime;
        }
        if (TextUtils.isEmpty(endTime)) {
            //默认使用当前时间
            reqAllPlan.mStartTime = TimeUtil.ms2Date(System.currentTimeMillis());
        } else {
            reqAllPlan.mStartTime = endTime;
        }
        reqAllPlan.PesonChecked = "true";
        reqAllPlan.Publisher = "admin";
        if (!TextUtils.isEmpty(role)) {
            if (!role.equals(BaseConstant.ROLE_SUPER_ADMINS) && !role.equals(BaseConstant.ROLE_ADMINS)) {
                reqAllPlan.PersonLiable = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
            }
        }

        curDisposable = planViewModel.reqAll(reqAllPlan);
    }

    private void getListNums() {
        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);

        ReqPlanNum reqPlanNum = new ReqPlanNum();
        reqPlanNum.status = String.valueOf(curType);
        reqPlanNum.isTime = 1;
        if (TextUtils.isEmpty(startTime)) {
            //接口设计原因，默认使用最小时间
            reqPlanNum.startTime = "19700101";
        } else {
            String[] date1 = startTime.split(" ");
            StringBuilder sb = new StringBuilder();
            if (date1.length == 2) {
                String[] date2 = date1[0].split("-");
                if (date2.length == 3) {
                    sb.append(date2[0])
                            .append(date2[1])
                            .append(date2[2]);
                }
            }
            reqPlanNum.startTime = sb.toString();
        }
        if (TextUtils.isEmpty(endTime)) {
            //默认使用当前时间
            reqPlanNum.endTime = TimeUtil.ms2Date(System.currentTimeMillis());
        } else {
            String[] date1 = endTime.split(" ");
            StringBuilder sb = new StringBuilder();
            if (date1.length == 2) {
                String[] date2 = date1[0].split("-");
                if (date2.length == 3) {
                    sb.append(date2[0])
                            .append(date2[1])
                            .append(date2[2]);
                }
            }
            reqPlanNum.endTime = sb.toString();
        }
        reqPlanNum.isUser = 1;
        if (!TextUtils.isEmpty(role)) {
            if (!role.equals(BaseConstant.ROLE_SUPER_ADMINS) && !role.equals(BaseConstant.ROLE_ADMINS)) {
                reqPlanNum.isPersonliable = 1;
            }
        }
        reqPlanNum.personliable = SharedPreferencesUtil.getInstance().getString(BaseConstant.SP_ACCOUNT, "");
        numDisposable = planViewModel.reqNum(reqPlanNum);
    }

    private ItemCallBack itemCallBack = item -> {
        if (item instanceof IncidentInfo) {
            IncidentInfo incidentInfo = (IncidentInfo) item;
            EventBusMessage eventBusMessage = new EventBusMessage(IncidentDetailsFragment.TAG);
            eventBusMessage.setArg1(incidentInfo);
            EventBus.getDefault().post(eventBusMessage);
        }
    };

    @Override
    public void bindViewModel() {
        planViewModel = ViewModelProviders.of(this).get(PlanViewModel.class);
    }

    @Override
    public void subscribeToModel() {
        if (!planViewModel.getObservableAll().hasObservers()) {
            planViewModel.getObservableAll().observe(this, planBeans -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != planBeans) {
                        if (planBeans.size() > 0) {
                            planBeanList.addAll(planBeans);
                            LogUtil.d(TAG, "load complete = " + planBeans);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                        } else {
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (!planViewModel.getObservableNum().hasObservers()) {
            planViewModel.getObservableNum().observe(this, integer -> {
                if (this.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED)) {
                    if (null != integer) {
                        maxNum = integer;
                        if (maxNum >= PAGE_COUNT) {
                            getDatas(0, PAGE_COUNT);
                        } else {
                            getDatas(0, maxNum);
                        }
                    } else {
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });
        }

        if (null == planBeanList || planBeanList.size() <= 0) {
            getListNums();
        }
    }
}
