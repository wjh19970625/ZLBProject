package com.wjh.utillibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wjh.utillibrary.R;
import com.wjh.utillibrary.utils.CommonUtil;


/**
 * Created by wjh on 2018/12/14 0014   15:30
 * Description 自定义dialog
 * Email: 2281837849@qq.com
 */

public class DialogTool {
    private Context mContext;
    private View mDialogView = null;
    private Dialog mDialog = null;
    private IDialogTool mIDialogTool = null;
    private OnDialogClickListener mOnDialogClickListener = null;
    private TextView mContent;
    private TextView mConfirm;
    private TextView mCancel;
    private View mLine;


    public DialogTool(Context context, IDialogTool idialogtool) {
        this.mContext = context;
        this.mIDialogTool = idialogtool;
    }

    public DialogTool(Context context) {
        this.mContext = context;
    }

    public DialogTool setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.mOnDialogClickListener = onDialogClickListener;
        return this;
    }

    private void infoShow() {
        mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_tool, null);
        mDialog = new Dialog(mContext, R.style.Dialog);
        mDialog.addContentView(mDialogView, new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT));
        mContent = mDialog.findViewById(R.id.content);
        mLine = mDialog.findViewById(R.id.line);
        mConfirm = mDialog.findViewById(R.id.confirm);
        mCancel = mDialog.findViewById(R.id.cancel);
        Window window = mDialog.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.width = (int) (CommonUtil.getScreenWidth(mContext)*0.75);
        window.setAttributes(attributes);
        mDialog.show();
    }

    /**
     * @param content 内容
     */
    public DialogTool dialogShow(String content) {
        infoShow();
        mContent.setText(content);
        mDialog.setCanceledOnTouchOutside(false);
        onClick();
        return this;
    }

    /**
     * 单个btn只有内容
     *
     * @param content 内容
     * @param btnName 按钮内容
     */
    public DialogTool dialogSingleBtnShow(String content, String btnName) {
        infoShow();
        mLine.setVisibility(View.GONE);
        mCancel.setVisibility(View.GONE);
        mContent.setText(content);
        mConfirm.setText(btnName);
        mDialog.setCanceledOnTouchOutside(false);
        onClick();
        return this;
    }

    /**
     * @param content     内容
     * @param ok          确定
     * @param cancle      取消
     */
    public DialogTool dialogShow(String content, String cancle, String ok) {
        infoShow();
        mContent.setText(Html.fromHtml(content));
        mCancel.setText(cancle);
        mConfirm.setText(ok);
        onClick();
        return this;
    }



    private void onClick() {
        /**
         * 确定
         */
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIDialogTool != null) {
                    mIDialogTool.onDialogOkClick();
                }
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onDialogOkClick();
                }
                mDialog.dismiss();
            }
        });
        /**
         * 取消
         */
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIDialogTool != null) {
                    mIDialogTool.onDialogCancelClick();
                }
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onDialogCancelClick();
                }
                mDialog.dismiss();
            }
        });
    }


    public interface OnDialogClickListener {
        void onDialogOkClick();

        void onDialogCancelClick();
    }


}
