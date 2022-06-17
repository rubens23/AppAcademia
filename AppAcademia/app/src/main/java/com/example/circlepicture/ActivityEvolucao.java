package com.example.circlepicture;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.circlepicture.databinding.ActivityEvolucaoBinding;

import java.util.ArrayList;
import java.util.List;

public class ActivityEvolucao extends AppCompatActivity {

    private ActivityEvolucaoBinding binding;
    private SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEvolucaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Your RecyclerView
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setLayoutManager(new GridLayoutManager(this, 4));

        //Your recyclerview adapter
        mAdapter = new SimpleAdapter(this);

        //This is the code to provide a sectioned grid
        List<SectionedGridRecyclerViewAdapter.Section> sections = new ArrayList<SectionedGridRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"Section 1"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(5,"Section 2"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(12,"Section 3"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"Section 4"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(20,"Section 5"));

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


