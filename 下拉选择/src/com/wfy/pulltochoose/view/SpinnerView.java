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
	 * ��ʼ��Views��
	 * 
	 * 1����layout�����뵱ǰview�����������
	 * 
	 * 2�� ��ȡlayout�и���view����
	 */
	private void initViews() {
		// ��layout_spinner_view�����뵱ǰview�����������
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
		mListView.setVerticalScrollBarEnabled(false);// ȡ����ֱ������
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
			// ����popupwindown
			initPopuWin();
			break;
		default:
			break;
		}
	}

	/**
	 * ��ʼ��PopupWindown
	 */
	private void initPopuWin() {
		if (popu == null) {
			popu = new PopupWindow(mListView, edtitext_back.getWidth(),
					POPUPHEIGHT);
		}
		popu.setFocusable(true);// ���popup�а���һ���ɻ�ý����view����ôpopu�Ľ��㽫�ᱻ�ɻ�ý����view��ȡ����
		popu.setBackgroundDrawable(new ColorDrawable());// �����Ҫִ�ж���������б���
		popu.setOutsideTouchable(true);// ����ߵ����popupСʱ���� �����б�������Ȼ����Ч
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
			// ����ɾ������
			holder.iconDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					data.remove(position);
					adapter.notifyDataSetChanged();

					if (data.size() == 0) {
						popu.dismiss();
						return;
					}

					// ����ListView�ĸ߶�
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
