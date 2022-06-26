package com.example.circlepicture;

import android.os.Bundle;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.circlepicture.databinding.ActivityEvolucaoBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityEvolucao extends AppCompatActivity {

    private ActivityEvolucaoBinding binding;
    private SimpleAdapter mAdapter;
    private BancoController meuBanco;

    //todo se o user ainda não tiver tirado nenhuma foto mostrar uma mensagem no centro da tela dizendo: "tire uma foto para ter um registro da mudança do seu corpo com os exercícios"
    //todo se o user já tiver tirado alguma foto eu tenho que carregar a foto com sua devida categoria, que no caso é um grupo de intervalo de datas(hoje, na ultima semana, nos ultimos 6 meses etc)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEvolucaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        meuBanco = new BancoController(this);


        //Your RecyclerView
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 4));

        //Your recyclerview adapter
        mAdapter = new SimpleAdapter(this);

        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections = new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"hoje"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(5,"na última semana"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(12,"no último mês"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"nos últimos 6 meses"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(20,"há mais de um ano"));

        //add your adapter to the sectionAdapter
        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
                SectionedGridRecyclerViewAdapter(this,R.layout.section,R.id.section_text,binding.recycler,mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        binding.recycler.setAdapter(mSectionedAdapter);


    }

}

//link sectiloned grid:
//https://gist.github.com/gabrielemariotti/e81e126227f8a4bb339c
//testar essa solução em um projeto novo, antes de implementar aqui, veja se essa
//solução suportará a adição de novas imagens e se tudo vai ser atualizado certinho
//link do stackoverflow onde eu achei essa possível solução:
//https://stackoverflow.com/questions/7397988/android-gridview-with-categories


