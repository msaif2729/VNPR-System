package com.example.vnpr;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyTextChange implements TextWatcher {

    public EditText[] editTexts;
    private View view;

    private Button verify;
    private String button;

    public MyTextChange(View view, EditText[] editTexts, String button, Button verify)
    {
        super();
        this.editTexts = editTexts;
        this.view = view;
        this.verify = verify;
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

        String str = editable.toString();
        if(view.getId()==editTexts[0].getId())
        {
            if(str.length()==1)
            editTexts[1].requestFocus();

        }
        if(view.getId()==editTexts[1].getId())
        {
            if(str.length()==1)
            editTexts[2].requestFocus();
        }
        if(view.getId()==editTexts[2].getId())
        {
            if(str.length()==1)
            editTexts[3].requestFocus();
        }
        if(view.getId()==editTexts[3].getId())
        {
            if(str.length()==1)
            editTexts[4].requestFocus();
        }
        if(view.getId()==editTexts[4].getId())
        {
            if(str.length()==1)
            editTexts[5].requestFocus();
        }

        if(button=="next")
        {
            verify.setId(R.id.verify_next);
            verify.setEnabled(false);

        }
        if(button=="register")
        {
            verify.setId(R.id.verify_register);
            verify.setEnabled(false);
        }

        if (editTexts[0].length()==1 && editTexts[1].length()==1 && editTexts[2].length()==1 && editTexts[3].length()==1 && editTexts[4].length()==1 && editTexts[5].length()==1 )
        {
            verify.setEnabled(true);
//            verify.getText().toString();
//            String code = editTexts[0].getText().toString()+editTexts[1].getText().toString()+editTexts[2].getText().toString()+editTexts[3].getText().toString()+editTexts[4].getText().toString()+editTexts[5].getText().toString();
        }
    }
}
