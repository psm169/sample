package com.bignerdranch.android.memo.Memoes;

/**
 * Created by realbyte on 2017. 7. 7..
 */
import java.util.Date;
import java.util.UUID;

/**
 * Created by realbyte on 2017. 7. 7..
 */

public class Memo {
    private String mTitle =" ";
    private String mText = " ";
    private Date mdate = null;
    private UUID mId;
    private int number = 0;

    public Memo(){
        mdate = new Date(); //잘모르겟음... 필요없나??
        mId = UUID.randomUUID();
    }

    public String getTitle()
    {
        return mTitle;
    }


    public String getText()
    {
        return mText;
    }

    public void setTitle(String Title)
    {
        mTitle = Title;
    }

    public void setText(String memoText)
    {
        mText = memoText;
    }

    public void setDate(Date date)
    {
        mdate = date;
    }
    public Date getDate()
    {
        return mdate;
    }

    public UUID getId()
    {
        return mId;
    }

    public String toString(){
        return mTitle;
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int num)
    {
        number = num;
    }

}
