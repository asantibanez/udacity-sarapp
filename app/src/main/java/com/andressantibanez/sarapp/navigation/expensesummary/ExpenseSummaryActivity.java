package com.andressantibanez.sarapp.navigation.expensesummary;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.andressantibanez.sarapp.R;
import com.andressantibanez.sarapp.Utils;
import com.andressantibanez.sarapp.endpoints.dtos.ExpenseSummaryResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.joda.time.DateTime;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpenseSummaryActivity extends AppCompatActivity {

    int mYear;
    ExpenseSummaryResponse mExpenseSummary;

    @Bind(R.id.chart) PieChart mPieChart;
    @Bind(R.id.not_assigned_amount) TextView mNotAssignedAmountView;
    @Bind(R.id.feeding_amount) TextView mFeedingAmountView;
    @Bind(R.id.health_amount) TextView mHealthAmountView;
    @Bind(R.id.clothing_amount) TextView mClothingAmountView;
    @Bind(R.id.dwelling_amount) TextView mDwellingAmountView;
    @Bind(R.id.education_amount) TextView mEducationAmountView;
    @Bind(R.id.other_amount) TextView mOtherAmountView;
    @Bind(R.id.grand_total) TextView mGrandTotalView;

    public static Intent launchIntent(Context context) {
        return new Intent(context, ExpenseSummaryActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary);
        ButterKnife.bind(this);

        mYear = DateTime.now().getYear() - 1;

        //Get summary on start
        getSupportLoaderManager().initLoader(1000, null, new LoaderManager.LoaderCallbacks<ExpenseSummaryResponse>() {
            @Override
            public Loader<ExpenseSummaryResponse> onCreateLoader(int id, Bundle args) {
                return new ExpenseSummaryLoader(ExpenseSummaryActivity.this, mYear);
            }

            @Override
            public void onLoadFinished(Loader<ExpenseSummaryResponse> loader, ExpenseSummaryResponse data) {
                displayData(data);
            }

            @Override
            public void onLoaderReset(Loader<ExpenseSummaryResponse> loader) {

            }
        });
    }

    public void displayData(ExpenseSummaryResponse expenseSummary) {
        mExpenseSummary = expenseSummary;

        if(mExpenseSummary.hasErrors()) {
            //TODO: handle no data gracefully
            return;
        }

        //Setup totals
        mNotAssignedAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.none.total));
        mFeedingAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.feeding.total));
        mHealthAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.health.total));
        mClothingAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.clothing.total));
        mDwellingAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.dwelling.total));
        mEducationAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.education.total));
        mOtherAmountView.setText(Utils.asMoneyString(mExpenseSummary.expenseTypes.other.total));
        mGrandTotalView.setText(Utils.asMoneyString(mExpenseSummary.total));

        //Setup pie chart
        setupPieChart();
    }

    private void setupPieChart() {

        int i = 0;
        ArrayList<Entry> yVals = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        xVals.add(getString(R.string.not_assigned));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.none.total, i));
        i++;

        xVals.add(getString(R.string.feeding));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.feeding.total, i));
        i++;

        xVals.add(getString(R.string.health));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.health.total, i));
        i++;

        xVals.add(getString(R.string.clothing));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.clothing.total, i));
        i++;

        xVals.add(getString(R.string.dwelling));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.dwelling.total, i));
        i++;

        xVals.add(getString(R.string.education));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.education.total, i));
        i++;

        xVals.add(getString(R.string.other));
        yVals.add(new Entry((float) mExpenseSummary.expenseTypes.other.total, i));
        i++;

        PieDataSet dataSet = new PieDataSet(yVals, "Expense Types");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.none_color));
        colors.add(getResources().getColor(R.color.feeding_color));
        colors.add(getResources().getColor(R.color.health_color));
        colors.add(getResources().getColor(R.color.clothing_color));
        colors.add(getResources().getColor(R.color.dwelling_color));
        colors.add(getResources().getColor(R.color.education_color));
        colors.add(getResources().getColor(R.color.other_color));
        dataSet.setColors(colors);

        PieData data = new PieData(xVals, dataSet);
        data.setDrawValues(false);

        mPieChart.setData(data);
        mPieChart.setDescription("");
        mPieChart.setHoleRadius(30f);
        mPieChart.setTransparentCircleRadius(35f);
        mPieChart.setHoleColor(getResources().getColor(R.color.colorPrimary));
        mPieChart.getLegend().setEnabled(false);
        mPieChart.setTouchEnabled(false);
        mPieChart.setDrawSliceText(false);
        mPieChart.highlightValues(null);
        mPieChart.invalidate();

        mPieChart.animateXY(1500, 1500);
    }
}
