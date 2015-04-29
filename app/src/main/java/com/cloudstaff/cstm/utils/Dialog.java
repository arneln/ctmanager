package com.cloudstaff.cstm.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Dialog {

    private static ProgressDialog mProgressDialog;

    /**
     * Complete Dialog
     *
     * @param context
     * @param title
     * @param message
     * @param positiveButtonText
     * @param negativeButtonText
     * @param positiveButtonClick
     * @param negativeButtonClick
     * @param cancellable
     */
    public static void showDialog(Context context, String title,
                                  String message, String positiveButtonText,
                                  String negativeButtonText,
                                  DialogInterface.OnClickListener positiveButtonClick,
                                  DialogInterface.OnClickListener negativeButtonClick, boolean cancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonClick)
                .setNegativeButton(negativeButtonText, negativeButtonClick)
                .setCancelable(cancellable);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Single Option Dialog
     *
     * @param context
     * @param title
     * @param message
     * @param positiveButtonText
     * @param positiveButtonClick
     * @param cancellable
     */
    public static void showSingleOptionDialog(Context context, String title,
                                              String message, String positiveButtonText,
                                              DialogInterface.OnClickListener positiveButtonClick, boolean cancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonClick)
                .setCancelable(cancellable);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Neutral Option Dialog
     *
     * @param context
     * @param title
     * @param message
     * @param positiveButtonText
     * @param negativeButtonText
     * @param neutralButtonText
     * @param positiveButtonClick
     * @param negativeButtonClick
     * @param neutralButtonClick
     * @param cancellable
     */
    public static void showNeutralDialog(Context context, String title,
                                         String message, String positiveButtonText,
                                         String negativeButtonText, String neutralButtonText,
                                         DialogInterface.OnClickListener positiveButtonClick,
                                         DialogInterface.OnClickListener negativeButtonClick,
                                         DialogInterface.OnClickListener neutralButtonClick, boolean cancellable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButtonClick)
                .setNegativeButton(negativeButtonText, negativeButtonClick)
                .setNeutralButton(neutralButtonText, neutralButtonClick)
                .setCancelable(cancellable);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Spinner Progress Dialog
     *
     * @param context
     * @param message
     * @param isCancellable
     */
    public static void showProgressDialog(Context context, String message,
                                          boolean isCancellable) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(message);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(isCancellable);
        mProgressDialog.show();
    }

    public static ProgressDialog getProgressDialog() {
        return mProgressDialog;
    }

    /**
     * Dismiss Progress Dialog!
     */
    public static void dismissProgressDialog() {
        mProgressDialog.dismiss();
    }
}
