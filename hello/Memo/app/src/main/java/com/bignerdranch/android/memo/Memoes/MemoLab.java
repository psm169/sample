package com.bignerdranch.android.memo.Memoes;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by realbyte on 2017. 7. 7..
 */

public class MemoLab {

    private ArrayList<Memo> mMemos;


    //싱글톤
    private static MemoLab sMemoLab;
    private Context mAppContext;

    private MemoLab(Context context)
    {
        mAppContext = context;
        mMemos = new ArrayList<Memo>();

        //무작위 메모 생성
      //  for(int i = 0 ; i<5; i++)
      //  {
      //      Memo m = new Memo();
      //      m.setTitle("메모 #"+i);
      //      m.setDate(new Date());
      //      mMemos.add(m);
      //  }
    }

    public static MemoLab get(Context c)
    {       //방어코드
        if(sMemoLab ==null)
        {
            sMemoLab = new MemoLab(c.getApplicationContext());
        }
        return sMemoLab;
    }

    public ArrayList<Memo> getMemo()
    {
        return mMemos;
    }

    public Memo getMemo(int position)
    {
        if(mMemos != null && mMemos.size() > position)
        {
            return mMemos.get(position);
        }
        else
            return null;
    }

    public Memo getMemo(UUID id)
    {
        for(Memo m : mMemos)
        {
            if(m.getId().equals(id))
                return m;
        }
        return null;
    }

    //새메모
    public void addMemo(Memo m)
    {
        mMemos.add(m);
    }

}
