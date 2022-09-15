package com.ekenya.lamparam.ui.wallet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.lamparam.activities.main.LampMainActivity
import com.ekenya.lamparam.R
import com.ekenya.lamparam.utilities.StaticData
import kotlinx.android.synthetic.main.fragment_wallet_statement.view.*


class WalletStatement : Fragment() {

    lateinit var rcvWalletStmt: RecyclerView
    lateinit var walletStmtAdapter: WalletStmtAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_wallet_statement, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rcvWalletStmt = view.rcvWalletStmt
        rcvWalletStmt.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rcvWalletStmt.layoutManager = layoutManager
        var dashmenuList = StaticData().sendMoneyList()
        walletStmtAdapter = WalletStmtAdapter()
        rcvWalletStmt.adapter = walletStmtAdapter

    }
}