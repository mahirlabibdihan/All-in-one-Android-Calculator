package com.mahirlabibdihan.diculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.AlarmClock;
import android.telephony.SmsManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Stack;


public class Main extends AppCompatActivity {
    Button buttonStopwatch, buttonSin, buttonTan, buttonWhatsapp;
    EditText OutputText;
    TextView InputText;

    int i = 0, parentheses = 0, send = 0, wpsend = 0, from = 10, to = 10;
    String Equation = "";
    boolean isPaused = true, Running = false;
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
        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            getActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        OutputText = (EditText) findViewById(R.id.edt2);
        InputText = (TextView) findViewById(R.id.edt1);
        buttonTan = (Button) findViewById(R.id.buttontan);
        buttonStopwatch = (Button) findViewById(R.id.buttonsw);
        buttonSin = (Button) findViewById(R.id.buttonsin);
        buttonWhatsapp = (Button) findViewById(R.id.buttonwp);

        OutputText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {

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
    }


    private String ToDecimal(String N, int B) {
        boolean Negative = (N.charAt(0) == '-');
        if (N.charAt(0) == '-')
            N = N.substring(1, N.length());

        BigDecimal Base = new BigDecimal(B);
        BigDecimal Decimal = new BigDecimal(0);
        BigDecimal Digit = new BigDecimal(0);
        BigDecimal Two = new BigDecimal(0);
        int i = -1, n = N.length();

        while (++i < n) if (N.charAt(i) == '.') break;

        for (Two = Base.pow(i), i = 0; i < n; i++) {
            if (N.charAt(i) == '.') continue;
            Digit = BigDecimal.valueOf(N.charAt(i) - (N.charAt(i) > '9' ? '7' : '0'));
            Two = Two.divide(Base);
            Decimal = Decimal.add(Digit.multiply(Two));
        }
        return (Negative ? "-" : "") + Decimal.toString();
    }

    private String FromDecimal(String Decimal, int Base) {
        boolean Negative = (Decimal.charAt(0) == '-');
        if (Decimal.charAt(0) == '-') Decimal = Decimal.substring(1, Decimal.length());
        String NumberInt = "", NumberFrac = "";
        BigInteger DecimalInt = new BigInteger("0");
        BigDecimal DecimalFrac = new BigDecimal(0);
        BigDecimal Mod = new BigDecimal(0);
        BigInteger temp1 = new BigInteger("1");
        BigDecimal temp2 = new BigDecimal(1);
        BigDecimal temp = new BigDecimal(0);

        int i, n = Decimal.length();
        for (i = 0; i < n && Decimal.charAt(i) != '.'; i++) {
            DecimalInt = DecimalInt.multiply(BigInteger.TEN);
            DecimalInt = DecimalInt.add(BigInteger.valueOf(Decimal.charAt(i) - '0'));
        }
        for (i++; i < n; i++) {
            temp2 = temp2.multiply(BigDecimal.TEN);
            BigDecimal Digit = new BigDecimal(Decimal.charAt(i) - '0');
            DecimalFrac = DecimalFrac.add(Digit.divide(temp2));
        }
        NumberInt = DecimalInt.toString(Base).toUpperCase();

        if (DecimalFrac.compareTo(BigDecimal.ZERO) > 0) {
            NumberFrac += '.';
            for (i = 0; i < 15 && DecimalFrac.compareTo(BigDecimal.ZERO) > 0; i++) {
                temp = DecimalFrac.multiply(BigDecimal.valueOf(Base));
                Mod = temp.setScale(0, RoundingMode.FLOOR);
                DecimalFrac = temp.subtract(Mod);

                if (Mod.compareTo(BigDecimal.valueOf(9)) > 0) {
                    NumberFrac += (char) (Mod.intValue() + 55);
                } else {
                    NumberFrac += (char) (Mod.intValue() + '0');
                }
            }
        }
        String Number = (Negative ? "-" : "") + NumberInt + NumberFrac;
        if (Number.length() > 0) return Number;
        return "0";
    }

    private BigDecimal Factorial(BigDecimal n) {
        BigDecimal i = new BigDecimal(1);
        BigDecimal Fact = new BigDecimal(1);
        for (; i.compareTo(n) < 1; ) {
            Fact = Fact.multiply(i);
            i = i.add(BigDecimal.ONE);
        }
        return Fact;
    }

    private BigDecimal pow(String a, String b) {
        BigDecimal c = new BigDecimal(1);
        BigDecimal Num = new BigDecimal(a);
        BigDecimal Pow = new BigDecimal(b);

        if (Pow.compareTo(BigDecimal.ZERO) < 0) {
            while (Pow.compareTo(BigDecimal.ZERO) < 0) {
                Pow = Pow.add(BigDecimal.ONE);
                c = c.divide(Num);
            }
        } else {
            while (Pow.compareTo(BigDecimal.ZERO) > 0) {
                Pow = Pow.subtract(BigDecimal.ONE);
                c = c.multiply(Num);
            }
        }
        return c;
    }

    public String calculate(String expression, int from, int to) {
        if (expression.length() == 0) return "0";

        char[] tokens = expression.toCharArray();
        Stack<String> values = new Stack<String>();
        Stack<Character> ops = new Stack<Character>();

        for (int i = 0; i < tokens.length; i++) {
            if ((tokens[i] >= '0' && tokens[i] <= '9') || (tokens[i] >= 'A' && tokens[i] <= 'F') || tokens[i] == '.') {
                String num = "";
                while (i < tokens.length && ((tokens[i] >= '0' && tokens[i] <= '9') || (tokens[i] >= 'A' && tokens[i] <= 'F') || tokens[i] == '.'))
                    num += tokens[i++];
                i--;
                values.push(num);
            } else if (tokens[i] == '(') ops.push(tokens[i]);
            else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    String value1, value2;
                    if (!values.empty()) {
                        value1 = values.peek();
                        values.pop();
                    } else value1 = "0";
                    if (!values.empty()) {
                        value2 = values.peek();
                        values.pop();
                    } else value2 = "0";
                    value1 = ToDecimal(value1, from);
                    value2 = ToDecimal(value2, from);
                    values.push(FromDecimal(applyOp(ops.pop(), value1, value2), from));
                }
                ops.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '×' || tokens[i] == '÷' || tokens[i] == '%' || tokens[i] == '^') {

                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    String value1, value2;
                    if (!values.empty()) {
                        value1 = values.peek();
                        values.pop();
                    } else value1 = "0";
                    if (!values.empty()) {
                        value2 = values.peek();
                        values.pop();
                    } else value2 = "0";
                    value1 = ToDecimal(value1, from);
                    value2 = ToDecimal(value2, from);
                    values.push(FromDecimal(applyOp(ops.pop(), value1, value2), from));
                }
                ops.push(tokens[i]);
            } else if (tokens[i] == '!') {
                while (!ops.empty() && hasPrecedence(tokens[i], ops.peek())) {
                    String value = values.peek();
                    values.pop();
                    values.push(applyOp(ops.peek(), value, value));
                    ops.pop();
                }
                ops.push(tokens[i]);
            }
        }


        while (!ops.empty()) {
            String value1, value2;
            if (!values.empty()) {
                value1 = values.peek();
                values.pop();
            } else value1 = "0";
            if (!values.empty()) {
                value2 = values.peek();
                values.pop();
            } else
                value2 = "0";
            value1 = ToDecimal(value1, from);
            value2 = ToDecimal(value2, from);
            values.push(FromDecimal(applyOp(ops.pop(), value1, value2), from));
        }
        String Decimal = ToDecimal(values.pop(), from);
        return FromDecimal(Decimal, to);
    }

    boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        if ((op1 == '^' || op1 == '!' || op1 == '×' || op1 == '÷' || op1 == '%') && (op2 == '+' || op2 == '-'))
            return false;
        if ((op1 == '!' || op1 == '^') && (op2 == '×' || op2 == '÷' || op2 == '%'))
            return false;
        if (op1 == '!' && op2 == '^')
            return false;
        else
            return true;
    }

    String applyOp(char op, String b, String a) {
        BigDecimal A = new BigDecimal(a);
        BigDecimal B = new BigDecimal(b);
        String Result = "0";
        switch (op) {
            case '+':
                Result = (A.add(B)).toString();
                break;
            case '-':
                Result = (A.subtract(B)).toString();
                break;
            case '×':
                Result = (A.multiply(B)).toString();
                break;
            case '^':
                Result = pow(a, b).toString();
                break;
            case '!':
                Result = Factorial(B).toString();
                break;
            case '%':
                try {
                    Result = (A.remainder(B)).toString();
                } catch (ArithmeticException e) {
                    OutputText.setText("Cannot be mod by 0");
                }
                break;
            case '÷':
                try {
                    Result = (A.divide(B, 20, RoundingMode.HALF_UP)).toString();
                } catch (ArithmeticException e) {
                    OutputText.setText("Cannot be divided by 0");
                }
                break;
        }


        boolean Point = false;
        for (int i = 0; i < Result.length(); i++) {
            if (Result.charAt(i) == '.') Point = true;
        }

        if (Point) {
            while (Result.charAt(Result.length() - 1) == '0') {
                Result = Result.substring(0, Result.length() - 1);
            }
        }

        if (Result.charAt(Result.length() - 1) == '.') {
            Result = Result.substring(0, Result.length() - 1);
        }

        if (Result.length() > 0) return Result;
        return "0";
    }

    private void sendWP(String phone, String message) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + phone + "&text=" + message));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Call(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void sendMessage(final String phoneNumber, final String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long hours = seconds / 36000;
                long minutes = ((seconds / 10) % 3600) / 60;
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

    private void timer(long millisInFuture) {
        final Vibrator vibe = (Vibrator) Main.this.getSystemService(Context.VIBRATOR_SERVICE);
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
                buttonTan.setText("tan");
                isPaused = true;
            }
        }.start();
    }

    public void equal(View view) {
        if (Equation.length() > 0) {
            char Last = 0;
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (((Last >= '0' && Last <= '9') || Last == ')') && parentheses == 0) {
                String Result = calculate(Equation, from, to);
                OutputText.setText(Result);
                Equation = Result;
                InputText.setText(Equation);
            }
        }
    }

    public void from(View view) {
        Button buttonFrom = (Button) view;
        if (buttonFrom.getText().toString().equals("Bin")) {
            buttonFrom.setText("Oct");
            from = 8;
        } else if (buttonFrom.getText().toString().equals("Oct")) {
            buttonFrom.setText("Dec");
            from = 10;
        } else if (buttonFrom.getText().toString().equals("Dec")) {
            buttonFrom.setText("Hex");
            from = 16;
        } else if (buttonFrom.getText().toString().equals("Hex")) {
            buttonFrom.setText("Bin");
            from = 2;
        }
        Running = false;
        seconds = 0;
        InputText.setText("");
        Equation = "";
        OutputText.setText("");
        parentheses = 0;
        isPaused = true;
        buttonWhatsapp.setText("►");
        buttonSin.setText("sin");
        send = 0;
        wpsend = 0;
        buttonStopwatch.setText("►");
        buttonTan.setText("tan");
    }

    public void addDigit(View view) {
        final Vibrator vibe = (Vibrator) Main.this.getSystemService(Context.VIBRATOR_SERVICE);
        Button b = (Button) view;
        vibe.vibrate(80);
        if (InputText.getText().length() < 40 && from > Integer.parseInt((String) b.getText())) {
            char Last=0;
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if(Last=='!'||Last==')') return;
            Equation += (String) b.getText();
            InputText.setText(Equation);
            if (parentheses == 0) {
                String Result = calculate(Equation, from, to);
                OutputText.setText(Result);
            }
        }
    }

    public void stopWatch(View view) {
        Button buttonStopwatch = (Button) view;
        if (Running) {
            Running = false;
            buttonStopwatch.setText("►");
        } else {
            Running = true;
            buttonStopwatch.setText("❚❚");
            runTimer();
        }
    }

    public void to(View view) {
        Button buttonTo = (Button) view;
        if (buttonTo.getText().toString().equals("Bin")) {
            buttonTo.setText("Oct");
            String Result = calculate(OutputText.getText().toString(), to, 8);
            OutputText.setText(Result);
            to = 8;
        } else if (buttonTo.getText().toString().equals("Oct")) {
            buttonTo.setText("Dec");
            String Result = calculate(OutputText.getText().toString(), to, 10);
            OutputText.setText(Result);
            to = 10;
        } else if (buttonTo.getText().toString().equals("Dec")) {
            buttonTo.setText("Hex");
            String Result = calculate(OutputText.getText().toString(), to, 16);
            OutputText.setText(Result);
            to = 16;
        } else if (buttonTo.getText().toString().equals("Hex")) {
            buttonTo.setText("Bin");
            String Result = calculate(OutputText.getText().toString(), to, 2);
            OutputText.setText(Result);
            to = 2;
        }
        Running = false;
        seconds = 0;
        isPaused = true;
        buttonWhatsapp.setText("►");
        buttonSin.setText("sin");
        send = 0;
        wpsend = 0;
        buttonStopwatch.setText("►");
        buttonTan.setText("tan");
    }

    public void mod(View view) {
        int hour = 0, minute = 0, i;
        for (i = 0; i < Equation.length() && Equation.charAt(i) != '.'; i++) {
            hour *= 10;
            hour += Equation.charAt(i) - 48;
        }

        if (i < Equation.length()) {
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
            } else {
                if (InputText.getText().length() < 40) {
                    char Last = '%';
                    if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                    if (Last >= '0' && Last <= '9' || Last == ')') Equation += "%";
                    else if (Last == '÷' || Last == '×' || Last == '+' || Last == '^')
                        Equation = Equation.substring(0, Equation.length() - 1) + '%';
                    else if (Equation.length() > 1 && Last == '-')
                        Equation = Equation.substring(0, Equation.length() - 1) + '%';
                    InputText.setText(Equation);
                }
            }
        } else {
            if (InputText.getText().length() < 40) {
                char Last = '%';
                if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
                if (Last >= '0' && Last <= '9' || Last == ')') Equation += "%";
                else if (Last == '÷' || Last == '×' || Last == '+' || Last == '^')
                    Equation = Equation.substring(0, Equation.length() - 1) + '%';
                else if (Equation.length() > 1 && Last == '-')
                    Equation = Equation.substring(0, Equation.length() - 1) + '%';
                InputText.setText(Equation);
            }
        }
    }

    public void whatsApp(View v) {
        wpsend++;
        if (wpsend > 1) {
            wpsend = 0;
            sendWP(InputText.getText() + "", OutputText.getText() + "");
            buttonWhatsapp.setText("►");
            OutputText.setEnabled(false);
            OutputText.setFocusable(false);
            OutputText.setFocusableInTouchMode(false);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(OutputText.getWindowToken(), 0);
        } else {
            OutputText.setEnabled(true);
            OutputText.setFocusable(true);
            OutputText.setFocusableInTouchMode(true);
            OutputText.setText("");
            buttonWhatsapp.setText("➤");
            OutputText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void sin(View v) {
        if (ContextCompat.checkSelfPermission(Main.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            OutputText.setText("Permission needed");
            ActivityCompat.requestPermissions(Main.this, new String[]{Manifest.permission.SEND_SMS}, 1);
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
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(OutputText.getWindowToken(), 0);
        } else {
            OutputText.setEnabled(true);
            OutputText.setFocusable(true);
            OutputText.setFocusableInTouchMode(true);
            OutputText.setText("");
            OutputText.requestFocus();
            buttonSin.setText("➤");
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public void up(View v) {
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

    public void down(View v) {
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

    public void tan(View v) {
        if (isPaused) {
            isPaused = !isPaused;
            long time;
            try {
                time = Long.parseLong(OutputText.getText() + "");
                buttonTan.setText("❚❚");
                timer(time);
            } catch (NumberFormatException e) {
                isPaused = !isPaused;
                OutputText.setText("");
                buttonTan.setText("tan");
            }
        } else {
            isPaused = !isPaused;
            buttonTan.setText("tan");
        }
    }

    public void cos(View v) {
        if (ContextCompat.checkSelfPermission(Main.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            OutputText.setText("Permission needed");
            ActivityCompat.requestPermissions(Main.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        } else {
            Call(InputText.getText() + " ");
        }
    }

    public void add(View v) {
        if (InputText.getText().length() < 40) {
            char Last = '+';
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (Last >= '0' && Last <= '9' || Last == ')' || Last == '!') Equation += "+";
            else if (Last == '÷' || Last == '×' || Last == '^')
                Equation = Equation.substring(0, Equation.length() - 1) + '+';
            else if (Last == '%') Equation = Equation.substring(0, Equation.length() - 1) + '+';
            else if (Equation.length() > 1 && Last == '-')
                Equation = Equation.substring(0, Equation.length() - 1) + '+';
            InputText.setText(Equation);
        }
    }

    public void sub(View v) {
        if (InputText.getText().length() < 40) {
            char Last = 0;
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (Last >= '0' && Last <= '9' || Last == ')' || Last == '!')
                Equation += "-";
            else if (Last == '+') Equation = Equation.substring(0, Equation.length() - 1) + '-';
            else if (Last == '÷' || Last == '×') {
                Equation += '1';
                Equation += '-';
            } else if (Last == '%') {
                Equation += '1';
                Equation += '-';
            } else if (Last == '^') {
                Equation += "(0-";
                parentheses++;
            } else if (Equation.length() == 0 || Last == '(') {
                if (Last == '(')
                    Equation += '0';
                Equation += '-';
            }
            InputText.setText(Equation);
        }
    }

    public void mul(View v) {
        if (InputText.getText().length() < 40) {
            char Last = '×';
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (Last >= '0' && Last <= '9' || Last == ')' || Last == '!') Equation += "×";
            else if (Last == '÷' || Last == '+' || Last == '^')
                Equation = Equation.substring(0, Equation.length() - 1) + '×';
            else if (Last == '%') Equation = Equation.substring(0, Equation.length() - 1) + '×';
            else if (Equation.length() > 1 && Last == '-')
                Equation = Equation.substring(0, Equation.length() - 1) + '×';
            InputText.setText(Equation);
        }
    }

    public void div(View v) {
        if (InputText.getText().length() < 40) {
            char Last = '÷';
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (Last >= '0' && Last <= '9' || Last == ')' || Last == '!') Equation += "÷";
            else if (Last == '×' || Last == '+' || Last == '^')
                Equation = Equation.substring(0, Equation.length() - 1) + '÷';
            else if (Last == '%') Equation = Equation.substring(0, Equation.length() - 1) + '÷';
            else if (Equation.length() > 1 && Last == '-')
                Equation = Equation.substring(0, Equation.length() - 1) + '÷';
            InputText.setText(Equation);
        }
    }

    public void factorial(View view) {
        if (InputText.getText().length() < 40) {
            char Last = '!';
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (Last >= '0' && Last <= '9' || Last == ')' || Last == '!') Equation += "!";
            else if (Last == '÷' || Last == '×' || Last == '+')
                Equation = Equation.substring(0, Equation.length() - 1) + '!';
            else if (Last == '%' || Last == '^')
                Equation = Equation.substring(0, Equation.length() - 1) + '!';
            else if (Equation.length() > 1 && Last == '-')
                Equation = Equation.substring(0, Equation.length() - 1) + '!';
            InputText.setText(Equation);
            if (parentheses == 0) {
                String Result = calculate(Equation, from, to);
                OutputText.setText(Result);
            }
        }
    }

    public void power(View view) {
        if (InputText.getText().length() < 40) {
            char Last = '^';
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if (Last >= '0' && Last <= '9' || Last == ')' || Last == '!') Equation += "^";
            else if (Last == '÷' || Last == '×' || Last == '+')
                Equation = Equation.substring(0, Equation.length() - 1) + '^';
            else if (Last == '%') Equation = Equation.substring(0, Equation.length() - 1) + '^';
            else if (Equation.length() > 1 && Last == '-')
                Equation = Equation.substring(0, Equation.length() - 1) + '^';
            InputText.setText(Equation);
        }
    }

    public void delete(View view) {
        if (Equation.length() > 0) {
            if (Equation.charAt(Equation.length() - 1) == '(') parentheses--;
            if (Equation.charAt(Equation.length() - 1) == ')') parentheses++;
            Equation = Equation.substring(0, Equation.length() - 1);
            InputText.setText(Equation);
            char Last;
            if (Equation.length() > 0)
                Last = Equation.charAt(Equation.length() - 1);
            else
                Last = '0';
            if (parentheses == 0 && Last >= '0' && Last <= '9') {
                if (Equation.length() > 0) {
                    String Result = calculate(Equation, from, to);
                    OutputText.setText(Result);
                } else OutputText.setText("");
            }
        }
    }

    public void reset(View v) {
        Running = false;
        seconds = 0;
        InputText.setText("");
        Equation = "";
        OutputText.setText("");
        parentheses = 0;
        isPaused = true;
        buttonWhatsapp.setText("►");
        buttonSin.setText("sin");
        send = 0;
        wpsend = 0;
        buttonStopwatch.setText("►");
        buttonTan.setText("tan");
    }

    public void addPoint(View v) {
        int i;
        boolean Point = false;

        for (i = Equation.length() - 1; i > 0; i--) {
            if (Equation.charAt(i) == '+' || Equation.charAt(i) == '-' || Equation.charAt(i) == '%' || Equation.charAt(i) == '(' || Equation.charAt(i) == '×' || Equation.charAt(i) == '÷' || Equation.charAt(i) == '^')
                break;
            if (Equation.charAt(i) == '.' || Equation.charAt(i) == '!') {
                Point = true;
                break;
            }
        }
        if (!Point) {
            Equation += ".";
            InputText.setText(Equation);
        }
    }

    public void openParentheses(View view) {
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
            InputText.setText(Equation);
            parentheses++;
        }
    }

    public void closeParentheses(View view) {
        if (InputText.getText().length() < 40 && parentheses > 0) {
            char Last = 0;
            if (Equation.length() > 0) Last = Equation.charAt(Equation.length() - 1);
            if ((Last >= '0' && Last <= '9') || Last == ')') {
                Equation += ")";
                parentheses--;
            } else if (Last == '(') {
                Equation += '0';
                Equation += ')';
                parentheses--;
            }
            InputText.setText(Equation);
            if (parentheses == 0) {
                String Result = calculate(Equation, from, to);
                OutputText.setText(Result);
            }
        }
    }
}