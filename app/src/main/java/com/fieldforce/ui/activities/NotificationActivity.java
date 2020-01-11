package com.fieldforce.ui.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fieldforce.R;
import com.fieldforce.db.DatabaseManager;
import com.fieldforce.interfaces.ItemTouchHelperAdapter;
import com.fieldforce.interfaces.OnStartDragListener;
import com.fieldforce.ui.adapters.NotificationCardAdapter;
import com.fieldforce.utility.AppConstants;
import com.fieldforce.utility.AppPreferences;
import com.fieldforce.utility.SimpleItemTouchHelperCallback;

public class NotificationActivity extends ParentActivity implements OnStartDragListener {

    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Context mContext = this;

        ImageView imgModule = findViewById(R.id.img_module);
        imgModule.setImageResource(R.drawable.ic_action_notification);
        TextView title = findViewById(R.id.txt_title);
        title.setText(getString(R.string.notification));
        TextView txtSubTitle = findViewById(R.id.txt_sub_title);
        txtSubTitle.setVisibility(View.GONE);
        ImageView imgBack = findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        NotificationCardAdapter adapter = new NotificationCardAdapter(mContext, DatabaseManager.getAllNotification(mContext,
                AppPreferences.getInstance(mContext).getString(AppConstants.EMP_ID, AppConstants.BLANK_STRING)));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
