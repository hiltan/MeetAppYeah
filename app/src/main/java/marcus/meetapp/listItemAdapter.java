package marcus.meetapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Adapter used for the ListView in the MainActivity.
 */
public class listItemAdapter extends ArrayAdapter<ListPersonLocation> {
    private final Context context;
    private final ListPersonLocation[] values;

    /**
     * Constructor of the adapter.
     * @param context
     * @param values
     */
    public listItemAdapter(Context context, ListPersonLocation[] values) {
        super(context, R.layout.list_item, values);
        this.context = context;
        this.values = values;
    }

    /**
     * Creates a View for a ListPersonLocation and defining the listeners
     * for the buttons that appear when a user long clicks the ListView.
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
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

    /**
     * Sets the image of a person or a map on a ListPersonLocation in the
     * ListView depending on if it is a person or location.
     * @param position
     * @param imageView
     */
    private void setImage(int position, ImageView imageView) {
        if("location".equalsIgnoreCase(values[position].getPersonOrLocation())) {
            imageView.setImageResource(R.drawable.ic_action_map);
        } else {
            imageView.setImageResource(R.drawable.ic_action_person);
        }
    }

}
