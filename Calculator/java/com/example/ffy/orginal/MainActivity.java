package com.example.ffy.orginal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {


    private TextView equationText,resultText;
    private Button buttons[] = new Button[26];

    private CheckInput checkInput;
    private CounterByEquation counterByEquation;

    //布局中所有按钮的ID
    private int buttonIds[] = new int[]{
            R.id.CE,R.id.BACKSPACE,R.id.Remainder,R.id.Involution,R.id.LBracket,R.id.RBracket,R.id.Factorial,
            R.id.Reciprocal,R.id.P,R.id.E, R.id.Divide,R.id.N7,R.id.N8,R.id.N9,R.id.Multiply,R.id.N4,
            R.id.N5,R.id.N6,R.id.Subtract,R.id.N1,R.id.N2,R.id.N3,R.id.Equal,R.id.Zero,R.id.Point, R.id.Add
    };
    //等式
    private String equation = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();  //初始化控件
    }
    /*
    * 点击计算器键盘
    *
    * */
    @Override
    public void onClick(View v) {

        int id = v.getId();
        Button button = (Button)findViewById(id);
        String text = button.getText().toString();
        switch (id){
            /*
            * 按数字按钮
            * */
            case R.id.N1:
            case R.id.N2:
            case R.id.N3:
            case R.id.N4:
            case R.id.N5:
            case R.id.N6:
            case R.id.N7:
            case R.id.N8:
            case R.id.N9:
            case R.id.Zero:
                checkInput.setEquation(equation);
                if(checkInput.checkNumberInput()){
                    equation = checkInput.getEquation();
                    equation += text;
                }
                break;
            /*
            * 点击 + - * /
            * */
            case R.id.Add:
            case R.id.Subtract:
            case R.id.Multiply:
            case R.id.Divide:
                checkInput.setEquation(equation);
                if(checkInput.checkOperationsInput()){
                    equation = checkInput.getEquation();
                    equation += text;
                }
                break;
             /*
             * 点击退格键
             *
             * */
            case R.id.BACKSPACE:
                checkInput.setEquation(equation);
                checkInput.backSpace();
                equation = checkInput.getEquation();
                break;
            /*
            * 点击 x^y
            * */
            case R.id.Involution:
                checkInput.setEquation(equation);
                if(checkInput.checkInvolutionInput())
                    equation+="^";
                break;
            /*
            * 倒数
            * */
            case R.id.Reciprocal:
                checkInput.setEquation(equation);
                if(checkInput.checkReciprocalInput())
                    equation+="^(-1)";
                break;
            /*
            * 左括号
            * */
            case R.id.LBracket:
                checkInput.setEquation(equation);
                if(checkInput.checkLBracketInput())
                    equation+="(";
                break;
            /*
            * 右括号
            * */
            case R.id.RBracket:
                checkInput.setEquation(equation);
                if(checkInput.checkRBracketInput())
                    equation+=')';
                break;
            /*
            * 取余
            * */
            case R.id.Remainder:
                checkInput.setEquation(equation);
                if(checkInput.checkRemainderInput())
                    equation+='%';
                break;
            /*
            * 求阶乘
            * */
            case R.id.Factorial:
                checkInput.setEquation(equation);
                if(checkInput.checkFactorialInput())
                    equation+='!';
                break;
            /*
            * 清空按钮
            * */
            case R.id.CE:
                equation = "";
                resultText.setText("");
                break;
            /*
            * 小数点按钮
            * */
            case R.id.Point:
                checkInput.setEquation(equation);
                if(checkInput.checkPointInput()){
                    equation = checkInput.getEquation();
                    equation+=text;
                }
                break;
            /*
            * 点击π
            * */
            case R.id.P:
                checkInput.setEquation(equation);
                if (checkInput.checkSpecialSymbolBack()){
                    equation+="π";
                }
                break;
            /*
            * 点击e
            * */
            case R.id.E:
                checkInput.setEquation(equation);
                if (checkInput.checkSpecialSymbolBack()){
                    equation+="e";
                }
                break;
            case R.id.Equal:
                resultText.setText(new CounterByEquation(equation).solveEquation());
                break;

        }
        equationText.setText(equation);
    }
    /*
    * init()初始化绑定所有的控件，并添加事件
    * */
    void init(){

        int length = buttons.length;
        for(int i=0;i<length;i++){

            buttons[i] = (Button)findViewById(buttonIds[i]);
            buttons[i].setOnClickListener(this);
        }
        equationText = (TextView)findViewById(R.id.Equation);
        resultText = (TextView)findViewById(R.id.Result);
        checkInput = new CheckInput();
        counterByEquation = new CounterByEquation(equation);
    }
}
