package com.andressantibanez.sarapp.navigation.expensesummary;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.andressantibanez.sarapp.Sarapp;
import com.andressantibanez.sarapp.endpoints.SarappWebService;
import com.andressantibanez.sarapp.endpoints.dtos.ExpenseSummaryResponse;

/**
 * Created by Andrés on 2/26/16.
 */
public class ExpenseSummaryLoader extends AsyncTaskLoader<ExpenseSummaryResponse> {

    int mYear;
    ExpenseSummaryResponse mExpenseSummary;

    public ExpenseSummaryLoader(Context context, int year) {
        super(context);
        mYear = year;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ExpenseSummaryResponse loadInBackground() {
        mExpenseSummary = SarappWebService.create()
                .getExpenseSummary(Sarapp.instance().getToken(), mYear);

        return mExpenseSummary;
    }
}
