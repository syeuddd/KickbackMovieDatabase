package com.leafnext.kickbackmoviedatabase.Utils;

import android.text.method.LinkMovementMethod;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by ehteshs1 on 2018/02/27.
 */

public class ResizeTextView {


    public static void makeTextViewReizable(final TextView tv, final int maxLine, final String expandText, boolean viewMore){

        if (tv.getTag()==null){
            tv.setTag(tv.getText());
        }

        ViewTreeObserver vto = tv.getViewTreeObserver();
          vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
              @Override
              public void onGlobalLayout() {
                  String text;
                  int lineEndIndex;
                  ViewTreeObserver obs = tv.getViewTreeObserver();
                  obs.removeOnGlobalLayoutListener(this);
                  if (maxLine>0 && tv.getLineCount() >= maxLine){
                      lineEndIndex = tv.getLayout().getLineEnd(maxLine-1);
                      text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                  }else {
                      lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                      text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                  }

                  tv.setText(text);
                  tv.setMovementMethod(LinkMovementMethod.getInstance());
              }
          });




    }

}
