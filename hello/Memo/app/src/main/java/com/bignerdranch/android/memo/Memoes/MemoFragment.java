package com.bignerdranch.android.memo.Memoes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.memo.Data.CategoriData;
import com.bignerdranch.android.memo.Data.MemoData;
import com.bignerdranch.android.memo.Database.RunDatabaseHelper;
import com.bignerdranch.android.memo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by realbyte on 2017. 7. 7..
 */

public class MemoFragment extends Fragment {

    public static final String EXTRA_MEMO_ID = "com.bignerdranch.android.memo.memo_id";
    public static final String DIALOG_DATE = "date";
    private static final int REQUEST_DATE = 0;

    private static RunDatabaseHelper dbhelper;
    private RunDatabaseHelper dbhelperCategori;

    private boolean isOldMemo = false;
    private MemoData mMemoData;
    private CategoriData mCategoriData;
    private Memo mMemo;
    private EditText mTitleField = null;
    private EditText mTextField = null;
    private Button mSaveButton;
    private Button mNoSaveButton;
    private Button mSettingDateButton;

    private Button mDeleteMemoButton;
    private Spinner mCategoriMemoSpinner;

    private ArrayList<CategoriData> spinnerCombobox;




    FragmentTransaction fragmentTransaction;

    //메모라는 객체를 생성주기 생성
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //       mMemo = new MemoData();
        //    UUID memoId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_MEMO_ID);

        //왜 안될꼬. spinner = null




        //참조한게 새로운 메모인지.
        isOldMemo = getActivity().getIntent().getBooleanExtra("newmemo", true);
        dbhelper = new RunDatabaseHelper(getActivity(), "dbnote", null, 1);
        if (isOldMemo) {
            long memodataId = getActivity().getIntent().getLongExtra("memo_id", 0);
            mMemoData = dbhelper.getReadMemo(memodataId);
        } else {
            mMemoData = new MemoData();
        }

        //    UUID memoId = (UUID)getArguments().getSerializable(EXTRA_MEMO_ID);
        //    mMemo = MemoLab.get(getActivity()).getMemo(position);

    }

    //창띄우기 onCreateView 오버라이딩 1.Layout을 당겨올 LayoutInflater , 뷰의 부모 ViewGroup,Bundle)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        //뷰 속성을 가져오는데 뜨울것의 레이아웃 주소
        View v = inflater.inflate(R.layout.fragment_memo, parent, false);

        mTitleField = (EditText) v.findViewById(R.id.mymemo_title_hint);
        mTextField = (EditText) v.findViewById(R.id.mymemo_text_hint);
        mSettingDateButton = (Button) v.findViewById(R.id.memo_settiong_date);
        mCategoriMemoSpinner = (Spinner)v.findViewById(R.id.memo_select_categori_spinner);
        //EditText위젯들을 코드에 연결

        if (isOldMemo) {
            mTitleField.setText(mMemoData.getMemoTitle());
            mTextField.setText(mMemoData.getMemoText());
            mSettingDateButton.setText(String.valueOf(mMemoData.getMemoDate()));
            mCategoriMemoSpinner.setSelection((int)mMemoData.getMemoId()); //작동 x 덮어씌워질 내용

        }


        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //내비둬
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //     if (mMemo != null && mMemo.getTitle() != null)
                mMemoData.setMemoTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//에딧이라 필요없음
        //     if (mMemo.getText() != null)

        mTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //     if (mMemo != null && mMemo.getText() != null)
                mMemoData.setMemoText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //달력
        updateDate();
        mSettingDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //     if(mMemoData == null)
                //     {
                //         mMemoData = new MemoData();
                //     }
                FragmentManager fm = getActivity().getSupportFragmentManager();


                Date memoDate = new Date();
                long memoDateLong = 0;

                if (mMemoData != null) {
                    memoDateLong = mMemoData.getMemoDate();
                }

                if (memoDateLong == 0) {
                    memoDateLong = Calendar.getInstance().getTimeInMillis();
                }

                memoDate.setTime(memoDateLong);

                DatePickerFragment dialog = DatePickerFragment.newInstance(memoDate);
                dialog.setTargetFragment(MemoFragment.this, REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });









        //취소버튼
        mNoSaveButton = (Button) v.findViewById(R.id.mymemo_nosave_button);
        mNoSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        mNoSaveButton.setEnabled(true);


//        mSettingDateButton.setText(mMemo.getDate().toString());


        mSaveButton = (Button) v.findViewById(R.id.mymemo_save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //캐스팅 안해줘도 되는지.
                //     if(isNewMemo != false)
                // {

                if(isOldMemo)
                {

                    MemoData tempMemoData = new MemoData();
                    tempMemoData.setMemoId(getActivity().getIntent().getLongExtra("memo_id", 0));
                    tempMemoData.setMemoTitle(mTitleField.getText().toString());
                    tempMemoData.setMemoText(mTextField.getText().toString());
                    tempMemoData.setMemoDate(Long.parseLong(mSettingDateButton.getTag().toString()));
                    //tempMemoData.setMemoCategoriInfo(mCategoriMemoSpinner.getSelectedItemId());
                    String memoSpinnerTitleTemp = mCategoriMemoSpinner.getSelectedItem().toString();
                    Long spinnerSelect = dbhelper.getCategoriIdnum(memoSpinnerTitleTemp);
                    tempMemoData.setMemoCategoriInfo(spinnerSelect);


                    //join을 사용해서 카테고리 id와 일치하는 항목을 set한다
                    dbhelper.updateTest(tempMemoData);
                }
                else {

                    mMemoData = new MemoData();
                    String memoTitleTemp = mTitleField.getText().toString();
                    String memoTextTemp = mTextField.getText().toString();

                    String memoSpinnerTitleTemp = mCategoriMemoSpinner.getSelectedItem().toString();
                    String compareTitleNull = "";

                    Long memoDateTemp = Long.parseLong(mSettingDateButton.getTag().toString());
                    if(memoDateTemp == 0)
                    {

                        Calendar calendar = Calendar.getInstance();
                        long now = calendar.getTimeInMillis();

                        memoDateTemp = now;
                    }

                    mMemoData.setMemoText(memoTextTemp);
                    mMemoData.setMemoTitle(memoTitleTemp);
                    mMemoData.setMemoDate(memoDateTemp);




                    if(memoTitleTemp.equals(compareTitleNull) )
                    {
                        int i = 0;
                        boolean isCheckTitle = true;

                        while (isCheckTitle)
                        {
                            i++;
                            memoTitleTemp = "새 메모" + i;
                            isCheckTitle = dbhelper.isCheckTitleIsEmpty(memoTitleTemp);

                        }
                        long memoCategoriIdTemp = dbhelper.getCategoriIdnum(memoSpinnerTitleTemp);

                    //    dbhelper.insert(memoTitleTemp, memoDateTemp, memoTextTemp,memoCategoriIdTemp);
                        mMemoData.setMemoCategoriInfo(memoCategoriIdTemp);
                        dbhelper.insertMemoData(mMemoData);
                    }
                    else{
                        String title = mTitleField.getText().toString();
       //                 String text = mTextField.getText().toString();
       //                 long date = Long.parseLong(mSettingDateButton.getTag().toString());
                        long memoCategoriIdTemp = dbhelper.getCategoriIdnum(memoSpinnerTitleTemp);
                        mMemoData.setMemoCategoriInfo(memoCategoriIdTemp);
                        dbhelper.insertMemoData(mMemoData);
                    }

                 //   getActivity().finish();
                    //    }
                    //     else
                    //         dbhelper.insert("1","1"," test");
                }
                getActivity().finish();

            }
        });
        mSaveButton.setEnabled(true);



        // 삭제버튼
        mDeleteMemoButton = (Button)v.findViewById(R.id.memo_delete_button);
        mDeleteMemoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isOldMemo) {
                    long tempId = getActivity().getIntent().getLongExtra("memo_id", 0);
                    dbhelper.delete(tempId);
                    getActivity().finish();
                }
                else
                    Toast.makeText(getActivity(),R.string.it_is_oldmemo2,Toast.LENGTH_SHORT).show();


            }
        });
        mDeleteMemoButton.setEnabled(true);




        Spinner mCategoriMemoSpinner = (Spinner)v.findViewById(R.id.memo_select_categori_spinner);
        spinnerCombobox = dbhelper.getSpinnerList(); //spinnerCombobox = ArrayList<String> ;
//        mCategoriMemoSpinner.setPrompt("카테고리를 고르세요");
        ArrayList<String> spinnerText = new ArrayList<>();

        for(CategoriData str : spinnerCombobox)
        {
            spinnerText.add(str.getCategoriTitle());
        }


        //categorilist에 있는 항목들을 긁어옴
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, spinnerText);

        //       Array.xml에 저장되어있는 스트링 변수들을 항목으로 사용하겠다
        //       ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.location ,  android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCategoriMemoSpinner.setAdapter(spinnerAdapter);

        mCategoriMemoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //각 항목 클릭시 포지션값을 토스트에 띄운다.


                CategoriData tempCategori = spinnerCombobox.get(position);
                view.setTag(tempCategori.getId());
                Toast.makeText(getActivity(), Long.toString(tempCategori.getId()), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (isOldMemo) {
            long categoryId = mMemoData.getMemoCategoriInfo();
            int index = 0;
            for (  ;index < spinnerCombobox.size()   ; index++) {
                if (categoryId == spinnerCombobox.get(index).getId()) {
                    break;
                }
            }
            mCategoriMemoSpinner.setSelection(index);
            mCategoriMemoSpinner.setTag(categoryId);
        }
        return v;
    }







    //값 전달 받음
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mMemoData.setMemoDate(date.getTime());
            updateDate();

        }
    }

    //인자를 프래그먼트에 추가해주는 코드
    public static MemoFragment newInstance(UUID memoId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_MEMO_ID, memoId);

        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    private void updateDate() {
        if (mMemoData == null)
            mMemoData = new MemoData();

        Date displayDate = new Date();
        displayDate.setTime(mMemoData.getMemoDate());
        mSettingDateButton.setText(displayDate.toString());
        mSettingDateButton.setTag(mMemoData.getMemoDate());
    }

    @Override
    public void onDestroy() {
        Log.d(this.getClass().getSimpleName(), "onDestroy()");
        super.onDestroy();
    }
}

