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

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpenseSummaryActivity extends AppCompatActivity {

    int mYear;

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
        if(expenseSummary == null) {
            //TODO: handle no data gracefully
            return;
        }

        mNotAssignedAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.none.total));
        mFeedingAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.feeding.total));
        mHealthAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.health.total));
        mClothingAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.clothing.total));
        mDwellingAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.dwelling.total));
        mEducationAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.education.total));
        mOtherAmountView.setText(Utils.asMoneyString(expenseSummary.expenseTypes.other.total));
        mGrandTotalView.setText(Utils.asMoneyString(expenseSummary.total));
    }
}
