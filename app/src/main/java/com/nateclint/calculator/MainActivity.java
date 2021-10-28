package com.nateclint.calculator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView textViewPrice,
      textView20, textView10, textView5, textView1,
      textView25c, textView10c, textView5c, textView1c;

    private String price = "0";

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPrice = findViewById(R.id.textViewPrice);
        textView20 = findViewById(R.id.textView20);
        textView10 = findViewById(R.id.textView10);
        textView5 = findViewById(R.id.textView5);
        textView1 = findViewById(R.id.textView1);
        textView25c = findViewById(R.id.textView25c);
        textView10c = findViewById(R.id.textView10c);
        textView5c = findViewById(R.id.textView5c);
        textView1c = findViewById(R.id.textView1c);

        ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(
          findViewById(R.id.buttonC), findViewById(R.id.buttonCE), findViewById(R.id.buttonBS),
          findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9),
          findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
          findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
          findViewById(R.id.button0), findViewById(R.id.button00), findViewById(R.id.buttonDot)
        ));

        for (Button button : buttons) {
            button.setOnClickListener(this::onClick);
        }

        updateView();
    }

    @SuppressLint("NonConstantResourceId")
    private void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.buttonC:
                price = "0";
                break;
            case R.id.buttonCE:
                break;
            case R.id.buttonBS:
                if (price.length() > 1) {
                    price = price.substring(0, price.length() - 1);
                } else {
                    price = "0";
                }
                break;
            case R.id.button7:
            case R.id.button8:
            case R.id.button9:
            case R.id.button4:
            case R.id.button5:
            case R.id.button6:
            case R.id.button1:
            case R.id.button2:
            case R.id.button3:
            case R.id.button0:
            case R.id.button00:
                if (price.equals("0")) {
                    price = "";
                }
                price += ((Button) view).getText();
                if (price.contains(".")) {
                    List<String> parts = Arrays.asList(price.split("\\."));
                    if (parts.get(1).length() > 3) {
                        price = parts.get(0) + "." + parts.get(1).substring(0, 3);
                    }
                }
                break;
            case R.id.buttonDot:
                if (!price.contains(".")) {
                    price += ((Button) view).getText();
                }
                break;

            default:
                Log.w(TAG, "onClick: Unknown button " + ((Button) view).getText());
                break;
        }

        updateView();
    }

    private void updateView() {
        textViewPrice.setText(String.format(Locale.ROOT, "$%s", price));

        List<String> parts = Arrays.asList(price.split("\\."));
        int numberPart = Integer.parseInt(parts.get(0)),
          fractionalPart = Integer.parseInt(parts.size() > 1 ? parts.get(1) : "0");
        int _20, _10, _5, _1, _25c, _10c, _5c, _1c;

        _20 = count(numberPart, 20);
        numberPart -= _20 * 20;
        _10 = count(numberPart, 10);
        numberPart -= _10 * 10;
        _5 = count(numberPart, 5);
        numberPart -= _5 * 5;
        _1 = count(numberPart, 1);

        while (fractionalPart % 10 == 0 && fractionalPart > 0) {
            fractionalPart /= 10;
        }
        _25c = count(fractionalPart, 25);
        fractionalPart -= _25c * 25;
        _10c = count(fractionalPart, 10);
        fractionalPart -= _10c * 10;
        _5c = count(fractionalPart, 5);
        fractionalPart -= _5c * 5;
        _1c = count(fractionalPart, 1);

        textView20.setText(String.format(Locale.ROOT, "$20 x %d", _20));
        textView10.setText(String.format(Locale.ROOT, "$10 x %d", _10));
        textView5.setText(String.format(Locale.ROOT, "$5 x %d", _5));
        textView1.setText(String.format(Locale.ROOT, "$1 x %d", _1));
        textView25c.setText(String.format(Locale.ROOT, "25¢ x %d", _25c));
        textView10c.setText(String.format(Locale.ROOT, "10¢ x %d", _10c));
        textView5c.setText(String.format(Locale.ROOT, "5¢ x %d", _5c));
        textView1c.setText(String.format(Locale.ROOT, "1¢ x %d", _1c));
    }

    private int count(int total, int threshold) {
        int count = 0;
        while (total >= threshold) {
            total -= threshold;
            count++;
        }
        return count;
    }
}