package com.example.calculator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.app.Application;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.Stack;
import java.util.Vector;



public class MainActivity2 extends AppCompatActivity {

    Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9,
            buttonAdd, buttonSub, buttonDivision, buttonMul, button10,  buttonEqual, buttonReset ,
            openParentheses, closeParentheses, buttonDelete, buttonStopwatch, buttonAlarm,buttonFrom,buttonTo,buttonConvert,
            buttonSin, buttonCos, buttonTan, buttonDown, buttonUp, buttonCosec, buttonSec, buttonCot, buttonLog, buttonPow, buttonRoot, buttonMod, buttonWhatsapp;

    EditText OutputText;
    TextView InputText;


    int i = 0, j, Parentheses = 0, send = 0, wpsend = 0,From=10,To=10;
    String Equation = "";
    boolean isPaused = true, floatingPoint = false, Running = false;
    long timeRemaining = 0, seconds = 0;

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (OutputText.length() > 0) {
                        try {
                            OutputText.setText(String.format("%d", Long.parseLong(OutputText.getText() + "") + 1));
                        } catch (NumberFormatException e) {
                            OutputText.setText("");
                        }

                    } else {
                        OutputText.setText("0");
                    }


                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if (OutputText.length() > 0) {
                        long count;
                        try {
                            count = Long.parseLong(OutputText.getText() + "");
                        } catch (NumberFormatException e) {
                            count = 0;
                        }
                        if (count > 0) {
                            OutputText.setText(String.format("%d", count - 1));
                        } else {
                            OutputText.setText("");
                        }
                    }
                }
                return true;
            case KeyEvent.KEYCODE_BACK: {
                if (action == KeyEvent.ACTION_DOWN) {
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                }
            }
            return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        //getActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        final Vibrator vibe = (Vibrator) MainActivity2.this.getSystemService(Context.VIBRATOR_SERVICE);
        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        buttonAdd = (Button) findViewById(R.id.buttonadd);
        buttonSub = (Button) findViewById(R.id.buttonsub);
        buttonMul = (Button) findViewById(R.id.buttonmul);
        buttonDivision = (Button) findViewById(R.id.buttondiv);
        buttonEqual = (Button) findViewById(R.id.buttoneql);
        OutputText = (EditText) findViewById(R.id.edt2);
        InputText = (TextView) findViewById(R.id.edt1);
        button10 = (Button) findViewById(R.id.button10);
        buttonDelete = (Button)findViewById(R.id.buttonC);
        buttonReset = (Button) findViewById(R.id.buttonres);
        openParentheses = (Button) findViewById(R.id.buttonp1);
        closeParentheses = (Button) findViewById(R.id.buttonp2);
        buttonSin = (Button) findViewById(R.id.buttonsin);
        buttonCos = (Button) findViewById(R.id.buttoncos);
        buttonMod = (Button) findViewById(R.id.buttonmod);
        buttonUp = (Button) findViewById(R.id.buttonup);
        buttonDown = (Button) findViewById(R.id.buttondown);
        buttonWhatsapp = (Button)findViewById(R.id.buttonwp);
        buttonPow = (Button)findViewById(R.id.buttonpow);
        buttonRoot = (Button)findViewById(R.id.buttonroot);
        buttonStopwatch = (Button)findViewById(R.id.buttonsw);
        buttonAlarm = (Button)findViewById(R.id.buttonalarm);
        buttonFrom = (Button)findViewById(R.id.buttonfrom);
        buttonTo = (Button)findViewById(R.id.buttonto);
        buttonConvert = (Button)findViewById(R.id.buttonconvert);



        buttonFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(buttonFrom.getText());
                if(buttonFrom.getText().toString().equals("Bin"))
                {
                    buttonFrom.setText("Oct");
                    From=8;
                }
                else if(buttonFrom.getText().toString().equals("Oct"))
                {
                    buttonFrom.setText("Dec");
                    From=10;
                }
                else if(buttonFrom.getText().toString().equals("Dec"))
                {
                    buttonFrom.setText("Hex");
                    From=16;
                }
                else if(buttonFrom.getText().toString().equals("Hex"))
                {
                    buttonFrom.setText("Bin");
                    From=2;
                }
                Running = false;
                seconds = 0;
                InputText.setText("");
                Equation = "";
                OutputText.setText("");
                floatingPoint = false;
                Parentheses = 0;
                isPaused = true;
                buttonWhatsapp.setText("xⁿ");
                buttonSin.setText("sin");
                send = 0;
                wpsend = 0;
                buttonStopwatch.setText("►");
                buttonMod.setText("tan");

            }
        });

        buttonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttonTo.getText().toString().equals("Bin"))
                {
                    buttonTo.setText("Oct");
                    To=8;
                }
                else if(buttonTo.getText().toString().equals("Oct"))
                {
                    buttonTo.setText("Dec");
                    To=10;
                }
                else if(buttonTo.getText().toString().equals("Dec"))
                {
                    buttonTo.setText("Hex");
                    To=16;
                }
                else if(buttonTo.getText().toString().equals("Hex"))
                {
                    buttonTo.setText("Bin");
                    To=2;
                }

            }
        });


        buttonStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Running) {
                    Running = false;
                    buttonStopwatch.setText("►");
                }

                else {
                    Running = true;
                    buttonStopwatch.setText("❚❚");
                    runTimer();
                }

            }
        });

        buttonAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = 0, minute = 0, i;
                for (i = 0; i < Equation.length() && Equation.charAt(i) != '.'; i++) {
                    hour *= 10;
                    hour += Equation.charAt(i) - 48;
                }

                if(i<Equation.length())
                {
                    i++;
                    for (; i < Equation.length(); i++) {
                        minute *= 10;
                        minute += Equation.charAt(i) - 48;
                    }



                    if (hour < 24 && minute < 60) {
                        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                        intent.putExtra(AlarmClock.EXTRA_HOUR, hour);
                        intent.putExtra(AlarmClock.EXTRA_MINUTES, minute);
                        startActivity(intent);
                    }

                    else
                    {
                        if (InputText.getText().length() < 40) {
                            char Last = '%';
                            if (Equation.length() > 0)Last = Equation.charAt(Equation.length() - 1);
                            if (Last >= '0' && Last <= '9' || Last == ')') Equation += "%";
                            else if (Last == '÷' || Last == '×' ||Last=='+'||Last=='^' ) Equation = Equation.substring(0, Equation.length() - 1) + '%';
                            else if (Equation.length() > 1 && Last == '-') Equation = Equation.substring(0, Equation.length() - 1) + '%';
                            InputText.setText(Equation); floatingPoint = false;
                        }
                    }
                }
                else
                {
                    if (InputText.getText().length() < 40) {
                        char Last = '%';
                        if (Equation.length() > 0)Last = Equation.charAt(Equation.length() - 1);
                        if (Last >= '0' && Last <= '9' || Last == ')') Equation += "%";
                        else if (Last == '÷' || Last == '×'||Last=='+'||Last=='^' ) Equation = Equation.substring(0, Equation.length() - 1) + '%';
                        else if (Equation.length() > 1 && Last == '-') Equation = Equation.substring(0, Equation.length() - 1) + '%';
                        InputText.setText(Equation); floatingPoint = false;
                    }
                }

            }
        });


        buttonWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wpsend++;
                if ( wpsend > 1) {
                    wpsend = 0;
                    sendWP(InputText.getText() + "", OutputText.getText() + "");
                    buttonWhatsapp.setText("xⁿ");
                    OutputText.setEnabled(false);
                    OutputText.setFocusable(false);
                    OutputText.setFocusableInTouchMode(false);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(OutputText.getWindowToken(), 0);
                } else {
                    OutputText.setEnabled(true);
                    OutputText.setFocusable(true);
                    OutputText.setFocusableInTouchMode(true);
                    OutputText.setText("");
                    buttonWhatsapp.setText("➤");
                    OutputText.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });

        OutputText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode ==  KeyEvent.KEYCODE_DPAD_CENTER || keyCode ==  KeyEvent.KEYCODE_ENTER) {

                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        // do nothing yet
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        sendWP(Equation, OutputText.getText() + "");
                    }
                    return true;

                } else {
                    // it is not an Enter key - let others handle the event
                    return false;
                }
            }

        });


        buttonSin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    OutputText.setText("Permission needed");
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.SEND_SMS}, 1);
                }
                send++;
                if (send > 1) {
                    if (OutputText.getText().length() > 0) {
                        sendMessage(InputText.getText() + "", OutputText.getText() + "");
                        OutputText.setText("Message sent");
                        OutputText.setEnabled(false);
                        OutputText.setFocusable(false);
                        OutputText.setFocusableInTouchMode(false);
                    } else OutputText.setText("Message not sent");
                    buttonSin.setText("sin");

                    send = 0;
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(OutputText.getWindowToken(), 0);
                } else {
                    OutputText.setEnabled(true);
                    OutputText.setFocusable(true);
                    OutputText.setFocusableInTouchMode(true);
                    OutputText.setText("");
                    OutputText.requestFocus();
                    buttonSin.setText("➤");
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
            }
        });







        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OutputText.length() > 0) {
                    try {
                        OutputText.setText(String.format("%d", Long.parseLong(OutputText.getText() + "") + 1));
                    } catch (NumberFormatException e) {
                        OutputText.setText("");
                    }

                } else {
                    OutputText.setText("0");
                }
            }
        });
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OutputText.length() > 0) {
                    long count;
                    try {
                        count = Long.parseLong(OutputText.getText() + "");
                    } catch (NumberFormatException e) {
                        count = 0;
                    }
                    if (count > 0) {
                        OutputText.setText(String.format("%d", count - 1));
                    } else {
                        OutputText.setText("");
                    }
                }
            }
        });

        buttonMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaused) {
                    isPaused = !isPaused;
                    long time;
                    try {
                        time = Long.parseLong(OutputText.getText() + "");
                        buttonMod.setText("❚❚");
                        Timer(time);
                    } catch (NumberFormatException e) {
                        isPaused = !isPaused;
                        OutputText.setText("");
                        buttonMod.setText("tan");
                    }
                } else {
                    isPaused = !isPaused;
                    buttonMod.setText("tan");
                }
            }
        });


        buttonCos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity2.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    OutputText.setText("Permission needed");
                    ActivityCompat.requestPermissions(MainActivity2.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
                } else {
                    Call(InputText.getText() + " ");
                }

            }
        });


        button1.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80);
                if (Equation.length() < 40&&From>1) {
                    Equation += "1"; InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80);
                if (Equation.length() < 40&&From>2) {
                    Equation += "2";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>3) {
                    Equation += "3"; InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>4) {
                    Equation += "4";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>5) {
                    Equation += "5";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>6) {
                    Equation += "6";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>7) {
                    Equation += "7";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>8) {
                    Equation += "8"; InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>9) {
                    Equation += "9";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });
        button0.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                vibe.vibrate(80); if (InputText.getText().length() < 40&&From>0) {
                    Equation += "0";  InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40) {
                    char Last = '+';
                    if (Equation.length() > 0)Last = Equation.charAt(Equation.length() - 1);
                    if (Last >= '0' && Last <= '9' || Last == ')') Equation += "+";
                    else if (Last == '÷' || Last == '×' ||Last=='^') Equation = Equation.substring(0, Equation.length() - 1) + '+';
                    else if(Last=='%') Equation = Equation.substring(0, Equation.length() - 1) + '+';
                    else if (Equation.length() > 1 && Last == '-') Equation = Equation.substring(0, Equation.length() - 1) + '+';
                    InputText.setText(Equation); floatingPoint = false;
                }
            }
        });

        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40) {
                    char Last = 0;
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if (Last >= '0' && Last <= '9' || Last == ')')
                        Equation += "-";
                    else if (Last == '+') Equation = Equation.substring(0, Equation.length() - 1) + '-';
                    else if (Last == '÷' || Last == '×') {
                        Equation += '1';
                        Equation += '-';
                    }
                    else if(Last=='%')  {
                        Equation += '1';
                        Equation += '-';
                    }
                    else if(Last=='^')
                    {
                        Equation+="(0-";
                        Parentheses++;
                    }
                    else if (Equation.length() == 0 || Last == '(') {
                        if (Last == '(')
                            Equation += '0';
                        Equation += '-';
                    }
                    InputText.setText(Equation); floatingPoint = false;
                }
            }
        });


        buttonMul.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40) {
                    char Last = '×';
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if (Last >= '0' && Last <= '9' || Last == ')') Equation += "×";
                    else if (Last == '÷' || Last == '+'||Last=='^') Equation = Equation.substring(0, Equation.length() - 1) + '×';
                    else if(Last=='%') Equation = Equation.substring(0, Equation.length() - 1) + '×';
                    else if (Equation.length() > 1 && Last == '-') Equation = Equation.substring(0, Equation.length() - 1) + '×';
                    InputText.setText(Equation); floatingPoint = false;
                }
            }
        });
        buttonDivision.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40) {
                    char Last = '÷';
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if (Last >= '0' && Last <= '9' || Last == ')') Equation += "÷";
                    else if ( Last == '×' || Last == '+'||Last=='^') Equation = Equation.substring(0, Equation.length() - 1) + '÷';
                    else if(Last=='%') Equation = Equation.substring(0, Equation.length() - 1) + '÷';
                    else if (Equation.length() > 1 && Last == '-') Equation = Equation.substring(0, Equation.length() - 1) + '÷';
                    InputText.setText(Equation); floatingPoint = false;
                }
            }
        });

        buttonPow.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40) {
                    char Last = '^';
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if (Last >= '0' && Last <= '9' || Last == ')') Equation += "^";
                    else if (Last== '÷' ||Last == '×' || Last == '+') Equation = Equation.substring(0, Equation.length() - 1) + '^';
                    else if(Last=='%') Equation = Equation.substring(0, Equation.length() - 1) + '^';
                    else if (Equation.length() > 1 && Last == '-') Equation = Equation.substring(0, Equation.length() - 1) + '^';
                    InputText.setText(Equation); floatingPoint = false;
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (Equation.length() > 0) {
                    if (Equation.charAt(Equation.length() - 1) == '.') floatingPoint = false;
                    if (Equation.charAt(Equation.length() - 1) == '(') Parentheses--;
                    if (Equation.charAt(Equation.length() - 1) == ')') Parentheses++;
                    Equation = Equation.substring(0, Equation.length() - 1);
                    InputText.setText(Equation);
                    char Last;
                    if (Equation.length() > 0)
                        Last = Equation.charAt(Equation.length() - 1);
                    else
                        Last = '0';
                    if (Parentheses == 0 && Last >= '0' && Last <= '9') {
                        if (Equation.length() > 0) {
                            String Result = evaluate(Equation);
                            OutputText.setText(Result);
                        }
                        else OutputText.setText("");
                    }
                }
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Running = false;
                seconds = 0;
                InputText.setText("");
                Equation = "";
                OutputText.setText("");
                floatingPoint = false;
                Parentheses = 0;
                isPaused = true;
                buttonWhatsapp.setText("xⁿ");
                buttonSin.setText("sin");
                send = 0;
                wpsend = 0;
                buttonStopwatch.setText("►");
                buttonMod.setText("tan");
            }
        });
        button10.setOnClickListener(new View.OnClickListener()
        {@Override public void onClick(View v)
        {

            int i;
            boolean Point=false;

            for(i=Equation.length()-1;i>0;i--)
            {
                if(Equation.charAt(i)=='+'||Equation.charAt(i)=='-'||Equation.charAt(i)=='%'||Equation.charAt(i)=='('||Equation.charAt(i)=='×'||Equation.charAt(i)=='÷'||Equation.charAt(i)=='^')
                    break;
                if(Equation.charAt(i)=='.')
                {
                    Point=true;
                    break;
                }
            }
        {
            if(! Point)
        { Equation += "."; InputText.setText(Equation);;  } }
        }});


        openParentheses.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40) {
                    char Last;
                    if (Equation.length() > 0) {
                        Last = Equation.charAt(Equation.length() - 1);
                        if (Last >= '0' && Last <= '9' || Last == ')') {
                            Equation += '×';
                            Equation += "(";
                        } else if (Last != '.') {
                            Equation += "(";
                        }
                    } else {
                        Equation += "(";
                    }
                    InputText.setText(Equation); Parentheses++;
                }
            }
        });


        closeParentheses.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (InputText.getText().length() < 40 && Parentheses > 0)
                {
                    char Last = 0;
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if ((Last >= '0' && Last <= '9') || Last == ')') {
                        Equation += ")";
                        Parentheses--;
                        floatingPoint = false;
                    } else if (Last == '(') {
                        Equation += '0';
                        Equation += ')';
                        Parentheses--;
                        floatingPoint = false;
                    }
                    InputText.setText(Equation);
                    if (Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                    }
                }
            }
        });


        buttonEqual.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (Equation.length() > 0) {
                    char Last = 0;
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if (((Last >= '0' && Last <= '9') || Last == ')') && Parentheses == 0) {
                        String Result = evaluate(Equation);
                        OutputText.setText(Result);
                        Equation=Result;
                        InputText.setText(Equation);
                    }
                }
            }
        });
    }

    public void Timer(long millisInFuture) {
        final Vibrator vibe = (Vibrator) MainActivity2.this.getSystemService(Context.VIBRATOR_SERVICE);
        //Initialize a new CountDownTimer instance
        CountDownTimer timer = new CountDownTimer(millisInFuture * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (isPaused) {
                    cancel();
                } else {
                    OutputText.setText("" + millisUntilFinished / 1000);
                    timeRemaining = millisUntilFinished;
                }
            }
            public void onFinish() {
                //Do something when count down finished
                vibe.vibrate(1000);
                OutputText.setText("Time Over");
                buttonMod.setText("tan");
                isPaused = true;
            }
        } .start();
    }

    public String ToDecimal(String N,int B)
    {
        //System.out.println(N);
        boolean Negative=(N.charAt(0)=='-');
        if(N.charAt(0)=='-')
        N=N.substring(1,N.length());

        BigDecimal Base=new BigDecimal(B);
        BigDecimal Decimal=new BigDecimal(0);
        BigDecimal Digit=new BigDecimal(0);
        BigDecimal Two=new BigDecimal(0);
        int i=-1,n=N.length();

        while(++i<n)if(N.charAt(i)=='.') break;

        for(Two=Base.pow(i),i=0;i<n;i++)
        {
            //System.out.println(Two);
            if(N.charAt(i)=='.') continue;
            Digit=BigDecimal.valueOf(N.charAt(i)-(N.charAt(i)>'9'?'7':'0'));
            Two= Two.divide(Base);
            Decimal=Decimal.add(Digit.multiply(Two));
        }

        //System.out.println(Decimal.toString());
        return (Negative?"-":"")+Decimal.toString();
    }

    public String FromDecimal(String Decimal,int Base)
    {
        boolean Negative=(Decimal.charAt(0)=='-');
        if(Decimal.charAt(0)=='-') Decimal=Decimal.substring(1,Decimal.length());
        String NumberInt="",NumberFrac="";
        BigInteger DecimalInt=new BigInteger("0");
        BigDecimal DecimalFrac=new BigDecimal(0);
        BigDecimal Mod=new BigDecimal(0);
        BigInteger temp1=new BigInteger("1");
        BigDecimal temp2=new BigDecimal(1);
        BigDecimal temp=new BigDecimal(0);

        int i,n=Decimal.length();
        for(i=0;i<n&&Decimal.charAt(i)!='.';i++)
        {
            DecimalInt=DecimalInt.multiply(BigInteger.TEN);
            DecimalInt=DecimalInt.add(BigInteger.valueOf(Decimal.charAt(i)-'0'));
        }
        for(i++;i<n;i++)
        {
            temp2=temp2.multiply(BigDecimal.TEN);
            BigDecimal Digit=new BigDecimal(Decimal.charAt(i)-'0');
//            System.out.println("->");
//            System.out.println(Digit);
            DecimalFrac=DecimalFrac.add(Digit.divide(temp2));
           // System.out.println(DecimalFrac);
        }
        NumberInt=DecimalInt.toString(Base).toUpperCase();
//        System.out.println(DecimalInt);
//        System.out.println(NumberInt);
//            while(DecimalInt>0)
//            {
//                Mod=DecimalInt.remainder(Base);
//                if(Mod.compareTo(BigDecimal.valueOf(9))==1)
//                {
//                    if(Mod.compareTo(BigDecimal.TEN)>=0) {
//                        NumberInt+=(Mod.intValue()+55);
//                    }
//
//                    else {
//                        NumberInt+=(Mod.intValue()+'0');
//                    }
//                }
//                DecimalInt=DecimalInt.divide(Base);
//            }
//        }
//        reverse(NumberInt.begin(),NumberInt.end());


        //System.out.println(DecimalFrac);
        if(DecimalFrac.compareTo(BigDecimal.ZERO)>0)
        {
            NumberFrac+='.';
            //System.out.println(DecimalFrac);
            for(i=0;i<15&&DecimalFrac.compareTo(BigDecimal.ZERO)>0;i++)
            {
                temp=DecimalFrac.multiply(BigDecimal.valueOf(Base));

                Mod=temp.setScale(0,RoundingMode.FLOOR);

                DecimalFrac=temp.subtract(Mod);
//                System.out.println(temp);
//                System.out.println(Mod);
//                System.out.println(DecimalFrac);

                //System.out.println(Mod.intValue());
                //System.out.println(NumberFrac);
                if(Mod.compareTo(BigDecimal.valueOf(9))>0)
                {
                    NumberFrac+=(char)(Mod.intValue()+55);
                }
                else
                    {
                       NumberFrac+=(char)(Mod.intValue()+'0');
                    }
                System.out.println(NumberFrac);

            }
        }
        String Number=(Negative?"-":"")+NumberInt+NumberFrac;
        if(Number.length()>0) return Number;
        return "0";
    }

    public String evaluate(String expression) {
        System.out.println(expression);
        if (expression.length()==0) return "0";

        char[] tokens = expression.toCharArray();

        Stack<String> values = new Stack<String>();
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            if ((tokens[i] >= '0' && tokens[i] <= '9') || tokens[i] == '.') {
                String num = "";
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.'))
                    num += tokens[i++];
                i--;
                values.push(num);
                //System.out.println(values.peek());
            }
            else if (tokens[i] == '(') ops.push(tokens[i]);
            else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    String value1, value2;
                    if (!values.empty()) {
                        value1 = values.peek();
                        values.pop();
                    }
                    else value1 = "0";
                    if (!values.empty()) {
                        value2 = values.peek();
                        values.pop();
                    } else value2 = "0";
                    value1=ToDecimal(value1,From);
                    value2=ToDecimal(value2,From);
                    values.push(FromDecimal(applyOp(ops.pop(), value1, value2),From));
                }
                ops.pop();
            }

            else if (tokens[i] == '+' || tokens[i] == '-' ||
                    tokens[i] == '×' || tokens[i] == '÷'||tokens[i] == '%' || tokens[i]=='^') {

                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    String value1, value2;
                    if (!values.empty()) {
                        value1 = values.peek();
                        values.pop();
                    }
                    else value1 = "0";
                    if (!values.empty()) {
                        value2 = values.peek();
                        values.pop();
                    } else value2 = "0";
                    value1=ToDecimal(value1,From);
                    value2=ToDecimal(value2,From);
                    values.push(FromDecimal(applyOp(ops.pop(), value1, value2),From));
                    //System.out.println(values.peek());
                }
                ops.push(tokens[i]);
            }
        }

        while (!ops.empty()) {
            System.out.println(values);
            String value1, value2;
            if (!values.empty()) {
                value1 = values.peek();
                values.pop();
            }
            else value1 = "0";
            if (!values.empty()) {
                value2 = values.peek();
                values.pop();
            } else
                value2 = "0";
            value1=ToDecimal(value1,From);
            value2=ToDecimal(value2,From);
            System.out.println(value1);
            System.out.println(ops.peek());
            System.out.println(value2);

            values.push(FromDecimal(applyOp(ops.pop(), value1, value2),From));
        }
        String Decimal=ToDecimal(values.pop(),From);
        //System.out.println(FromDecimal(Decimal,To));
        return FromDecimal(Decimal,To);
    }
    public BigDecimal pow(String a,String b)
    {
        BigDecimal c=new BigDecimal(1);
        BigDecimal Num=new BigDecimal(a);
        BigDecimal Pow=new BigDecimal(b);
        //System.out.println(Num);
        //System.out.println(Pow);
        if(Pow.compareTo(BigDecimal.ZERO)<0)
        {
            while(Pow.compareTo(BigDecimal.ZERO)<0)
            {
                Pow=Pow.add(BigDecimal.ONE);
                c=c.divide(Num);
            }
        }

        else
        {

            while(Pow.compareTo(BigDecimal.ZERO)>0)
            {
                Pow=Pow.subtract(BigDecimal.ONE);
                c=c.multiply(Num);
            }
        }
        return c;
    }

    boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1=='^'||op1 == '×' || op1 == '÷'|| op1=='%') && (op2 == '+' || op2 == '-'))
            return false;
        if((op1=='^')&&(op2 == '×' || op2 == '÷'|| op2=='%'))
            return false;
        else
            return true;
    }

    String applyOp(char op, String b, String a) {
        BigDecimal A=new BigDecimal(a);
        BigDecimal B=new BigDecimal(b);
        String Result="0";
        switch (op) {
            case '+':
                Result=(A.add(B)).toString();
                break;
            case '-':
                Result=(A.subtract(B)).toString();
                break;
            case '×':
                Result=(A.multiply(B)).toString();
                break;
            case '^':
                Result=pow(a,b).toString();
                break;
            case '%':
                try {
                    Result = (A.remainder(B)).toString();
                }
                catch (ArithmeticException e)
                {

                }
                break;
            case '÷':
                try {
                    Result=(A.divide(B, 20, RoundingMode.HALF_UP)).toString();
                }
                catch (ArithmeticException e)
                {
                    OutputText.setText("Cannot be divided by 0");
                }
                break;
        }

        boolean Point=false;
        for(int i=0;i<Result.length();i++)
        {
            if(Result.charAt(i)=='.') Point=true;
        }


        if(Point)
        while(Result.charAt(Result.length()-1)=='0')
        {
            Result=Result.substring(0,Result.length()-1);
        }
        if(Result.charAt(Result.length()-1)=='.')
        {
            Result=Result.substring(0,Result.length()-1);
        }

        if(Result.length()>0) return Result;
        return "0";
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;



    public void sendWP(String phone, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + "+88" + phone + "&text=" + message));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    public void Call(final String phoneNumber) {
            startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
    }

    public void sendMessage(final String phoneNumber, final String message) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }


    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long hours = seconds / 36000;
                long minutes = ( (seconds / 10) % 3600) / 60;
                long secs = (seconds / 10) % 60;
                long msecs = seconds % 10;
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d:%d", hours, minutes, secs, msecs);
                OutputText.setText(time);
                if (Running) {
                    seconds++;
                    handler.postDelayed(this, 100);
                }
            }
        });
    }
}