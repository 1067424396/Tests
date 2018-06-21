package com.example.administrator.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AutoRollLayout autoRollLayout;
    List<BankInfo> bankInfos=new ArrayList<>();
    BankInfo bankInfo;
    MyGridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoRollLayout= (AutoRollLayout) findViewById(R.id.arl_viewpager);
        gridView= (MyGridView) findViewById(R.id.gridView);

        for (int i=0;i<6;i++){
            bankInfo= new BankInfo();
            bankInfo.setImageUrl("http://zhcs-static.oss-cn-shanghai.aliyuncs.com/app/banner/%E5%A5%BD%E5%8F%8B%E9%82%80%E8%AF%B7APP.jpg");
            bankInfo.setTitle(i);
            bankInfos.add(bankInfo);
        }
       // bankInfos.add(0,bankInfo);

        autoRollLayout.setFragment(this);
        autoRollLayout.setItems(bankInfos);

        setBottom();

    }

    private void setBottom() {
        GridAdapter gridAdapter = new GridAdapter();
        gridView.setAdapter(gridAdapter);
        int currentItems = autoRollLayout.getCurrentItems();
        Toast.makeText(MainActivity.this,""+currentItems,Toast.LENGTH_LONG).show();
    }
    private String[] mine_list = { "可用余额(元)", "投资金额(元)", "梦想计划(元)", "待收收益(元)"};
    private String[] amount_list = { "0.00", "0.00", "0.00", "0.00"};
    private int[] mine_imageId = { R.mipmap.dp_dream,
            R.mipmap.dp_dream, R.mipmap.dp_dream,
            R.mipmap.dp_dream, };
    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return bankInfos.size();
        }

        @Override
        public Object getItem(int arg0) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }



        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(MainActivity.this, R.layout.item_bottom,
                        null);
            }

            ImageView iv_dream = (ImageView) convertView
                    .findViewById(R.id.iv_dream);
            //iv_dream.setVisibility(View.GONE);
            //iv_balance_icon.setImageResource(mine_imageId[position]);
            if (autoRollLayout.getCurrentItems()==1){

            }
            return convertView;
        }

    }
}
