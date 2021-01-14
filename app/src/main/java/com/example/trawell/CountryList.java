package com.example.trawell;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryList extends AppCompatActivity implements SearchView.OnQueryTextListener{

    String userEmail;
    private FirebaseFirestore db;
    SearchView inputSearch;
    Custd_Adp ca;
    ListView list;
    ArrayList<Country> arrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        userEmail = getIntent().getExtras().getString("email");
        list = findViewById(R.id.list);
        inputSearch=(SearchView)findViewById(R.id.searchView2);

        db = FirebaseFirestore.getInstance();


        getCountrylist();

    }

    private void getCountrylist() {

        db.collection("country")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Country cname = new Country();
                                cname.setId(document.getId());
                                cname.setName(document.getString("name"));
                                cname.setAmount(document.getString("price"));
                                cname.setImageurl(document.getString("imageurl"));
                                setupSearchView();
                                arrayList.add(cname);

                            }

                           ca=new Custd_Adp(CountryList.this,arrayList);
                            list.setAdapter(ca);

                        }
                    }
                });
    }

    private void setupSearchView()
    {
        inputSearch.setIconifiedByDefault(false);
        inputSearch.setOnQueryTextListener(this);
        inputSearch.setSubmitButtonEnabled(true);
        inputSearch.setQueryHint("Search Here");
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        String text = s;
        ca.getFilter().filter(text);
        return false;
    }

    private class Custd_Adp extends BaseAdapter implements Filterable {
        ArrayList<Country> arrayList;
        Context context;
        LayoutInflater inflater;
        public ArrayList<Country> orig;
        public Custd_Adp(Context context, ArrayList<Country> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }
        @Override
        public Filter getFilter() {
            return new Filter() {

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    final FilterResults oReturn = new FilterResults();
                    final ArrayList<Country> results = new ArrayList<Country>();
                    if (orig == null)
                        orig = arrayList;
                    if (constraint != null) {
                        if (orig != null && orig.size() > 0) {
                            for (final Country g : orig) {
                                if (g.getName().toLowerCase()
                                        .contains(constraint.toString()))
                                    results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                    return oReturn;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,
                                              FilterResults results) {
                    arrayList = (ArrayList<Country>) results.values;
                    notifyDataSetChanged();
                }
            };

        }
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return arrayList.size();

        }
        @Override
        public Object getItem(int position) {
            return arrayList.get(position);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int i, View vieww, ViewGroup viewGroup) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.row_country, viewGroup, false);

            TextView pname = (TextView) view.findViewById(R.id.name);
            TextView amount = (TextView) view.findViewById(R.id.amount);
            TextView booknow = (TextView) view.findViewById(R.id.booknow);
            ImageView img = (ImageView) view.findViewById(R.id.image);


            Country p = arrayList.get(i);
            pname.setText(p.getName());
            amount.setText(p.getAmount());
            Glide.with(CountryList.this).load(arrayList.get(i).getImageurl()).into(img);

            booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(CountryList.this, "Booked", Toast.LENGTH_SHORT).show();
                }
            });




            return view;
        }
    }
}
