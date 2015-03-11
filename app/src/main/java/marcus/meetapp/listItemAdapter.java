package marcus.meetapp;


        import android.content.Context;
        import android.media.Image;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ListView;
        import android.widget.TextView;

public class listItemAdapter extends ArrayAdapter<ListPerson> {
    private final Context context;
    private final ListPerson[] values;

    public listItemAdapter(Context context, ListPerson[] values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View listItem = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) listItem.findViewById(R.id.firstLine);
        TextView textView2 = (TextView) listItem.findViewById(R.id.secondLine);
        ImageView imageView = (ImageView) listItem.findViewById(R.id.icon);
        textView.setText(values[position].getName());
        textView2.setText("Longitud: " + String.valueOf(values[position].getLocation().getLongitude())
                            + "      Latitud: " + String.valueOf(values[position].getLocation().getLatitude()));

        setImage(position, imageView);

        final ImageButton checker = (ImageButton) listItem.findViewById(R.id.removeChecker);
        ImageButton cross = (ImageButton) listItem.findViewById(R.id.removeCross);

        checker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getVisibility() == View.VISIBLE) {
                    ListView listView = (ListView) v.getParent().getParent();
                    int position = listView.getPositionForView((View) v.getParent());
                    System.err.println("CONTEXT " + context.getClass().toString() + " " +v.getContext());
                    MainActivity main = (MainActivity) v.getContext();
                    main.removePosition(position);
                }
            }
        });

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getVisibility() == View.VISIBLE) {
                    checker.setVisibility(View.INVISIBLE);
                    v.setVisibility(View.INVISIBLE);
                    ImageView icon = (ImageView) listItem.findViewById(R.id.icon);
                    setImage(position, icon);

                }
            }
        });

        return listItem;
    }

    private void setImage(int position, ImageView imageView) {
        if("location".equalsIgnoreCase(values[position].getPersonOrLocation())) {
            imageView.setImageResource(R.drawable.ic_action_map);
        } else {
            imageView.setImageResource(R.drawable.ic_action_person);
        }
    }

}
