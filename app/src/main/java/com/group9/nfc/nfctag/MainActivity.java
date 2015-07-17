package com.group9.nfc.nfctag;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import connection.client.Client;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Button goodsInRead;
    private Button buttonWrite;
    private String account;

    private TextView accountTextView;
    private TextView balanceTextView;
    private TextView balanceTitleTextView;
    private TextView textDetected;
    private LinearLayout myAccount;
    private LinearLayout goodsIn;

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;

    void initViewPointer() {
        myAccount = (LinearLayout) findViewById(R.id.myAccount);
        goodsIn = (LinearLayout) findViewById(R.id.goodsIn);

        goodsInRead = (Button) findViewById(R.id.goodInRead);

        accountTextView = (TextView) findViewById(R.id.accountName);
        accountTextView.setText(account);
        accountTextView.setTextSize(25);

        Client.Response response = new Client.AsnyRequest() {
            public Client.Response getResponse() {
                return Client.getClient().getUserInfo();
            }
        }.post();

        balanceTextView = (TextView) findViewById(R.id.balance);
        String balanceNum = String.valueOf(response.helper.getUserBalance());

        balanceTextView.setText(balanceNum);
        balanceTextView.setTextSize(25);

        textDetected = (TextView) findViewById(R.id.textDetected);

    }

    public void goodsInFunc(View view) {
        Toast.makeText(this, "正在读取nfc数据…�??", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            NdefMessage[] msgs = getNdefMessagesFromIntent(intent);
            confirmDisplayedContentOverwrite(msgs[0]);

        } else if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Toast.makeText(this, "This NFC tag has no NDEF data.", Toast.LENGTH_LONG).show();
        }
    }

    NdefMessage[] getNdefMessagesFromIntent(Intent intent) {
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) || action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};

            }

        } else {
//            Log.e(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void confirmDisplayedContentOverwrite(final NdefMessage msg) {
//        new AlertDialog.Builder(this).setTitle("New tag found!").setMessage("Do you wanna show the content of this tag?")
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {

        String payload = new String(msg.getRecords()[0].getPayload());

        textDetected.setText(new String(payload));
        Toast.makeText(this, "读取成功", Toast.LENGTH_LONG).show();
//                    }
//                }).setNegativeButton("No", new DialogInterface.OnClickListener()
//        {
//            @Override
//            public void onClick(DialogInterface dialog, int id)
//            {
//                textDetected.setText(data);
//                dialog.cancel();
//            }
//        }).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mNavigationDrawerFragment = new NavigationDrawerFragment();
        mNavigationDrawerFragment.setType("admin");
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        account = intent.getStringExtra("account");
        initViewPointer();


        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();
        mTitle = "admin";
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
//        buttonWrite = (Button)findViewById(R.id.buttonWrite);
//        buttonWrite.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, WriteTagActivity.class));
//            }
//        });
//
//        buttonRead = (Button)findViewById(R.id.buttonRead);
//        buttonRead.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ReadTagActivity.class));
//            }
//        });

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void setVisible(int number) {

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                myAccount.setVisibility(View.VISIBLE);
                goodsIn.setVisibility(View.GONE);

                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                myAccount.setVisibility(View.GONE);
                goodsIn.setVisibility(View.VISIBLE);

                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
