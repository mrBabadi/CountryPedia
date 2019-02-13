package babadi.countrypedia.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import babadi.countrypedia.R;
import babadi.countrypedia.data.model.Country;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MVHolder> {

    Context context;
    List<Country> countryList;
    OnItemClickListener itemClickListener;

    public CountryAdapter(Context context, List<Country> countryList) {
        this.context = context;
        this.countryList = countryList;
    }

    public class MVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView countryName;
        TextView capitalName;
        ImageView countryFlag;

        public MVHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.country_name_tv);
            capitalName = itemView.findViewById(R.id.capital_name_tv);
            countryFlag = itemView.findViewById(R.id.country_flag_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onClick(countryList.get(getAdapterPosition()));
            }
        }
    }


    @NonNull
    @Override
    public MVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_country, parent, false);
        return new MVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MVHolder holder, int position) {
        Country country = countryList.get(position);
        holder.countryName.setText(country.getNativeName());
        holder.capitalName.setText(country.getCapital());

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public void setClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
