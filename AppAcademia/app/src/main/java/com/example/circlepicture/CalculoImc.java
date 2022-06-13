package com.example.circlepicture;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class CalculoImc extends AppCompatActivity {

    EditText peso, altura;
    Button btnImc;
    TextView auxResultadoImc, resultadoImc, descricaoImc;
    Float pesoImc, alturaImc, resImc;
    String formattedImc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculo_imc);

        peso = findViewById(R.id.peso);
        altura = findViewById(R.id.altura);
        resultadoImc = (TextView) findViewById(R.id.resultadoImc);
        auxResultadoImc = (TextView) findViewById(R.id.auxResultadoImc);
        descricaoImc = (TextView) findViewById(R.id.descricaoIMC);
        btnImc = (Button) findViewById(R.id.btnImc);
        btnImc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pesoImc = Float.valueOf(peso.getText().toString().trim());
                alturaImc = Float.valueOf(altura.getText().toString().trim());
                resImc = pesoImc / (alturaImc * alturaImc);
                formattedImc = String.format("%.2f", resImc);
                resultadoImc.setText(formattedImc);
                auxResultadoImc.setVisibility(View.VISIBLE);
                resultadoImc.setVisibility(View.VISIBLE);
                if (resImc >= 18.5 && resImc <= 24.9){
                    descricaoImc.setVisibility(View.VISIBLE);
                    descricaoImc.setText("Você está com o peso normal");
                }else if (resImc > 24.9 && resImc <= 29.9){
                    descricaoImc.setVisibility(View.VISIBLE);
                    descricaoImc.setText("Você está com sobrepeso");
                }else if (resImc >= 30 && resImc <= 34.9){
                    descricaoImc.setVisibility(View.VISIBLE);
                    descricaoImc.setText("Você está com obesidade Grau I");
                }else if (resImc > 34.9 && resImc <= 39.9){
                    descricaoImc.setVisibility(View.VISIBLE);
                    descricaoImc.setText("você está com obesidade Grau II");
                }else if (resImc > 39.9){
                    descricaoImc.setVisibility(View.VISIBLE);
                    descricaoImc.setText("Você está com Obesidade Grau III ou Mórbida");
                }else{
                    descricaoImc.setVisibility(View.VISIBLE);
                    descricaoImc.setText("Você está abaixo do peso ideal");
                }

            }
        });


    }
}
