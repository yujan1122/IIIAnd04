package tw.org.iii.iiiand04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private String answer;
    private int dig = 3;
    private EditText input;
    private TextView log;
    private int counter; //basic type default 0
    //System 當時使用的作業系統
    private long lastTime = 0;//System.currentTimeMillis();
    private int temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.input);
        log = findViewById(R.id.log);
        answer = createAnswer(dig);
        Log.v("brad", answer);
    }

    //定義產生謎底 java api search, 善用api既有功能
    private String createAnswer(int dig){ //gray  還沒被呼叫過
        /* method1:
        HashSet<Integer> set = new HashSet<>();
        while (set.size() < dig){
            set.add((int)(Math.random()*10));//產生0-9數字
        }//產生不重複三位數
        */

        /*StringBuffer sb = new StringBuffer();
        for(Integer i : set) { //沒有順序性 , 陣列, for-each
            sb.append(i);
        }
        Log.v("brad", sb.toString());
        //Log.v("brad", set.toString());
        */

        //method2:
        LinkedList<Integer> list = new LinkedList<>();
        for (int i=0; i<10; i++) list.add(i);
        Collections.shuffle(list);

        StringBuffer sb = new StringBuffer();
        for (int i=0; i<dig; i++){
            sb.append(list.get(i));
        }
        Log.v("brad", sb.toString());

        return sb.toString();
        //return "";
    }

    public void guess(View view) {
        //createAnswer(4);//參數dig會自動帶出
        counter++;
        String strInput = input.getText().toString();
        //思考重複輸入,應該要防呆, 拉出函數
        if ( !isRightNum(strInput) ){
            return;
        }


        String result = checkAB(strInput);
        log.append( counter + ":" + strInput + "=>" + result + "\n");

        if (result.equals(dig + "A0B")){
            //winner
            showDialog(true);
        }else if(counter == 3) {
            //loser
            showDialog(false);
        }
        input.setText("");
    }
    private boolean isRightNum(String g){
        return g.matches("^[0-9]{" + dig + "}");
    }



    private void showDialog(boolean isWinner){
        /*
        AlertDialog alertDialog =  null; //a dialog,
        AlertDialog.Builder builder = new AlertDialog.Builder( this)  //cat in cat, produce dialog
        builder.setTitle("Title"); //.setMessage(""); keep reply, no write obj name
        builder.setMessage("");
        alertDialog = builder.create();
        */
        AlertDialog alertDialog = new AlertDialog.Builder( this)
                .setTitle(isWinner?"WINNER" : "Loser")
                .setMessage(isWinner?"Success": "ans="+answer)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGame(null);
                    }
                })
                .create();
        alertDialog.show();
    }

    private String checkAB(String guess){
        int a, b; a= b = 0;
        for(int i=0; i<guess.length(); i++) {
            if (guess.charAt(i) == answer.charAt(i)) {   //guess ith = ans ith
                a++;
            } else if (answer.indexOf(guess.charAt(i)) >= 0) { //guess ith exist in ans
                b++;
            }
        }//end for-loop
        return a + "A" + b + "B";
    }

    public void newGame(View view) {
        Log.v("brad", "new game");
        counter = 0;
        input.setText("");
        log.setText("");
        answer = createAnswer(dig);
    }

    public void setting(View view) {
        //猜幾碼(給四個選項), 盡量防呆
        String[] items = {"3","4","5","6"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //.setMessage("請選擇數值長度:") setMessage,setItems 擇一處理
                .setTitle("Select Game Mode")
                .setSingleChoiceItems(items, dig - 3, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("brad", "which1="+which);
                        temp = which;
                    }
                })
                /* 點入就觸發了,好像不是很好用
                .setItems(items , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("brad", "which="+which);
                    }
                })

                 */

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dig = which;
                        Log.v("brad", "which2=" + which);//確認的是which1選擇的點
                        dig = temp + 3;
                        newGame(null);
                    }
                })
                .create();
        alertDialog.show();


    }

    public void exit(View view) {
        //finish();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage("Exit?")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        alertDialog.show(); //記得要加, 才會呈現
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("brad", "onDestroy");
    }

    @Override
    public void finish() {
        super.finish();//父類別定義定義死掉
        Log.v("brad", "finish");
    }

    @Override
    public void onBackPressed() { //返回鍵觸發
        if (System.currentTimeMillis() - lastTime > 3*1000){
            lastTime = System.currentTimeMillis();
            Toast.makeText(this, "back one more", Toast.LENGTH_SHORT).show(); //與顯示有關,會問context是誰 吐司麵包,下方訊息出現一下下
        }else {
            super.onBackPressed();
        }

        //super.onBackPressed();//不做super也會離開
        //Log.v("brad", "onBackPress");
        //真的離開,利用時間軸,超過?秒仍點按, exit
    }
}
//真正在控制的是super