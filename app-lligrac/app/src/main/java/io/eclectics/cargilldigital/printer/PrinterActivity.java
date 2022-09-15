package io.eclectics.cargilldigital.printer;

import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R200II;
import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R200III;
import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R210;
import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R300;
import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R310;
import static com.bxl.config.editor.BXLConfigLoader.PRODUCT_NAME_SPP_R400;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.bxl.config.editor.BXLConfigLoader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.eclectics.cargilldigital.R;
import io.eclectics.cargilldigital.databinding.ActivityPrinterBinding;
import io.eclectics.cargilldigital.utils.DemoDummyData;
import io.eclectics.cargilldigital.utils.UtilPreference;
import io.eclectics.cargill.utils.NetworkUtility;
import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.config.JposEntry;

public class PrinterActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_BLUETOOTH = 1;
    private static final int REQUEST_CODE_ACTION_PICK = 2;

    private static final String DEVICE_ADDRESS_START = " (";
    private static final String DEVICE_ADDRESS_END = ")";

    private final ArrayList<CharSequence> bondedDevices = new ArrayList<>();
    private ArrayAdapter<CharSequence> arrayAdapter;

    private TextView pathTextView;
    private TextView progressTextView;
    private RadioGroup openRadioGroup;
    private Button openFromDeviceStorageButton;
    private BXLConfigLoader bxlConfigLoader;
    private POSPrinter posPrinter;
    private String logicalName;
    private int brightness = 80;
    private AppBarConfiguration appBarConfiguration;
    private ActivityPrinterBinding binding;
    boolean isPrinterConnected = false;
    RecyclerView rcv;
    ListView deviceListView;
    LinearLayout layoutPrint;
    String printerDataList;
    Boolean isPrinterMenu;
    Button btnPrint;
    Button btnPrintBuyerCopy;
    TextView tvHeader;
    TextView tvFooter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrinterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, bondedDevices);
       // setSupportActionBar(binding.toolbar);
        getPermission();
        onActivityAction();
        //  setBondedDevices();
        setPairedDevices3();
        showPairedDevices();
        //loadBXLConfig();
       // openPrinter();
        setToolbarTitle();
        //get passed intent bundel
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            printerDataList = extras.getString("printerdata");
            isPrinterMenu = extras.getBoolean("isSettings");
            //The key argument here must match that used in the other activity
            checkIfSettingMenu();
        }

    }

    private void setToolbarTitle() {
        TextView tx ;
        tx= findViewById(R.id.toolbar_title);
        tx.setText(getResources().getString(R.string.print_receipt));
        ImageView toolcancel = findViewById(R.id.toolbar_cancel);
        toolcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      /*  binding.mainLayoutToolbar.toolbar.visibility = View.VISIBLE
        toolBar.toolbarTitle.text = title
        toolBar.toolbarDescription.text = description
        toolBar.toolbarCancel.setOnClickListener {
            navigationMgmt()
        }*/
    }

    private void checkIfSettingMenu() {
        //layoutPrintDetails  isSettings - if true
        //if this originates from settings menu hide print recycler veiw and show only menu nad connection
        if(isPrinterMenu){
            findViewById(R.id.layoutPrintDetails).setVisibility(View.GONE);
            showConfigs();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closePrinter();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            final Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_BLUETOOTH);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_BLUETOOTH:
                //setBondedDevices();
                setPairedDevices3();
                break;

            case REQUEST_CODE_ACTION_PICK:
                onPickSomeAction(data);
                break;
        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.buttonOpenFromDeviceStorage:
                openFromDeviceStorage();
                break;

            case R.id.buttonOpenPrinter:
                openPrinter();
                break;

            case R.id.buttonPrint:
                //print();
                try {
                   // printSampleText();
                    printTransactionData();
                } catch (JposException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnPrint:
                //print();
                try {
                    // printSampleText();
                    printTransactionData();
                } catch (JposException e) {
                    e.printStackTrace();
                    finish();
                }
                break;

        }
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
        final String device = ((TextView) view).getText().toString();
        final String name = device.substring(0, device.indexOf(DEVICE_ADDRESS_START));
        final String address = device.substring(device.indexOf(DEVICE_ADDRESS_START)
                + DEVICE_ADDRESS_START.length(), device.indexOf(DEVICE_ADDRESS_END));

        try {
            for (final Object entry : bxlConfigLoader.getEntries()) {
                final JposEntry jposEntry = (JposEntry) entry;
                bxlConfigLoader.removeEntry(jposEntry.getLogicalName());
            }
        } catch (final Exception e) {
            Log.e(TAG, "Error removing entry.", e);
        }

        try {
            //TODO MODIFICATION
            logicalName = setProductName(name);
            bxlConfigLoader.addEntry(logicalName,
                    BXLConfigLoader.DEVICE_CATEGORY_POS_PRINTER,
                    logicalName,
                    BXLConfigLoader.DEVICE_BUS_BLUETOOTH, address);

            bxlConfigLoader.saveFile();
        } catch (final Exception e) {
            Log.e(TAG, "Error saving file.", e);
        }
    }

    private void onActivityAction() {
        try {
            pathTextView = findViewById(R.id.textViewPath);
            progressTextView = findViewById(R.id.textViewProgress);
            btnPrint = findViewById(R.id.btnPrint);
            btnPrintBuyerCopy = findViewById(R.id.btnPrintBuyerCopy);
            deviceListView = findViewById(R.id.listViewPairedDevices);
            layoutPrint = findViewById(R.id.buttonsPrinter);
            rcv = findViewById(R.id.rcvPrintdata);
            tvHeader = findViewById(R.id.tvHeader);
            tvFooter = findViewById(R.id.tvFooter);
            tvHeader.setText(getStringHeader());
            tvFooter.setText(getStringFooter());
            List<PrinterDataAdapter.PrinterData> plist = DemoDummyData.INSTANCE.printList(this);
            PrinterDataAdapter padapter = new PrinterDataAdapter(this, plist);
            rcv.setAdapter(padapter);
            openRadioGroup = findViewById(R.id.radioGroupOpen);
            openRadioGroup.setOnCheckedChangeListener(this);


            openFromDeviceStorageButton = findViewById(R.id.buttonOpenFromDeviceStorage);
            openFromDeviceStorageButton.setOnClickListener(this);

            findViewById(R.id.buttonOpenPrinter).setOnClickListener(this);
            findViewById(R.id.buttonPrint).setOnClickListener(this);
            // findViewById(R.id.buttonClosePrinter).setOnClickListener(this);
            findViewById(R.id.btnPrint).setOnClickListener(this);

            final SeekBar seekBar = findViewById(R.id.seekBarBrightness);
            seekBar.setOnSeekBarChangeListener(this);
            //reset flag isprinter
            new UtilPreference().setIsPrintableData(this,false);
        }catch (Exception ex){}
    }

    private void showPairedDevices() {
        try {
            Log.e("shwoingbt", "show bt devices");

            final ListView listView = findViewById(R.id.listViewPairedDevices);
            listView.setAdapter(arrayAdapter);

            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setOnItemClickListener(this);
        }catch (Exception e){
            Log.e("exceprion",e.getMessage());
        }
    }

    private void loadBXLConfig() {
        try {
            bxlConfigLoader = new BXLConfigLoader(this);
            try {
                bxlConfigLoader.openFile();
            } catch (Exception e) {
                e.printStackTrace();
                bxlConfigLoader.newFile();
            }
            posPrinter = new POSPrinter(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onPickSomeAction(final Intent data) {
        if (data != null) {
            final Uri uri = data.getData();
            if (uri != null) {
                final ContentResolver cr = getContentResolver();
                final Cursor c = cr.query(uri, new String[]{MediaStore.Images.Media.DATA},
                        null, null, null);
                if (c == null || c.getCount() == 0) {
                    return;
                }

                c.moveToFirst();
                final int columnIndex = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                final String text = c.getString(columnIndex);
                c.close();

                pathTextView.setText(text);
            }
        }
    }

    private String setProductName(final String name) {
        if ((name.contains("SPP-R200II"))) {
            if (name.length() > 10) {
                if (name.substring(10, 11).equals("I")) {
                    return PRODUCT_NAME_SPP_R200III;
                }
            }
        } else if ((name.contains("SPP-R210"))) {
            return PRODUCT_NAME_SPP_R210;
        } else if ((name.contains("SPP-R310"))) {
            return PRODUCT_NAME_SPP_R310;
        } else if ((name.contains("SPP-R300"))) {
            return PRODUCT_NAME_SPP_R300;
        } else if ((name.contains("SPP-R400"))) {
            return PRODUCT_NAME_SPP_R400;
        }
        return PRODUCT_NAME_SPP_R200II;
    }


    @Override
    @SuppressLint("SetTextI18n")
    public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
        progressTextView.setText(Integer.toString(progress));
        brightness = progress;
    }

    @Override
    public void onStartTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(final SeekBar seekBar) {
    }

    @Override
    public void onCheckedChanged(final RadioGroup group, final int checkedId) {
        switch (checkedId) {
            case R.id.radioDeviceStorage:
                openFromDeviceStorageButton.setEnabled(true);
                break;

            case R.id.radioProjectResources:
                openFromDeviceStorageButton.setEnabled(false);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // setBondedDevices();
                    setPairedDevices3();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                   // Toast.makeText(PrinterActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getPermission() {
        ActivityCompat.requestPermissions(PrinterActivity.this,
                new String[]{Manifest.permission.BLUETOOTH_CONNECT,Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADMIN},
                1);
    }


    /* public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults){
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

             //calling function again once we get permissions granted
         }
     }*/
    private void setPairedDevices3() {
        try {
            //bondedDevices.clear();

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                getPermission();
                return;
            }
            Set<BluetoothDevice> bondedDeviceSet = bluetoothAdapter.getBondedDevices();

            for (BluetoothDevice device : bondedDeviceSet) {
                Log.e("btdevices", "bt devices" + device.getName() + "type");
                bondedDevices.add(device.getName() + DEVICE_ADDRESS_START + device.getAddress() + DEVICE_ADDRESS_END);
            }
            arrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_single_choice, bondedDevices);
            if (arrayAdapter != null) {
                arrayAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            new NetworkUtility().transactionWarning(getResources().getString(R.string.ensure_printconnect),this);

        }
    }
    private void openFromDeviceStorage() {
        final String externalStorageState = Environment.getExternalStorageState();
        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            final Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
            startActivityForResult(intent, REQUEST_CODE_ACTION_PICK);
        }
    }

    private void openPrinter() {
        try {
            posPrinter.open("SPP-R310");//SPP-R310  //logicalName
            posPrinter.claim(0);
            posPrinter.setDeviceEnabled(true);
            hideConfigs();
            isPrinterConnected = true;
        } catch (final JposException e) {
            Log.e(TAG, "Error opening printer.", e);
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            showConfigs();
            isPrinterConnected = false;
            closePrinter();
        }
    }


    private void showConfigs() {
        deviceListView.setVisibility(View.VISIBLE);
        layoutPrint.setVisibility(View.VISIBLE);
        btnPrint.setVisibility(View.GONE);
    }
    private void hideConfigs() {
        deviceListView.setVisibility(View.GONE);
        layoutPrint.setVisibility(View.GONE);
        btnPrint.setVisibility(View.VISIBLE);
        checkIfSettingMenu();
    }

    private void closePrinter() {
       /* try {
            posPrinter.close();
        } catch (JposException e) {
            Log.e(TAG, "Error closing printer.", e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }
    void printTransactionData() throws JposException {
        try {
            ArrayList<String> printRows = new ArrayList<String>();
            List<PrinterDataAdapter.PrinterData> plist = DemoDummyData.INSTANCE.printList(this);
            printRows.add("--------------------------" + "\r\n");
            for (PrinterDataAdapter.PrinterData pdata :
                    plist) {
                printRows.add("" + pdata.getPrintRow() + "\r\n");
            }
            String[] headerArray = getHeader("Customer Copy");
            String[] footerArray = getFooter();
            String[] stringArray = new String[printRows.size()];
            stringArray = printRows.toArray(stringArray);
            final ByteBuffer buffer = buildBuffer();
            InputStream is = null;
            is = getResources().openRawResource(R.raw.logotwo);
            final Bitmap bitmap = BitmapFactory.decodeStream(is);
            posPrinter.printBitmap(buffer.getInt(0), bitmap,
                    posPrinter.getRecLineWidth(), POSPrinterConst.PTR_BM_LEFT);
            for (String hh : headerArray) {

                posPrinter.printNormal(buffer.getInt(0), hh);

            }
            for (String hh : stringArray) {

                posPrinter.printNormal(buffer.getInt(0), hh);

            }
            for (String hh : footerArray) {

                posPrinter.printNormal(buffer.getInt(0), hh);

            }
            //hide the printer button and customer copy issue
            showBuyerCopy();

        }catch (Exception e){
            new NetworkUtility().transactionWarning(getResources().getString(R.string.ensure_printconnect),this);
        }
    }

    private void showBuyerCopy() throws JposException {
        btnPrint.setVisibility( View.GONE);
        btnPrintBuyerCopy.setVisibility(View.VISIBLE);
        buyerPrinterCopy();
    }

    /**
     * print buyer copy
     */
    void buyerPrinterCopy() throws JposException {
        try{
        ArrayList<String> printRows = new ArrayList<String>();
        List<PrinterDataAdapter.PrinterData> plist = DemoDummyData.INSTANCE.printList(this);
        printRows.add("-------------------------------------------"+"\r\n");
        for (PrinterDataAdapter.PrinterData pdata:
                plist) {
            printRows.add(""+pdata.getPrintRow()+ "\r\n");
        }
        String[]headerArray = getHeader("Buyer Copy");
        String[]footerArray = getFooter();
        String[] stringArray = new String [printRows.size()];
        stringArray = printRows.toArray(stringArray);
        final ByteBuffer buffer = buildBuffer();
        InputStream is = null;
        is = getResources().openRawResource(R.raw.logotwo);
        final Bitmap bitmap = BitmapFactory.decodeStream(is);
        posPrinter.printBitmap(buffer.getInt(0), bitmap,
                posPrinter.getRecLineWidth(), POSPrinterConst.PTR_BM_LEFT);
        for (String hh :   headerArray) {

            posPrinter.printNormal(buffer.getInt(0),hh);

        }
        for (String hh :   stringArray) {

            posPrinter.printNormal(buffer.getInt(0),hh);

        }
        for (String hh :   footerArray) {

            posPrinter.printNormal(buffer.getInt(0),hh);

        }
        }catch (Exception e){
            new NetworkUtility().transactionWarning(getResources().getString(R.string.ensure_printconnect),this);
        }
    }

    /**
     * Round off to 2dp
     * and add comma separator.
     * @param wholeNumber
     * @return
     */
    public static String formatNumber(String wholeNumber){


        return wholeNumber;
    }




    private ByteBuffer buildBuffer() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put((byte) POSPrinterConst.PTR_S_RECEIPT);
        buffer.put((byte) brightness);
        int compress = 1;
        buffer.put((byte) compress);
        buffer.put((byte) 0x00);
        return buffer;
    }

    private void closeInputStream(final InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (final IOException e) {
                Log.e(TAG, "Error closing input stream.", e);
            }
        }
    }

    //footer
    private static String[] getFooter() {
        return new String[]{
                PrinterActivity.getSeparator("<<SEPARATOR>>"),
                "\n",
                "Served by: " + UtilPreference.Companion.getBuyerUsername() + "\n",
                "Powered by: " + "Cargill Co." + "\n",
                PrinterActivity.getSeparator("<<SEPARATOR>>"),
                "\n",
                "\n"
        };
    }
    String getStringFooter(){
        String[] ftString = getFooter();
        StringBuilder sb = new StringBuilder("");
        for (String hh :   ftString) {

           sb.append(hh);

        }
        return sb.toString();
    }

    String getStringHeader(){
        String[] ftString = getHeader("Customer Copy");
        StringBuilder sb = new StringBuilder("");
        for (String hh :   ftString) {

            sb.append(hh);

        }
        return sb.toString();
    }

    private static String[] getHeader( String copyOwner) {
        return new String[]{
                PrinterActivity.centeredText("***** " + copyOwner + " *****") + "\n",
                "\n",
                PrinterActivity.centeredText("Cargill Digital Payment") + "\n",
                PrinterActivity.centeredText("Code: "+ UtilPreference.Companion.getSectionCode()) + "\n",
                "\n",
                "DATE         TIME        TERMINAL ID" + "\n",
                new NetworkUtility().getUTCtime() + "     " +"122511415" + "\n",
               // "CODE: " + Config.AGENT_CODE + "\n",
                "TRAN NUM:  " +  "5225152525" + "\n",
                "SECTION: " + UtilPreference.Companion.getUserSection() + "\n",
                PrinterActivity.getSeparator("<<SEPARATOR>>")
        };
    }
    public static String getSeparator(String line) {
        return "--------------------------------";
    }
    public static String centeredText(String text) {
        if (TextUtils.isEmpty(text)) {
            return "";
        }
        int textLength = text.length();
        int paperSize = 48;

        if (textLength > paperSize) {
            String compactText = text.substring(0, paperSize);
            int lastSpaceIndex = compactText.lastIndexOf(" ");
            if (lastSpaceIndex <= 0) {
                return compactText + "\n" +
                        centeredText(text.substring(paperSize, textLength));
            } else {
                return centeredText(compactText.substring(0, lastSpaceIndex)) + "\n" +
                        centeredText(text.substring(lastSpaceIndex + 1, textLength));
            }
        } else if (textLength == paperSize) {
            return text;
        } else {
            int whiteSpaceLength = paperSize - textLength;
            int padding = whiteSpaceLength / 2;
            for (int i = 0; i < padding; i++) {
                text = " " + text;
            }
            return text;
        }
    }
}