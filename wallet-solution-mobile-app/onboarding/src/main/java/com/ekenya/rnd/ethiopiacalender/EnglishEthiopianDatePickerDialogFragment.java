package com.ekenya.rnd.ethiopiacalender;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.ekenya.rnd.common.storage.SharedPreferencesManager;
import com.ekenya.rnd.onboarding.MainActivity;
import com.ekenya.rnd.onboarding.R;
import com.ekenya.rnd.onboarding.databinding.FragmentDatePickerDialogBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class EnglishEthiopianDatePickerDialogFragment extends DialogFragment implements View.OnClickListener{

    private FragmentDatePickerDialogBinding binding;
    private EditText editText;
    private int currentYear, currentMonth, currentDay, selectedYear, selectedMonth, clickedDay, clickedDayMonth, clickedDayYear;
    private final String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final String[] ethMonths = {"Meskerem", "Tikimit", "Hidar", "Tahisas",
            "Tir", "Yekatit", "Megabit", "Miyazia", "Ginbot", "Sene", "Hamile",
            "Nehase", "Pagume"};


    public EnglishEthiopianDatePickerDialogFragment(EditText x) {
        this.editText=x;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDatePickerDialogBinding.inflate(inflater, container, false);

        initializeUI();

        return binding.getRoot();
    }

    private void initializeUI() {
        Calendar cal = Calendar.getInstance();
        int[] values = new EthiopicCalendar(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)).gregorianToEthiopic();
        currentDay = values[2];
        clickedDay = currentDay;
        currentMonth = values[1];
        clickedDayMonth = currentMonth;
        currentYear = values[0];
        clickedDayYear = currentYear;
        binding.selectedDate.setText(values[2] + ", " + ethMonths[values[1] - 1] + " " + values[0]);

        binding.prevMonthBtn.setOnClickListener(this);
        binding.nextMonthBtn.setOnClickListener(this);
        binding.ok.setOnClickListener(this);
        binding.cancel.setOnClickListener(this);
        binding.selectedYear.setOnClickListener(this);

        setSpinnerData();
        setTextViewListeners();
        drawCalendar(currentYear, currentMonth);
    }

    public void drawCalendar(int year, int month) {
        clearAllTextViews();

        binding.selectedDate.setText(clickedDay + ", " + ethMonths[clickedDayMonth - 1] + " " + clickedDayYear);
        binding.selectedMonth.setText(ethMonths[month - 1]);
        binding.selectedYear.setText(String.valueOf(year));
        selectedYear = year;
        selectedMonth = month;

        // Set Heading
        binding.rowHeadingColOne.setText(days[0]);
        binding.rowHeadingColTwo.setText(days[1]);
        binding.rowHeadingColThree.setText(days[2]);
        binding.rowHeadingColFour.setText(days[3]);
        binding.rowHeadingColFive.setText(days[4]);
        binding.rowHeadingColSix.setText(days[5]);
        binding.rowHeadingColSeven.setText(days[6]);

        /*
         * Heading done
         */
        int startDayOfWeek;

        EthiopicCalendar ethCal = new EthiopicCalendar(year, month, 1);
        int[] values = ethCal.ethiopicToGregorian();
        Calendar cal = Calendar.getInstance();
        cal.set(values[0], values[1] - 1, values[2]);
        startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int day, maxDay;
        maxDay = 30;
        if (month == 13) {
            maxDay = (((year + 1) % 4) == 0) ? 6 : 5;
        }

        clearBackground();
        // Set row one
        day = (((7 * 0) + 1) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColOne.setText(String.valueOf(day));
            setUp(binding.rowOneColOne, day);
        }
        day = (((7 * 0) + 2) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColTwo.setText(String.valueOf(day));
            setUp(binding.rowOneColTwo, day);
        }
        day = (((7 * 0) + 3) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColThree.setText(String.valueOf(day));
            setUp(binding.rowOneColThree, day);
        }
        day = (((7 * 0) + 4) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColFour.setText(String.valueOf(day));
            setUp(binding.rowOneColFour, day);
        }
        day = (((7 * 0) + 5) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColFive.setText(String.valueOf(day));
            setUp(binding.rowOneColFive, day);
        }
        day = (((7 * 0) + 6) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColSix.setText(String.valueOf(day));
            setUp(binding.rowOneColSix, day);
        }
        day = (((7 * 0) + 7) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowOneColSeven.setText(String.valueOf(day));
            setUp(binding.rowOneColSeven, day);
        }

        // Set row two
        day = (((7 * 1) + 1) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColOne.setText(String.valueOf(day));
            setUp(binding.rowTwoColOne, day);
        }
        day = (((7 * 1) + 2) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColTwo.setText(String.valueOf(day));
            setUp(binding.rowTwoColTwo, day);
        }
        day = (((7 * 1) + 3) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColThree.setText(String.valueOf(day));
            setUp(binding.rowTwoColThree, day);
        }
        day = (((7 * 1) + 4) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColFour.setText(String.valueOf(day));
            setUp(binding.rowTwoColFour, day);
        }
        day = (((7 * 1) + 5) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColFive.setText(String.valueOf(day));
            setUp(binding.rowTwoColFive, day);
        }
        day = (((7 * 1) + 6) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColSix.setText(String.valueOf(day));
            setUp(binding.rowTwoColSix, day);
        }
        day = (((7 * 1) + 7) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowTwoColSeven.setText(String.valueOf(day));
            setUp(binding.rowTwoColSeven, day);
        }

        // Set row three
        day = (((7 * 2) + 1) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColOne.setText(String.valueOf(day));
            setUp(binding.rowThreeColOne, day);
        }
        day = (((7 * 2) + 2) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColTwo.setText(String.valueOf(day));
            setUp(binding.rowThreeColTwo, day);
        }
        day = (((7 * 2) + 3) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColThree.setText(String.valueOf(day));
            setUp(binding.rowThreeColThree, day);
        }
        day = (((7 * 2) + 4) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColFour.setText(String.valueOf(day));
            setUp(binding.rowThreeColFour, day);
        }
        day = (((7 * 2) + 5) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColFive.setText(String.valueOf(day));
            setUp(binding.rowThreeColFive, day);
        }
        day = (((7 * 2) + 6) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColSix.setText(String.valueOf(day));
            setUp(binding.rowThreeColSix, day);
        }
        day = (((7 * 2) + 7) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowThreeColSeven.setText(String.valueOf(day));
            setUp(binding.rowThreeColSeven, day);
        }

        // Set row four
        day = (((7 * 3) + 1) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColOne.setText(String.valueOf(day));
            setUp(binding.rowFourColOne, day);
        }
        day = (((7 * 3) + 2) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColTwo.setText(String.valueOf(day));
            setUp(binding.rowFourColTwo, day);
        }
        day = (((7 * 3) + 3) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColThree.setText(String.valueOf(day));
            setUp(binding.rowFourColThree, day);
        }
        day = (((7 * 3) + 4) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColFour.setText(String.valueOf(day));
            setUp(binding.rowFourColFour, day);
        }
        day = (((7 * 3) + 5) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColFive.setText(String.valueOf(day));
            setUp(binding.rowFourColFive, day);
        }
        day = (((7 * 3) + 6) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColSix.setText(String.valueOf(day));
            setUp(binding.rowFourColSix, day);
        }
        day = (((7 * 3) + 7) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFourColSeven.setText(String.valueOf(day));
            setUp(binding.rowFourColSeven, day);
        }

        // Set row five
        day = (((7 * 4) + 1) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColOne.setText(String.valueOf(day));
            setUp(binding.rowFiveColOne, day);
        }
        day = (((7 * 4) + 2) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColTwo.setText(String.valueOf(day));
            setUp(binding.rowFiveColTwo, day);
        }
        day = (((7 * 4) + 3) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColThree.setText(String.valueOf(day));
            setUp(binding.rowFiveColThree, day);
        }
        day = (((7 * 4) + 4) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColFour.setText(String.valueOf(day));
            setUp(binding.rowFiveColFour, day);
        }
        day = (((7 * 4) + 5) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColFive.setText(String.valueOf(day));
            setUp(binding.rowFiveColFive, day);
        }
        day = (((7 * 4) + 6) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColSix.setText(String.valueOf(day));
            setUp(binding.rowFiveColSix, day);
        }
        day = (((7 * 4) + 7) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowFiveColSeven.setText(String.valueOf(day));
            setUp(binding.rowTwoColSeven, day);
        }

        // Set row six
        day = (((7 * 5) + 1) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowSixColOne.setText(String.valueOf(day));
            setUp(binding.rowSixColOne, day);
        }
        day = (((7 * 5) + 2) - startDayOfWeek) + 1;
        if (day > 0 && day <= maxDay) {
            binding.rowSixColTwo.setText(String.valueOf(day));
            setUp(binding.rowSixColTwo, day);
        }

    }

//    public void setSpinnerData(){
//        ArrayList<Integer> years = new ArrayList<Integer>();
//        for (int i = 1900; i <= 2100; i++) {
//            years.add(i);
//        }
//        ArrayAdapter<String> adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years);
//        binding.spinner.setAdapter(adapter);
//        binding.spinner.setSelection(years.indexOf(clickedDayYear));
//        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                Log.e("Years",years.toString());
//                Integer year = years.get(position);
//                Log.e("ValuesH", ""+selectedYear+" "+year);
//                if (currentYear == year){
//                    selectedYear = year;
//                    selectedMonth = currentMonth;
//                    clickedDayYear = selectedYear;
//                    clickedDayMonth = selectedMonth;
//                    clickedDay = currentDay;
//                    binding.yearSpinnerView.setVisibility(View.GONE);
//                    drawCalendar(selectedYear, selectedMonth);
//                } else if (selectedYear != year){
//                    selectedYear = year;
//                    selectedMonth = 1;
//                    clickedDayYear = year;
//                    clickedDayMonth = selectedMonth;
//                    clickedDay = 1;
//                    binding.yearSpinnerView.setVisibility(View.GONE);
//                    drawCalendar(selectedYear, selectedMonth);
//                } else if (selectedYear == year){
//                    clickedDayYear = year;
//                    binding.yearSpinnerView.setVisibility(View.GONE);
//                    drawCalendar(selectedYear, selectedMonth);
//                }
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> arg0) {
//                Log.e("ValuesH", "nothing");
//                binding.yearSpinnerView.setVisibility(View.GONE);
//                drawCalendar(selectedYear, selectedMonth);
//            }
//        });
//    }

    public void setSpinnerData(){
        ArrayList<Integer> years = new ArrayList<Integer>();
        for (int i = 1900; i <= 2100; i++) {
            years.add(i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setSelection(years.indexOf(clickedDayYear));
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ((TextView) binding.spinner.getSelectedView()).setTextColor(getResources().getColor(R.color.accent));
                Integer year = years.get(position);
                if (currentYear == year){
                    selectedYear = year;
                    selectedMonth = currentMonth;
                    clickedDayYear = selectedYear;
                    clickedDayMonth = selectedMonth;
                    clickedDay = currentDay;
                    drawCalendar(selectedYear, selectedMonth);
                } else if (selectedYear != year){
                    selectedYear = year;
                    selectedMonth = 1;
                    clickedDayYear = year;
                    clickedDayMonth = selectedMonth;
                    clickedDay = 1;
                    drawCalendar(selectedYear, selectedMonth);
                } else if (selectedYear == year){
                    clickedDayYear = year;
                    drawCalendar(selectedYear, selectedMonth);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                drawCalendar(selectedYear, selectedMonth);
            }
        });
    }



    public void setPlainBackground(TextView tv) {
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setBackground(null);
    }

    public void setUp(TextView tv, Integer day) {
        if (selectedYear > currentYear
                || (selectedYear == currentYear && selectedMonth > currentMonth)
                || (selectedYear == currentYear && selectedMonth == currentMonth && day >= currentDay)) {
            registerForContextMenu(tv);
        }
        if (currentDay == day && currentYear == selectedYear && currentMonth == selectedMonth) {
            if (clickedDay == day) {
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setBackgroundResource(R.drawable.circle_background);
            } else {
                tv.setTextColor(getResources().getColor(R.color.accent));
                tv.setBackground(null);
            }
        } else {
            if (clickedDay == day && clickedDayMonth == selectedMonth && clickedDayYear == selectedYear) {
                tv.setTextColor(getResources().getColor(R.color.white));
                tv.setBackgroundResource(R.drawable.circle_background);
            } else {
                tv.setTextColor(getResources().getColor(R.color.black));
                tv.setBackground(null);
            }
        }
    }

    public void clearBackground() {
        setPlainBackground(binding.rowOneColOne);
        setPlainBackground(binding.rowOneColTwo);
        setPlainBackground(binding.rowOneColThree);
        setPlainBackground(binding.rowOneColFour);
        setPlainBackground(binding.rowOneColFive);
        setPlainBackground(binding.rowOneColSix);
        setPlainBackground(binding.rowOneColSeven);

        setPlainBackground(binding.rowTwoColOne);
        setPlainBackground(binding.rowTwoColTwo);
        setPlainBackground(binding.rowTwoColThree);
        setPlainBackground(binding.rowTwoColFour);
        setPlainBackground(binding.rowTwoColFive);
        setPlainBackground(binding.rowTwoColSix);
        setPlainBackground(binding.rowTwoColSeven);

        setPlainBackground(binding.rowThreeColOne);
        setPlainBackground(binding.rowThreeColTwo);
        setPlainBackground(binding.rowThreeColThree);
        setPlainBackground(binding.rowThreeColFour);
        setPlainBackground(binding.rowThreeColFive);
        setPlainBackground(binding.rowThreeColSix);
        setPlainBackground(binding.rowThreeColSeven);

        setPlainBackground(binding.rowFourColOne);
        setPlainBackground(binding.rowFourColTwo);
        setPlainBackground(binding.rowFourColThree);
        setPlainBackground(binding.rowFourColFour);
        setPlainBackground(binding.rowFourColFive);
        setPlainBackground(binding.rowFourColSix);
        setPlainBackground(binding.rowFourColSeven);

        setPlainBackground(binding.rowFiveColOne);
        setPlainBackground(binding.rowFiveColTwo);
        setPlainBackground(binding.rowFiveColThree);
        setPlainBackground(binding.rowFiveColFour);
        setPlainBackground(binding.rowFiveColFive);
        setPlainBackground(binding.rowFiveColSix);
        setPlainBackground(binding.rowFiveColSeven);

        setPlainBackground(binding.rowSixColOne);
        setPlainBackground(binding.rowSixColTwo);
    }

    public void clearAllTextViews() {
        binding.rowOneColOne.setText("");
        binding.rowOneColTwo.setText("");
        binding.rowOneColThree.setText("");
        binding.rowOneColFour.setText("");
        binding.rowOneColFive.setText("");
        binding.rowOneColSix.setText("");
        binding.rowOneColSeven.setText("");

        binding.rowTwoColOne.setText("");
        binding.rowTwoColTwo.setText("");
        binding.rowTwoColThree.setText("");
        binding.rowTwoColFour.setText("");
        binding.rowTwoColFive.setText("");
        binding.rowTwoColSix.setText("");
        binding.rowTwoColSeven.setText("");

        binding.rowThreeColOne.setText("");
        binding.rowThreeColTwo.setText("");
        binding.rowThreeColThree.setText("");
        binding.rowThreeColFour.setText("");
        binding.rowThreeColFive.setText("");
        binding.rowThreeColSix.setText("");
        binding.rowThreeColSeven.setText("");

        binding.rowFourColOne.setText("");
        binding.rowFourColTwo.setText("");
        binding.rowFourColThree.setText("");
        binding.rowFourColFour.setText("");
        binding.rowFourColFive.setText("");
        binding.rowFourColSix.setText("");
        binding.rowFourColSeven.setText("");

        binding.rowFiveColOne.setText("");
        binding.rowFiveColTwo.setText("");
        binding.rowFiveColThree.setText("");
        binding.rowFiveColFour.setText("");
        binding.rowFiveColFive.setText("");
        binding.rowFiveColSix.setText("");
        binding.rowFiveColSeven.setText("");

        binding.rowSixColOne.setText("");
        binding.rowSixColTwo.setText("");
    }

    public void setTextViewListeners() {
        binding.rowOneColOne.setOnClickListener(this);
        binding.rowOneColTwo.setOnClickListener(this);
        binding.rowOneColThree.setOnClickListener(this);
        binding.rowOneColFour.setOnClickListener(this);
        binding.rowOneColFive.setOnClickListener(this);
        binding.rowOneColSix.setOnClickListener(this);
        binding.rowOneColSeven.setOnClickListener(this);

        binding.rowTwoColOne.setOnClickListener(this);
        binding.rowTwoColTwo.setOnClickListener(this);
        binding.rowTwoColThree.setOnClickListener(this);
        binding.rowTwoColFour.setOnClickListener(this);
        binding.rowTwoColFive.setOnClickListener(this);
        binding.rowTwoColSix.setOnClickListener(this);
        binding.rowTwoColSeven.setOnClickListener(this);

        binding.rowThreeColOne.setOnClickListener(this);
        binding.rowThreeColTwo.setOnClickListener(this);
        binding.rowThreeColThree.setOnClickListener(this);
        binding.rowThreeColFour.setOnClickListener(this);
        binding.rowThreeColFive.setOnClickListener(this);
        binding.rowThreeColSix.setOnClickListener(this);
        binding.rowThreeColSeven.setOnClickListener(this);

        binding.rowFourColOne.setOnClickListener(this);
        binding.rowFourColTwo.setOnClickListener(this);
        binding.rowFourColThree.setOnClickListener(this);
        binding.rowFourColFour.setOnClickListener(this);
        binding.rowFourColFive.setOnClickListener(this);
        binding.rowFourColSix.setOnClickListener(this);
        binding.rowFourColSeven.setOnClickListener(this);

        binding.rowFiveColOne.setOnClickListener(this);
        binding.rowFiveColTwo.setOnClickListener(this);
        binding.rowFiveColThree.setOnClickListener(this);
        binding.rowFiveColFour.setOnClickListener(this);
        binding.rowFiveColFive.setOnClickListener(this);
        binding.rowFiveColSix.setOnClickListener(this);
        binding.rowFiveColSeven.setOnClickListener(this);

        binding.rowSixColOne.setOnClickListener(this);
        binding.rowSixColTwo.setOnClickListener(this);
    }

    public void onClick(View v) {
        String value;
        Intent intent = new Intent(requireContext(), MainActivity.class);
        switch (v.getId()) {
            // Switched month
            case R.id.prevMonthBtn:
                if (selectedMonth > 1) {
                    selectedMonth = selectedMonth - 1;
                } else {
                    selectedMonth = 13;
                    selectedYear = selectedYear - 1;
                }
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.nextMonthBtn:
                if (selectedMonth == 13) {
                    selectedMonth = 1;
                    selectedYear = selectedYear + 1;
                } else {
                    selectedMonth = selectedMonth + 1;
                }
                drawCalendar(selectedYear, selectedMonth);
                break;

         /*   // Selected different year
            case R.id.prevYearBtn:
                selectedYear--;
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.nextYearBtn:
                selectedYear++;
                drawCalendar(selectedYear, selectedMonth);
                break;
*/

            // Selected dates - Row 1
            case R.id.rowOneColOne:
                value = binding.rowOneColOne.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowOneColTwo:
                value = binding.rowOneColTwo.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowOneColThree:
                value = binding.rowOneColThree.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowOneColFour:
                value = binding.rowOneColFour.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowOneColFive:
                value = binding.rowOneColFive.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowOneColSix:
                value = binding.rowOneColSix.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowOneColSeven:
                value = binding.rowOneColSeven.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;

            // Selected dates - Row 2
            case R.id.rowTwoColOne:
                value = binding.rowTwoColOne.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowTwoColTwo:
                value = binding.rowTwoColTwo.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowTwoColThree:
                value = binding.rowTwoColThree.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowTwoColFour:
                value = binding.rowTwoColFour.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowTwoColFive:
                value = binding.rowTwoColFive.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowTwoColSix:
                value = binding.rowTwoColSix.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowTwoColSeven:
                value = binding.rowTwoColSeven.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;

            // Selected dates - Row 3
            case R.id.rowThreeColOne:
                value = binding.rowThreeColOne.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowThreeColTwo:
                value = binding.rowThreeColTwo.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowThreeColThree:
                value = binding.rowThreeColThree.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowThreeColFour:
                value = binding.rowThreeColFour.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowThreeColFive:
                value = binding.rowThreeColFive.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowThreeColSix:
                value = binding.rowThreeColSix.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowThreeColSeven:
                value = binding.rowThreeColSeven.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;

            // Selected dates - Row 4
            case R.id.rowFourColOne:
                value = binding.rowFourColOne.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFourColTwo:
                value = binding.rowFourColTwo.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFourColThree:
                value = binding.rowFourColThree.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFourColFour:
                value = binding.rowFourColFour.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFourColFive:
                value = binding.rowFourColFive.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFourColSix:
                value = binding.rowFourColSix.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFourColSeven:
                value = binding.rowFourColSeven.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;

            // Selected dates - Row 5
            case R.id.rowFiveColOne:
                value = binding.rowFiveColOne.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFiveColTwo:
                value = binding.rowFiveColTwo.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFiveColThree:
                value = binding.rowFiveColThree.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFiveColFour:
                value = binding.rowFiveColFour.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFiveColFive:
                value = binding.rowFiveColFive.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFiveColSix:
                value = binding.rowFiveColSix.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowFiveColSeven:
                value = binding.rowFiveColSeven.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;

            // Selected dates - Row 6
            case R.id.rowSixColOne:
                value = binding.rowSixColOne.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;
            case R.id.rowSixColTwo:
                value = binding.rowSixColTwo.getText().toString();
                clickedDay = Integer.parseInt(value);
                clickedDayMonth = selectedMonth;
                clickedDayYear = selectedYear;
                Log.e("RowClicked: ", value + " - " + selectedMonth + " - " + selectedYear);
                drawCalendar(selectedYear, selectedMonth);
                break;

            // Click Ok
            case R.id.ok:
                String date = clickedDay +" - "+ clickedDayMonth +" - "+ clickedDayYear;
               // intent = new Intent(requireContext(), MainActivity.class);
                //intent.putExtra("SelectedDate", date);
                // startActivity(intent);
                SimpleDateFormat df2 = new SimpleDateFormat("dd - MM - yyyy");
                //editText.setText(df2.format(date));
                try {
                    Date newDate =df2.parse(date);
                    df2= new SimpleDateFormat("EEE, dd MMM yyyy");
                    date=df2.format(newDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //var spf = SimpleDateFormat("dd - MM - yyyy")

                editText.setText(date);

                dismiss();
                break;

            // Click Cancel
            case R.id.cancel:
                dismiss();
                //startActivity(intent);
                break;

            // Click Year
            case R.id.selectedYear:
                binding.spinner.performClick();
                break;

            default:
                Log.e("ViewClicked", "Clicked view ID: " + v.getId());
                break;
        }
    }
}