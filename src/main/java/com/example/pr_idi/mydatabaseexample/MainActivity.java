package com.example.pr_idi.mydatabaseexample;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private BookData bookData;
    private String[] mItemsNav;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private MainFragment mainFragment;
    private BookInfoFragment bookInfoFragment;
    private ListBookFragment fragmentListBook;
    private AddBookFragment addBookFragment;
    private ModifyRatingFragment modifyRatingFragment;
    private RecyclerViewFragment recyclerViewFragment;
    private ActionBarDrawerToggle mDrawerToggle;
    private Book currentlyRating;
    private static boolean onMainFragment;
    private static FragmentManager fragmentManager;
    private static boolean lastDelete;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fragmentManager = getFragmentManager();

        //initialization of all fragments
        bookInfoFragment = new BookInfoFragment();
        mainFragment = new MainFragment();
        fragmentListBook = new ListBookFragment();
        addBookFragment = new AddBookFragment();
        modifyRatingFragment = new ModifyRatingFragment();
        recyclerViewFragment = new RecyclerViewFragment();

        //initialization of elements in the nav drawer
        mItemsNav = getResources().getStringArray(R.array.ItemsNav);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mItemsNav));

        //Preparing the listener for the nav Drawer
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        //Generating the default books info
        bookData = new BookData(this);
        bookData.open();

        fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.icon_menu,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    protected  void onStart() {
        List<Book> LBooks = bookData.getAllBooks();
        ListView booklist = (ListView) findViewById(R.id.listB);
        if (booklist != null) booklist.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, LBooks));

        Book ElQuijote = new Book();
        ElQuijote.setTitle("El Quixot de la Manxa");
        ElQuijote.setAuthor("Miguel de Cervantes");
        ElQuijote.setCategory("Paròdia");
        ElQuijote.setPublisher("Desconeguda");
        ElQuijote.setYear(1605);
        boolean ok = true;
        for (Book b : bookData.getAllBooks()) {
            if (b.toString().equals(ElQuijote.toString())) {ok = false; break;}
        }
        if (ok) bookData.createBook(ElQuijote);

        Book Pilares = new Book();
        Pilares.setTitle("Els Pilars de la Terra");
        Pilares.setAuthor("Ken Follet");
        Pilares.setCategory("Històrica");
        Pilares.setPublisher("MacMillan Publishers");
        Pilares.setYear(1989);
        ok = true;
        for (Book b : bookData.getAllBooks()) {
            if (b.toString().equals(Pilares.toString())) {ok = false; break;}
        }
        if (ok) bookData.createBook(Pilares);

        Book Hamlet = new Book();
        Hamlet.setTitle("Hamlet");
        Hamlet.setAuthor("William Shakespeare");
        Hamlet.setCategory("Tragèdia");
        Hamlet.setPublisher("Desconeguda");
        Hamlet.setYear(1603);
        ok = true;
        for (Book b : bookData.getAllBooks()) {
            if (b.toString().equals(Hamlet.toString())) {ok = false; break;}
        }
        if (ok) bookData.createBook(Hamlet);


        Book Jobs = new Book();
        Jobs.setTitle("Steve Jobs, la biografia");
        Jobs.setAuthor("Walter Isaacson");
        Jobs.setCategory("Biografia");
        Jobs.setPublisher("Penguin Random House Grupo Editorial");
        Jobs.setYear(2011);
        ok = true;
        for (Book b : bookData.getAllBooks()) {
            if (b.toString().equals(Jobs.toString())) {ok = false; break;}
        }
        if (ok) bookData.createBook(Jobs);

        mainFragment.setFragManag(fragmentManager);
        mainFragment.setBookData(bookData);
        mainFragment.setBookInfoFrag(bookInfoFragment);

        fragmentListBook.setFragManag(fragmentManager);
        fragmentListBook.setBookData(bookData);
        fragmentListBook.setBookInfoFrag(bookInfoFragment);

        recyclerViewFragment.setBD(bookData);
        onMainFragment = true;
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.HelpButton:
                Intent i = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(i);

                return true;
            case R.id.AboutButton:
                Intent i1 = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(i1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.HelpButton).setVisible(!drawerOpen);
        menu.findItem(R.id.AboutButton).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    private void selectItem(int position) {
        // Insert the fragment by replacing any existing fragment
        switch(position) {
            case 0:
                fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
                break;
            case 1:
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentListBook).commit();
                break;
            case 2:
                fragmentManager.beginTransaction().replace(R.id.content_frame, addBookFragment).commit();
                break;
            case 3:
                fragmentManager.beginTransaction().replace(R.id.content_frame, recyclerViewFragment).commit();
                break;
            default:
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void onModifyItemPressed(View v) {
        currentlyRating = bookInfoFragment.getBook();
        fragmentManager.beginTransaction().replace(R.id.content_frame, modifyRatingFragment).commit();
        modifyRatingFragment.setBook(currentlyRating);
    }

    public void onGuardaAddBookButtonPressed(View v) {
        EditText etTitol = (EditText) findViewById(R.id.bookNameNew);
        EditText etAutor = (EditText) findViewById(R.id.bookAuthorNew);
        EditText etPublisher = (EditText) findViewById(R.id.bookPublisherNew);
        EditText etYear = (EditText) findViewById(R.id.bookYearNew);
        EditText etCategory = (EditText) findViewById(R.id.bookCategoryNew);


        Log.d("TESTING", "el valor que te publisher és------>" + etPublisher.getText());

        if (etTitol.getText().toString().equals(""))
            Toast.makeText(this, "No s'ha pogut afegir llibre, no té títol", Toast.LENGTH_SHORT).show();
        else if (etAutor.getText().toString().equals(""))
            Toast.makeText(this, "No s'ha pogut afegir llibre, no té autor", Toast.LENGTH_SHORT).show();
        else {
            boolean ok = true;
            for (Book b : bookData.getAllBooks()) {
                if (b.getTitle().equals(etTitol.getText().toString()) && b.getAuthor().equals(etAutor.getText().toString())) {
                    Toast.makeText(this, "Ja existeix el llibre a la base de dades", Toast.LENGTH_SHORT).show();
                    ok = false;
                    break;
                }
            }
            if (ok) {
                Book b = new Book();
                b.setTitle(etTitol.getText().toString());
                b.setAuthor(etAutor.getText().toString());
                b.setPublisher(etPublisher.getText().toString());
                if (etYear.getText().length() != 0)
                    b.setYear(Integer.parseInt(etYear.getText().toString()));
                b.setCategory(etCategory.getText().toString());
                bookData.createBook(b);
                Toast.makeText(this, "Llibre afegit correctament", Toast.LENGTH_SHORT).show();
                fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
                mDrawerList.setItemChecked(0, true);

            }
        }
    }

    public void onEliminaLlibreButtonPressed(View v) {
        currentlyRating = bookInfoFragment.getBook();
        bookData.deleteBook(currentlyRating);
        Toast.makeText(this, "Llibre eliminat correctament", Toast.LENGTH_SHORT).show();

        if (lastDelete) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
            mDrawerList.setItemChecked(0, true);
        }
        else {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragmentListBook).commit();
            mDrawerList.setItemChecked(1, true);
        }

    }

    //whenever we push the search button on the ListBookFragment, we should look for books in the database
    //and display them
    public void onSearchButtonPressed(View v) {
        EditText autorSearch = (EditText) findViewById(R.id.autorSearch);
        String text = autorSearch.getText().toString();
        ArrayList<Book> selected = searchAuthor(text, bookData.getAllBooks());
        if (selected.isEmpty()) Toast.makeText(this, "No hi ha cap autor que coincideixi", Toast.LENGTH_SHORT).show();
        else {
            ListView booklist = (ListView) findViewById(R.id.list);
            ArrayAdapter<Book> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selected);
            if (booklist != null)  booklist.setAdapter(adapter);
        }
    }

    public void onGuardaButtonPressed(View v) {
        Toast.makeText(this, "Valoració guardada correctament", Toast.LENGTH_SHORT).show();
        Spinner et = (Spinner) findViewById(R.id.spinnerValoration);
        bookData.deleteBook(currentlyRating);
        currentlyRating.setPersonal_evaluation(et.getSelectedItem().toString());
        bookData.createBook(currentlyRating);
        fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (onMainFragment) super.onBackPressed();
        else {
            fragmentManager.beginTransaction().replace(R.id.content_frame, mainFragment).commit();
            mDrawerList.setItemChecked(0, true);
        }
    }

    //auxiliary method which actually returns the books whose author
    //may correspond to the text searched in the EditText
    public static ArrayList<Book> searchAuthor(String name, List<Book> books) {
        ArrayList<Book> okNames = new ArrayList<Book>();
        boolean empty = true;
        for (Book b : books) {
            String nameAux = b.getAuthor();
            if (nameAux.startsWith(name)) {
                okNames.add(b);
                empty = false;
            }
        }
        if (empty) {
            for (Book b : books) {
                String nameAux = b.getAuthor();
                if (nameAux.contains(name)) okNames.add(b);
            }
        }

        return okNames;
    }

    //Fragment that allows us to display the books ordered
    public static class MainFragment extends Fragment {

        private BookData bookData;
        private FragmentManager fm;
        private BookInfoFragment bif;
        private ListView booklist;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.main_fragment, container, false);
            getActivity().getActionBar().setTitle(getDrawerTitle());
            booklist = (ListView) v.findViewById(R.id.listB);
            ListView lv = (ListView) v.findViewById(R.id.listB);
            onMainFragment = true;
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    bif.setBook(bookData.getAllBooks().get(position));
                    fm.beginTransaction().replace(R.id.content_frame, bif).commit();
                    lastDelete = true;
                }
            });
            return v;
        }

        public void onStart() {
            List<Book> LBooks = bookData.getAllBooks();
            ArrayList<String> Titles = new ArrayList<String>();
            for (Book b:LBooks) Titles.add(b.getTitle());
            if (booklist != null) booklist.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Titles));
            super.onStart();
        }


        public void setFragManag(FragmentManager fm) {
            this.fm = fm;
        }

        public void setBookData(BookData bd) {
            bookData = bd;
        }

        public void setBookInfoFrag(BookInfoFragment bif) {
            this.bif = bif;
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public String getDrawerTitle() {
            return "Llistat de llibres";
        }
    }

    //Fragment that allows us to display the books and do some author search
    public static class ListBookFragment extends Fragment {

        private EditText et;
        private BookData bookData;
        private FragmentManager fm;
        private BookInfoFragment bif;
        private ListView booklist;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v =  inflater.inflate(R.layout.list_books_fragment, container, false);
            getActivity().getActionBar().setTitle(getDrawerTitle());
            et = (EditText) v.findViewById(R.id.autorSearch);
            booklist = (ListView) v.findViewById(R.id.list);
            onMainFragment = false;
            et.setOnKeyListener(new View.OnKeyListener()
            {
                public boolean onKey(View v, int keyCode, KeyEvent event)
                {
                    if (event.getAction() == KeyEvent.ACTION_DOWN)
                    {
                        switch (keyCode)
                        {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
                                String text = et.getText().toString();
                                ArrayList<Book> selected = searchAuthor(text, bookData.getAllBooks());
                                if (selected.isEmpty()) Toast.makeText(getActivity(), "No hi ha cap autor que coincideixi", Toast.LENGTH_SHORT).show();
                                else {
                                    ArrayAdapter<Book> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, selected);
                                    if (booklist != null) booklist.setAdapter(adapter);
                                }
                                return true;

                            default:
                                break;
                        }
                    }
                    return false;
                }
            });
            booklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    String bookid = arg0.getItemAtPosition(position).toString();
                    for (Book b: bookData.getAllBooks()) {
                        if (b.toString().equals(bookid)) {
                            bif.setBook(b);
                            fm.beginTransaction().replace(R.id.content_frame, bif).commit();
                        }
                    }
                    lastDelete = false;

                }
            });
            return v;
        }

        public void setFragManag(FragmentManager fm) {
            this.fm = fm;
        }

        public void setBookData(BookData bd) {
            bookData = bd;
        }

        public void setBookInfoFrag(BookInfoFragment bif) {
            this.bif = bif;
        }

        public void onStart() {
            et.setText("");

            super.onStart();
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public String getDrawerTitle() {
            return "Cerca per autor";
        }
    }

    //Fragment that allows us to generate new books
    public static class AddBookFragment extends Fragment{

        private EditText etTitol;
        private EditText etAutor;
        private EditText etPublisher;
        private EditText etYear;
        private EditText etCategory;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.add_book_fragment, container, false);
            getActivity().getActionBar().setTitle(getDrawerTitle());
            etTitol = (EditText) v.findViewById(R.id.bookNameNew);
            etAutor = (EditText) v.findViewById(R.id.bookAuthorNew);
            etPublisher = (EditText) v.findViewById(R.id.bookPublisherNew);
            etYear = (EditText) v.findViewById(R.id.bookYearNew);
            etCategory = (EditText) v.findViewById(R.id.bookCategoryNew);
            onMainFragment = false;
            return v;
        }

        public void onStart() {
            etTitol.setText("");
            etAutor.setText("");
            etPublisher.setText("");
            etYear.setText("");
            etCategory.setText("");
            super.onStart();
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public String getDrawerTitle() {
            return "Afegir llibre";
        }
    }

    public static class ModifyRatingFragment extends Fragment {

        private Book b;
        private TextView tv;
        private Spinner sp;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.modify_rating_fragment, container, false);
            getActivity().getActionBar().setTitle(getDrawerTitle());
            tv = (TextView) view.findViewById(R.id.TitleOfBook);
            sp = (Spinner) view.findViewById(R.id.spinnerValoration);
            onMainFragment = false;
            return view;
        }

        public void onStart() {
            tv.setText(b.getTitle());
            String[] items = getResources().getStringArray(R.array.spinnerItems);
            int auxPos = 0;
            for (String s : items) {
                if (s.equals(b.getPersonal_evaluation())) break;
                auxPos++;
            }
            sp.setSelection(auxPos);
            tv.setText(b.getTitle());
            super.onStart();
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public void setBook(Book b) {
            this.b = b;
        }

        public String getDrawerTitle() {
            return "Modificar valoració";
        }
    }

    public static class BookInfoFragment extends Fragment{
        private Book b;
        private TextView tvTitle;
        private TextView tvAuthor;
        private TextView tvPublisher;
        private TextView tvYear;
        private TextView tvCategory;
        private EditText etValoration;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v =  inflater.inflate(R.layout.book_info_fragment, container, false);
            getActivity().getActionBar().setTitle(getDrawerTitle());
            tvTitle = (TextView) v.findViewById(R.id.bookNameShow);
            tvAuthor = (TextView) v.findViewById(R.id.bookAuthorShow);
            tvPublisher = (TextView) v.findViewById(R.id.bookPublisherShow);
            tvYear = (TextView) v.findViewById(R.id.bookYearShow);
            tvCategory = (TextView) v.findViewById(R.id.bookCategoryShow);
            etValoration = (EditText) v.findViewById(R.id.bookValorationShow);
            onMainFragment = false;
            return v;
        }

        public void onStart() {
            tvTitle.setText(b.getTitle());
            tvAuthor.setText(b.getAuthor());
            tvPublisher.setText(b.getPublisher());
            Integer year = b.getYear();
            tvYear.setText(year.toString());
            tvCategory.setText(b.getCategory());
            etValoration.setText(b.getPersonal_evaluation());
            super.onStart();
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        public Book getBook() {
            return b;
        }

        public void setBook(Book b) {
            this.b = b;
        }

        public String getDrawerTitle() {
            return "Informació del llibre";
        }
    }

    public static  class RecyclerViewFragment extends Fragment {

        private BookData bd;
        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View v = inflater.inflate(R.layout.recyclerview_fragment, container, false);
            getActivity().getActionBar().setTitle("Llibres per categoria");
            mRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            onMainFragment = false;
            // specify an adapter (see also next example)
            List<Book> lb = bd.getAllBooks();
            sortByCategoria(lb);
            mAdapter = new BooksAdapter(lb);
            if (mRecyclerView != null) mRecyclerView.setAdapter(mAdapter);

            return v;
        }

        private void sortByCategoria(List<Book> lb) {
            int min;
            for (int i = 0; i < lb.size(); ++i) {
                //find minimum in the rest of array
                min = i;
                for (int j = i + 1; j < lb.size(); ++j) {
                    if (lb.get(j).getCategory().compareTo(lb.get(min).getCategory()) < 0) {
                        min = j;
                    }
                }
                //do swap
                swap(lb, i, min);
            }

        }

        public static void swap(List<Book> sort, int i, int j) {
            Book tmp = sort.get(i);
            sort.set(i, sort.get(j));
            sort.set(j, tmp);
        }

        public void setBD(BookData bd) {
            this.bd = bd;
        }

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

    }

    public static class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

        private List<Book> booksList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, author, category, year, publisher;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                category= (TextView) view.findViewById(R.id.category);
                author = (TextView) view.findViewById(R.id.author);
                year = (TextView) view.findViewById(R.id.Year);
                publisher = (TextView) view.findViewById(R.id.Publisher);
            }
        }


        public BooksAdapter(List<Book> booksList) {
            this.booksList = booksList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_list_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Book book = booksList.get(position);
            holder.title.setText(book.getTitle());
            holder.category.setText(book.getCategory());
            holder.author.setText(book.getAuthor());
            holder.year.setText(String.valueOf(book.getYear()));
            holder.publisher.setText(book.getPublisher());
        }

        @Override
        public int getItemCount() {
            return booksList.size();
        }
    }
}