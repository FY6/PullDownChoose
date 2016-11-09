package com.wfy.pulltochoose.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfy.pulltochoose.R;

public class SpinnerView extends RelativeLayout implements View.OnClickListener {

	private static final int POPUPHEIGHT = 500;
	private ImageView down_arrow;
	private EditText edtitext_back;
	private PopupWindow popu;
	private ListView mListView;
	private ArrayList<String> data;
	private SpinnerAdapter adapter;
	private Context mContext;

	public SpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		init();
	}

	public SpinnerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		init();
	}

	public SpinnerView(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	private void init() {
		initViews();
		initData();
		initListView();
		initListener();
	}

	/**
	 * 初始化Views：
	 * 
	 * 1、把layout布局与当前view对象关联起来
	 * 
	 * 2、 获取layout中给子view对象
	 */
	private void initViews() {
		// 把layout_spinner_view布局与当前view对象关联起来
		View rootView = View.inflate(mContext, R.layout.layout_spinner_view,
				this);
		down_arrow = (ImageView) rootView.findViewById(R.id.down_arrow);
		edtitext_back = (EditText) rootView.findViewById(R.id.edtitext_back);
	}

	private void initListener() {
		down_arrow.setOnClickListener(this);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String content = data.get(position);
				edtitext_back.setText(content);
				popu.dismiss();
			}
		});
	}

	private void initListView() {
		mListView = new ListView(mContext);
		mListView.setBackgroundResource(R.drawable.listview_background);
		mListView.setVerticalScrollBarEnabled(false);// 取消垂直滚动条
		adapter = new SpinnerAdapter();
		mListView.setAdapter(adapter);
	}

	private void initData() {
		data = new ArrayList<String>();
		for (int i = 0; i <= 10; i++) {
			data.add("9000" + i);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.down_arrow:
			// 弹出popupwindown
			initPopuWin();
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化PopupWindown
	 */
	private void initPopuWin() {
		if (popu == null) {
			popu = new PopupWindow(mListView, edtitext_back.getWidth(),
					POPUPHEIGHT);
		}
		popu.setFocusable(true);// 如果popup中包含一个可获得焦点的view，那么popu的焦点将会被可获得焦点的view夺取焦点
		popu.setBackgroundDrawable(new ColorDrawable());// 如果需要执行动画必须得有背景
		popu.setOutsideTouchable(true);// 在外边点击，popup小时，， 必须有背景，不然就无效
		popu.showAsDropDown(edtitext_back, 0, 0);
	}

	class SpinnerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public String getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(mContext, R.layout.item_list_view,
						null);
				holder.iconDelete = (ImageView) convertView
						.findViewById(R.id.icon_delete);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String item = getItem(position);
			holder.tvContent.setText(item);

			final View view = convertView;
			// 设置删除监听
			holder.iconDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					data.remove(position);
					adapter.notifyDataSetChanged();

					if (data.size() == 0) {
						popu.dismiss();
						return;
					}

					// 计算ListView的高度
					int listViewHeight = view.getHeight() * data.size();
					popu.update(edtitext_back.getWidth(),
							listViewHeight > POPUPHEIGHT ? POPUPHEIGHT
									: listViewHeight);
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvContent;
		public ImageView iconDelete;
	}
}
