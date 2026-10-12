package com.example.dispmov_proyecto;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends Activity {
    private final int navy=Color.rgb(22,39,68), blue=Color.rgb(36,100,219), muted=Color.rgb(95,111,133);
    private LinearLayout root, content;
    private EditText taskName, details;
    private TextView dateValue, durationValue;
    private String selectedDate="";
    private int minutes=30;
    private SharedPreferences prefs;
    private JSONArray tasks;
    @Override public void onCreate(Bundle state){super.onCreate(state);getWindow().setStatusBarColor(navy);prefs=getSharedPreferences("disp_trabajador",MODE_PRIVATE);try{tasks=new JSONArray(prefs.getString("tasks","[]"));}catch(Exception e){tasks=new JSONArray();}showHome();}
    private int dp(float v){return (int)(getResources().getDisplayMetrics().density*v+0.5f);}
    private GradientDrawable bg(int color,int radius){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(dp(radius));return d;}
    private TextView text(String s,int size,int color,boolean bold){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(color);if(bold)t.setTypeface(null,Typeface.BOLD);return t;}
    private void gap(int n){View v=new View(this);content.addView(v,new LinearLayout.LayoutParams(1,dp(n)));}
    private void screen(String heading,String subtitle){ScrollView scroll=new ScrollView(this);scroll.setFillViewport(true);root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(Color.rgb(245,248,253));scroll.addView(root);setContentView(scroll);LinearLayout header=new LinearLayout(this);header.setOrientation(LinearLayout.VERTICAL);header.setPadding(dp(22),dp(28),dp(22),dp(25));header.setBackgroundColor(navy);TextView h=text(heading,25,Color.WHITE,true);header.addView(h);TextView sub=text(subtitle,14,Color.rgb(203,218,238),false);sub.setPadding(0,dp(8),0,0);header.addView(sub);root.addView(header);content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(dp(18),dp(20),dp(18),dp(35));root.addView(content);}
    private void card(View v){LinearLayout shell=new LinearLayout(this);shell.setOrientation(LinearLayout.VERTICAL);shell.setPadding(dp(16),dp(16),dp(16),dp(16));shell.setBackground(bg(Color.WHITE,16));shell.setElevation(dp(2));shell.addView(v);content.addView(shell,new LinearLayout.LayoutParams(-1,-2));gap(12);}
    private void button(String title,Runnable action){Button b=new Button(this);b.setText(title);b.setAllCaps(false);b.setTextSize(16);b.setTextColor(Color.WHITE);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(blue));b.setMinHeight(dp(52));content.addView(b,new LinearLayout.LayoutParams(-1,dp(56)));b.setOnClickListener(v->action.run());gap(12);}
    private void secondary(String title,Runnable action){Button b=new Button(this);b.setText(title);b.setAllCaps(false);b.setTextSize(15);b.setTextColor(navy);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.rgb(222,232,246)));content.addView(b,new LinearLayout.LayoutParams(-1,dp(52)));b.setOnClickListener(v->action.run());gap(12);}
    private void label(String s){TextView t=text(s,15,navy,true);content.addView(t);gap(8);}
    private EditText field(String hint,boolean multi){EditText e=new EditText(this);e.setTextSize(16);e.setSingleLine(!multi);e.setHint(hint);e.setPadding(dp(14),dp(12),dp(14),dp(12));e.setBackground(bg(Color.WHITE,12));e.setMinHeight(dp(multi?90:54));content.addView(e,new LinearLayout.LayoutParams(-1,-2));gap(18);return e;}
    private void showHome(){screen("Mi espacio de trabajo","DISP MÓVILES  ·  TRABAJADOR");card(text("Organiza tus actividades con una fecha programada y un tiempo de solución estimado.",16,muted,false));card(text("ACTIVIDADES REGISTRADAS    " + tasks.length(),15,blue,true));button("＋  Crear tarea",this::showWorker);secondary("▤  Mis tareas",this::showTasks);}
    private void showWorker(){selectedDate="";minutes=30;screen("Crear nueva tarea","TRABAJADOR  ·  REGISTRO DE ACTIVIDAD");secondary("‹  Volver al inicio",this::showHome);label("Nombre de la tarea");taskName=field("Ej. Revisar equipo",false);label("Descripción (opcional)");details=field("Describe brevemente la actividad",true);label("Fecha programada");dateValue=text("▦  Seleccionar fecha en calendario",16,navy,false);dateValue.setPadding(dp(15),dp(17),dp(12),dp(17));dateValue.setBackground(bg(Color.WHITE,12));content.addView(dateValue);dateValue.setContentDescription("Seleccionar fecha en calendario");dateValue.setOnClickListener(v->{Calendar c=Calendar.getInstance();DatePickerDialog dialog=new DatePickerDialog(this,(picker,y,m,d)->{selectedDate=String.format(Locale.US,"%04d-%02d-%02d",y,m+1,d);dateValue.setText("▦  "+String.format(Locale.getDefault(),"%02d/%02d/%04d",d,m+1,y));},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));dialog.getDatePicker().setMinDate(c.getTimeInMillis()-1000);dialog.show();});gap(18);label("Tiempo estimado de solución");durationValue=text("◷  30 minutos  ▾",16,navy,false);durationValue.setPadding(dp(15),dp(17),dp(12),dp(17));durationValue.setBackground(bg(Color.WHITE,12));content.addView(durationValue);durationValue.setContentDescription("Seleccionar tiempo requerido");durationValue.setOnClickListener(v->showTimeOptions());gap(22);button("✓  Guardar tarea",this::saveTask);}
    private void showTimeOptions(){String[] opts={"15 minutos","30 minutos","45 minutos","1 hora","1 hora 30 minutos","2 horas","3 horas","4 horas","Personalizado"};new android.app.AlertDialog.Builder(this).setTitle("Tiempo requerido").setItems(opts,(d,i)->{if(i==8){LinearLayout wrap=new LinearLayout(this);wrap.setPadding(dp(22),0,dp(22),0);EditText value=new EditText(this);value.setInputType(2);value.setHint("Minutos (1 a 10080)");wrap.addView(value);new android.app.AlertDialog.Builder(this).setTitle("Duración personalizada").setView(wrap).setPositiveButton("Aceptar",(x,w)->{try{int n=Integer.parseInt(value.getText().toString());if(n<1||n>10080)throw new Exception();minutes=n;durationValue.setText("◷  "+formatDuration(n)+"  ▾");}catch(Exception ex){Toast.makeText(this,"Ingresa entre 1 y 10080 minutos",Toast.LENGTH_LONG).show();}}).setNegativeButton("Cancelar",null).show();}else{int[] values={15,30,45,60,90,120,180,240};minutes=values[i];durationValue.setText("◷  "+formatDuration(minutes)+"  ▾");}}).show();}
    private String formatDuration(int n){return n<60?n+" min":n%60==0?(n/60)+" h":(n/60)+" h "+(n%60)+" min";}
    private void saveTask(){String name=taskName.getText().toString().trim();if(name.isEmpty()){taskName.setError("Escribe el nombre de la tarea");return;}if(selectedDate.isEmpty()){Toast.makeText(this,"Selecciona una fecha del calendario",Toast.LENGTH_LONG).show();return;}try{JSONObject o=new JSONObject();o.put("name",name);o.put("description",details.getText().toString().trim());o.put("date",selectedDate);o.put("minutes",minutes);o.put("status","Pendiente");o.put("created",System.currentTimeMillis());tasks.put(o);prefs.edit().putString("tasks",tasks.toString()).apply();new android.app.AlertDialog.Builder(this).setTitle("Registro listo").setMessage("La tarea se guardó en este dispositivo para la demostración. El equipo conectará el guardado compartido.").setPositiveButton("Aceptar",(d,w)->showTasks()).show();}catch(Exception e){Toast.makeText(this,"No se pudo guardar",Toast.LENGTH_LONG).show();}}
    private void showTasks(){
        screen("Mis actividades","TRABAJADOR  ·  REGISTROS LOCALES");
        secondary("‹  Volver",this::showHome);
        button("＋  Nueva tarea",this::showWorker);
        if(tasks.length()==0){card(text("No hay tareas registradas.",16,muted,false));return;}
        for(int i=tasks.length()-1;i>=0;i--){
            JSONObject task=tasks.optJSONObject(i);if(task==null)continue;
            LinearLayout block=new LinearLayout(this);block.setOrientation(LinearLayout.VERTICAL);
            block.addView(text(task.optString("name"),18,navy,true));
            TextView meta=text("▦  "+task.optString("date")+"    ◷  "+formatDuration(task.optInt("minutes")),15,muted,false);
            meta.setPadding(0,dp(9),0,dp(7));block.addView(meta);
            String description=task.optString("description");
            if(!description.isEmpty())block.addView(text(description,15,muted,false));
            card(block);
        }
    }
    @Override public void onBackPressed(){showHome();}
}
