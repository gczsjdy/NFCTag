package com.group9.nfc.nfctag;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import connection.client.Client;

import static com.group9.nfc.nfctag.R.id.navigation_drawer;

public class MainActivity2 extends ActionBarActivity
        implements NavigationDrawerFragment2.NavigationDrawerCallbacks {

    private Button buttonRead;
    private Button buttonWrite;
    private NavigationDrawerFragment2 mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        mNavigationDrawerFragment = (NavigationDrawerFragment2)
                getSupportFragmentManager().findFragmentById(navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section2_1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2_2);
                break;
            case 3:
                mTitle = getString(R.string.title_section2_3);
                break;
            case 4:
                mTitle = getString(R.string.title_section2_4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Client.getClient().logout();
            startActivity(new Intent(this, SignInActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private int account_balance;
        private int wallets;
        private TextView textAccountName;
        private TextView textAccountBalance;
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_BALANCE = "account_balance";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int select = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = null;
            switch (select) {
                case 1:
                    account_balance = new Client.AsnyRequest(){
                        public Client.Response getResponse() {
                            return Client.getClient().getUserInfo();
                        }
                    }.post().helper.getUserBalance();
                    rootView = inflater.inflate(R.layout.fragment_account, container, false);
                    textAccountName = (TextView) rootView.findViewById(R.id.accountName);
                    textAccountName.setText(Client.getClient().getUsername());
                    textAccountBalance = (TextView) rootView.findViewById(R.id.accountBalance2);
                    textAccountBalance.setText(String.valueOf(account_balance));
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_pay, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
                    break;
                case 4:
                    rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                    break;
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity2) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
