package bd.ac.mist.sharecare;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends ArrayAdapter{
    List list;//=new ArrayList();
    public Adapter(Context context, int resource) {
        super(context, resource);
        list=new ArrayList();
    }

    public void add(Details object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        row=convertView;
        Holder holder;
        if(row==null){
            LayoutInflater layoutInflater= (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=layoutInflater.inflate(R.layout.post_list_item,parent,false);
            holder=new Holder();
            holder.post= (TextView) row.findViewById(R.id.post);
            holder.user_name= (TextView) row.findViewById(R.id.author);
            holder.time=(TextView) row.findViewById(R.id.time);
            row.setTag(holder);
        }else {
            holder= (Holder) row.getTag();
        }
        Details details= (Details) this.getItem(position);
        holder.post.setText(details.getPost());
        holder.user_name.setText(details.getUser());
        holder.time.setText(details.getTime());
        return row;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    static class Holder{
        TextView post,user_name,time;
    }
}
