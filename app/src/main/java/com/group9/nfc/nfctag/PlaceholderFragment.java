package com.group9.nfc.nfctag;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;

import connection.client.Client;
import connection.json.JSONArray;
import connection.json.JSONObject;

/**
 * Created by yang on 15/7/17.
 */
public class PlaceholderFragment extends Fragment {
    Activity mactivity;
    private int account_balance;
    private int wallets;
    private TextView textWallets;
    private TextView textAccountName;
    private TextView textAccountBalance;
    private TextView textWalletName;
    private TextView textWalletBalance;
    private Button buttonWallet;
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
    public static PlaceholderFragment newInstance(int sectionNumber, Activity activity) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        fragment.mactivity = activity;
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public String ranId() {
        String ans = "";
        for (int i = 0; i < 4; i++) {
            int ch = (int) Math.floor(Math.random() * 1000) % 4;
            ans += ('a' + ch);
        }
        return ans;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int select = getArguments().getInt(ARG_SECTION_NUMBER);
        View rootView = null;
        switch (select) {
            case 1:
                account_balance = new Client.AsnyRequest() {
                    public Client.Response getResponse() {
                        return Client.getClient().getUserInfo();
                    }
                }.post().helper.getUserBalance();
                rootView = inflater.inflate(R.layout.fragment_account, container, false);
                textAccountName = (TextView) rootView.findViewById(R.id.accountName);
                textAccountName.setText(Client.getClient().getUsername());
                textAccountBalance = (TextView) rootView.findViewById(R.id.accountBalance2);
                textAccountBalance.setText(String.valueOf(account_balance));
                Client.Response response = new Client.AsnyRequest() {
                    public Client.Response getResponse() {
                        return Client.getClient().getWallets();
                    }
                }.post();
                if (response.getResult().equals("success")) {
                    JSONArray wallets = response.json.getJSONArray("wallets");
                    textWallets = (TextView) rootView.findViewById(R.id.accountWalletNum);
                    textWallets.setText(String.valueOf(wallets.length()));
                    LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.account);
                    for (int i = 0; i < wallets.length(); i++) {
                        JSONObject wallet = wallets.getJSONObject(i);
//                        final int walletId = wallet.getInt("walletId");
                        LinearLayout ly = (LinearLayout) inflater.inflate(R.layout.wallet, null).findViewById(R.id.addwallet);
                        textWalletName = (TextView) ly.findViewById(R.id.WalletName);
                        textWalletName.setText(wallet.getString("name"));
                        textWalletBalance = (TextView) ly.findViewById(R.id.WalletBalance);
                        textWalletBalance.setText(String.valueOf(wallet.getInt("balance")));
                        ly.findViewById(R.id.deleteWallet).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mactivity);
                                builder.setTitle("虚拟钱包");
                                builder.setMessage("确认删除钱包么");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(mactivity, "positive: " + which, Toast.LENGTH_SHORT).show();
                                       // Client.getClient().deleteWallet(walletId);
                                    }
                                });
                                //    设置一个NegativeButton
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(mactivity, "negative: " + which, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //    设置一个NeutralButton
                                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(mactivity, "neutral: " + which, Toast.LENGTH_SHORT).show();
                                    }
                                });
                                //    显示出该对话框
                                builder.show();

                            }
                        });
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        lp.setMargins(0, 20, 0, 0);
                        layout.addView(ly, lp);

                    }
                } else {
                    response.getErrorMsg();
                }
                break;
            case 2:
                rootView = inflater.inflate(R.layout.fragment_pay, container, false);
                break;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
                final View rootView_ = rootView;
                buttonWallet = (Button) rootView.findViewById(R.id.newWalletButton);
                buttonWallet.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View v) {
                        EditText newWallet = (EditText) rootView_.findViewById(R.id.newWallet);
                        EditText pwd = (EditText) rootView_.findViewById(R.id.AccountPwd);
                        EditText BalanceWallet = (EditText) rootView_.findViewById(R.id.WalletBalance);
                        EditText desc = (EditText) rootView_.findViewById(R.id.descriptionWallet);
                        final String name = newWallet.getText().toString();
                        final String rawVal = ranId();
                        final String description = desc.getText().toString();
                        final int balance = Integer.valueOf(BalanceWallet.getText().toString());
                        if (name.length() > 10){

                        }
                        Client.Response response = new Client.AsnyRequest() {
                            public Client.Response getResponse() {
                                return Client.getClient().createWallet(name, rawVal, description, balance);
                            }
                        }.post();
                        if (response.getResult().equals("success")) {
                            // success
                            Toast.makeText(mactivity, "success", Toast.LENGTH_LONG).show();
                            Log.i("app", "success");
                        } else {
                            Log.i("app", response.getErrorMsg());
                        }
                    }
                });

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

