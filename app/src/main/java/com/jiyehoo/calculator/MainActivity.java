package com.jiyehoo.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiyehoo.calculator.util.Arith;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "CalculatorActivity";
    private TextView mTvResult;

    //操作符
    private String operator = "";
    //第一个数字
    private String firstNum = "";
    //第二个数字
    private String secondNum = "";
    //结果
    private String result = "";
    //显示的文本内容
    private String showText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setListener();

    }

    //找到控件，设置按钮监听
    private void setListener() {
        mTvResult = findViewById(R.id.tv_result);

        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_multiply).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);

        findViewById(R.id.btn_seven).setOnClickListener(this);
        findViewById(R.id.btn_eight).setOnClickListener(this);
        findViewById(R.id.btn_nine).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);

        findViewById(R.id.btn_four).setOnClickListener(this);
        findViewById(R.id.btn_five).setOnClickListener(this);
        findViewById(R.id.btn_six).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);

        findViewById(R.id.btn_one).setOnClickListener(this);
        findViewById(R.id.btn_two).setOnClickListener(this);
        findViewById(R.id.btn_three).setOnClickListener(this);
        findViewById(R.id.btn_sqrt).setOnClickListener(this);

        findViewById(R.id.btn_zero).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String inputText;
        int resId = v.getId();

        //获取键盘输入，放入到inputText，里面存的是单个字符
        inputText = ((TextView) v).getText().toString();
        Log.d(TAG, "resId=" + resId + ", inputString=" + inputText);

        //操作符

        switch (v.getId()) {
            case R.id.btn_clear:
                //点击C,清空
                clearAll();
                break;

            case R.id.btn_cancel:
                //点击CE，取消
                if (operator.equals("")) {
                    //没有操作符，删第一个数字
                    if (firstNum.length() == 1) {
                        //如果是个位数，设置为0，注意不能为0
                        firstNum = "0";
                    } else if (firstNum.length() > 0) {
                        //如果不是个位数，减少一位
                        firstNum = firstNum.substring(0, firstNum.length() - 1);
                    } else {
                        //如果第一个数字没有，提示
                        Toast.makeText(this, "没有数字了！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //显示删了一位之后的内容
                    showText = firstNum;
                    mTvResult.setText(showText);
                } else {
                    //有操作符，删第二个数字
                    if (secondNum.length() == 1) {
                        //如果是个位数，设置为空
                        secondNum = "";
                    } else if (secondNum.length() > 0) {
                        //如果不是个位数，减少一位
                        secondNum = secondNum.substring(0, secondNum.length() - 1);
                    } else {
                        //如果第一个数字没有，提示
                        Toast.makeText(this, "没有数字了！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //显示删了一位之后的内容
                    showText = showText.substring(0, showText.length() - 1);
                    mTvResult.setText(showText);
                }
                break;

            case R.id.btn_equal:
                //点击了等于
                equalNum(inputText);
                break;

            case R.id.btn_plus:
            case R.id.btn_minus:
            case R.id.btn_multiply:
            case R.id.btn_divide:
                //点击了加减乘除
                if (firstNum.length() <= 0) {
                    //没有第一个数字不能输入运算符
                    Toast.makeText(this, "请输入第一个运算数字!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (firstNum.length() >= 0 && secondNum.equals("") && operator.length() > 0) {
                    Toast.makeText(this, "两个运算符了", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (operator.length() == 0 || operator.equals("=") || operator.equals("√")) {
                    //没有运算符，载入，显示
                    operator = inputText;
                    showText += operator;
                    mTvResult.setText(showText);
                } else {
//                    有了两个数，一个运算符，没有按下等于
//                    Toast.makeText(this, "请输入数字（加减乘除）", Toast.LENGTH_SHORT).show();
//                    return;
                    equalNum(inputText);
                    operator = inputText;
                    showText += operator;
                    secondNum = "";
                    mTvResult.setText(showText);

                }
                break;

            case R.id.btn_sqrt:
                //点击了根号
                if (firstNum.length() <= 0) {
                    //对第一个数字操作，不能为空
                    Toast.makeText(this, "请输入开根号的数字!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!secondNum.equals("")) {
                    //第二个数字不为空，则计算出结果，再开根号
                    equalNum(inputText);
                }
                if (Double.parseDouble(firstNum) < 0) {
                    //判断不能为负数
                    Toast.makeText(this, "开根号的数字不能小于0", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //开始计算根号
                    result = String.valueOf(Math.sqrt(Double.parseDouble(firstNum)));
                    //显示
                    firstNum = result;
                    secondNum = "";
                    operator = inputText;
                    showText += "√=" + result;
                    mTvResult.setText(showText);
                    Log.d(TAG, "result=" + result + ",first=" + firstNum + ",operator=" + operator);
                }
                break;

            default:
                //点击了其他按键（数字、小数点）
                if (operator.equals("=")) {
                    //如果上次按的是等于，则清空
                    operator = "";
                    firstNum = "";
                    showText = "";
                }
                if (resId == R.id.btn_dot) {
                    //点击了小数点
                    inputText = ".";
                }
                if (operator.equals("")) {
                    //没有运算符，则拼接第一个数字
                    if (firstNum.contains(".") && inputText.equals(".")) {
                        //防止一个数字有两个小数点
                        return;
                    }
                    firstNum += inputText;
                } else {
                    //有操作符，则拼接第二个数字
                    if (secondNum.contains(".") && inputText.equals(".")) {
                        //防止一个数字有两个小数点
                        return;
                    }
                    secondNum += inputText;
                }
                //显示
                showText += inputText;
                mTvResult.setText(showText);
                return;
        }
    }

    //初始化清空
    private void clearAll() {
        showText = "";
        mTvResult.setText(showText);
        operator = "";
        firstNum = "";
        secondNum = "";
        result = "";
    }

    //开始计算
    private boolean calculate() {
        Log.d(TAG, "first=" + firstNum + ",second=" + secondNum);
        if (operator.equals("＋")) {
            //加
            result = Arith.add(firstNum, secondNum);
        } else if (operator.equals("－")) {
            //减
            result = Arith.sub(firstNum, secondNum);
        } else if (operator.equals("×")) {
            //乘
            result = Arith.mul(firstNum, secondNum);
        } else if (operator.equals("÷")) {
            //除
            if (Double.parseDouble(secondNum) == 0) {
                //被除数是0
                Toast.makeText(this, "被除数不能为0!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                //开始计算除法
                result = Arith.div(firstNum, secondNum);
            }
        }
        //Log
        Log.d(TAG, "result=" + result);
        //计算完成，为继续计算做准备
        firstNum = result;
//        secondNum = "";

        //计算成功
        return true;
    }

    //点击了等于
    private void equalNum (String inputText) {
        if (operator.equals("=") || operator.length() == 0) {
            //没有运算符
            Toast.makeText(this, "没有运算符!", Toast.LENGTH_SHORT).show();
            return;
        } else if (secondNum.length() <= 0) {
            //没有第二个数字
            Toast.makeText(this, "没有第二个数!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (calculate()) {
            //计算成功
            operator = "";
            showText += "=" + result;
            mTvResult.setText(showText);
            secondNum = "";
        } else {
            //计算失败
            return;
        }
    }
}