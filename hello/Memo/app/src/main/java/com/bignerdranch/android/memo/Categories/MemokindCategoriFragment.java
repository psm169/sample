package com.bignerdranch.android.memo.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.android.memo.Data.CategoriData;
import com.bignerdranch.android.memo.Data.MemoData;
import com.bignerdranch.android.memo.Database.RunDatabaseHelper;
import com.bignerdranch.android.memo.Memoes.MemoActivity;
import com.bignerdranch.android.memo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by realbyte on 2017. 7. 17..
 */

public class MemokindCategoriFragment extends ListFragment {

    private CategoriData mCategoriData;


    private RunDatabaseHelper dbHelper;
    private long selectedCategoriId ;

    private MemoKindAdapter memoAdapter;
    private ArrayList<MemoData> mMemoDataArrayList;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

         selectedCategoriId = getActivity().getIntent().getLongExtra("Categori_id_info",0);
        dbHelper = new RunDatabaseHelper(getActivity() , "aaa" , null , 1);

        mMemoDataArrayList = new ArrayList<>();
        memoAdapter = new MemoKindAdapter(mMemoDataArrayList);
        setListAdapter(memoAdapter);

    }
    @Override
    public void onResume()
    {
        super.onResume();

        mMemoDataArrayList.clear();

        ArrayList<MemoData> test =  dbHelper.getResultKindCategoriList(selectedCategoriId);
        for(MemoData str : test)
        {
            mMemoDataArrayList.add(str);
        }
        memoAdapter.notifyDataSetChanged();

    }

    @Override
    public void onListItemClick(ListView l , View view, int position, long id)
    {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCoe, Intent data){
        ((MemokindCategoriFragment.MemoKindAdapter)getListAdapter()).notifyDataSetChanged();;
    }

    private class MemoKindAdapter extends ArrayAdapter<MemoData>
    {
            public MemoKindAdapter(ArrayList<MemoData> memoes)
            {
                super(getActivity() ,0, memoes);
            }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_memo_list_kind_categori, null);
            }

            MemoData memoData = getItem(position);

            TextView setKindTitle = (TextView) convertView.findViewById(R.id.list_kind_title);
            setKindTitle.setText(memoData.getMemoTitle());
            TextView setKindText = (TextView) convertView.findViewById(R.id.list_kind_text);
            setKindText.setText(memoData.getMemoText());

            long time = memoData.getMemoDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);

            TextView setKindDate = (TextView) convertView.findViewById(R.id.list_kind_date);
            int tempMonth = calendar.get(Calendar.MONTH) +1;
            setKindDate.setText(calendar.get(Calendar.YEAR) + "/" + tempMonth + "/" + calendar.get(Calendar.DAY_OF_MONTH));

            TextView setKindCategori = (TextView) convertView.findViewById(R.id.list_kind_categori_info);
            setKindCategori.setText(dbHelper.findCategoriTitle(selectedCategoriId));

            RelativeLayout kindListRelative = (RelativeLayout)convertView.findViewById(R.id.list_kind_categori_layout_button);
            kindListRelative.setTag(position);
            kindListRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    MemoData memoData = mMemoDataArrayList.get((int)v.getTag());
                    Intent i = new Intent(getActivity(), MemoActivity.class);
                    Log.d("asd", String.valueOf(memoData.getMemoId()) );
                    i.putExtra("memo_id",memoData.getMemoId());
                    i.putExtra("newmemo", true);

                    startActivity(i);

                }
            });




            return convertView;

        }
    }
}
