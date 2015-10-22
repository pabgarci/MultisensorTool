package es.pabgarci.multisensortool;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListAdapterSimple extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;

    public ListAdapterSimple(Activity context,
                       String[] web) {
        super(context, R.layout.list_simple, web);
        this.context = context;
        this.web = web;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_simple, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText(web[position]);
        return rowView;
    }
}


